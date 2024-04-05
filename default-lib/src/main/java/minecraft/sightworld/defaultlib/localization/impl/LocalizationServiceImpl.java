package minecraft.sightworld.defaultlib.localization.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import minecraft.sightworld.defaultlib.localization.Language;
import minecraft.sightworld.defaultlib.localization.LocalizationService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LocalizationServiceImpl implements LocalizationService {
    private final Map<String, LocalizationStorage> storageMap = new ConcurrentHashMap<>();

    private final String[] listFiles = new String[]{"main", "tab", "motd", "chat", "command"};
    public LocalizationServiceImpl() {

        for (Language language : Language.values()) {
            storageMap.put(language.getAbbreviatedName().toLowerCase(), new LocalizationStorage(language));
        }
    }

    @Override
    public String[] getFiles() {
        return listFiles;
    }

    @Override
    public void download(String filePrefix, String fileUrl) {
        try {
            URL url = new URL(String.format(fileUrl));

            InputStreamReader inputStream = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);

            parseKeys(JsonParser.parseReader(inputStream).getAsJsonObject().entrySet(), filePrefix);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadFile(String filePrefix, String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            parseKeys(JsonParser.parseReader(inputStreamReader).getAsJsonObject().entrySet(), filePrefix);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String get(Language type, String key, Object... format) {
        String message = getStorage(type).getMessage(key);

        if (message == null) {
            message = getStorage(Language.RUSSIAN).getMessage(key);

            if (message == null) {
                return key;
            }
        }

        return replacePlaceholders(message, format);
    }

    @Override
    public List<String> getList(Language type, String key, Object... format) {
        List<String> messageList = getStorage(type).getMessageList(key);

        if (messageList.isEmpty()) {
            messageList = getStorage(Language.RUSSIAN).getMessageList(key);

            if (messageList.isEmpty()) {
                return Collections.singletonList(key);
            }
        }

        String joinedMessages = String.join("\n", messageList);
        String formattedMessage = replacePlaceholders(joinedMessages, format);

        return Arrays.asList(formattedMessage.split("\\n"));
    }


    private LocalizationStorage getStorage(Language language) {
        return storageMap.get(language.getAbbreviatedName().toLowerCase());
    }

    private void parseKeys(Set<Map.Entry<String, JsonElement>> keys, String fileName) {
        keys.forEach(stringJsonElementEntry -> {
            String keyName = fileName + "_" + stringJsonElementEntry.getKey();

            parseMessage(keyName, stringJsonElementEntry);
        });
    }

    private void parseMessage(String keyName, Map.Entry<String, JsonElement> keyGroup) {
        keyGroup.getValue().getAsJsonObject().entrySet().forEach(key -> {
            if (key.getValue().isJsonObject()) {
                parseMessage(keyName + "_" + key.getKey().toLowerCase(), key);
                return;
            }

            saveKey(keyName, key);
        });
    }

    private static final Type collectionType = new TypeToken<List<String>>() {

    }.getType();

    public void saveKey(String keyName, Map.Entry<String, JsonElement> localeMessage) {
        String languageName = localeMessage.getKey().toLowerCase();
        LocalizationStorage languageStorage = storageMap.get(languageName);

        if (localeMessage.getValue().isJsonArray())
            languageStorage.addMessageList(keyName, new Gson().fromJson(localeMessage.getValue(), collectionType));
        else
            languageStorage.addMessage(keyName, localeMessage.getValue().getAsString());
    }


    private String replacePlaceholders(String input, Object... format) {
        if (format.length == 0) {
            return input;
        }
        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();
        int count = 0;

        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(format[0])) {
                if (count < format.length - 1) {
                    result.append(format[count + 1]);
                    count++;
                } else {
                    result.append(words[i]);
                }
            } else {
                result.append(words[i]);
            }

            if (i < words.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }

}
