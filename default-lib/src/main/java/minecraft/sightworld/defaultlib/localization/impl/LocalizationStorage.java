package minecraft.sightworld.defaultlib.localization.impl;

import lombok.Getter;
import minecraft.sightworld.defaultlib.localization.Language;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
class LocalizationStorage {
    private final Language language;

    public LocalizationStorage(Language language) {
        this.language = language;
    }

    private final Map<String, String> messageMap = new ConcurrentHashMap<>();
    private final Map<String, List<String>> messageListMap = new ConcurrentHashMap<>();

    public void addMessage(String key, String value) {
        messageMap.put(key.toLowerCase(), value);
    }

    public String getMessage(String key) {
        key = key.toLowerCase();

        if (messageMap.containsKey(key)) {
            return messageMap.get(key);
        }

        return null;
    }

    public void addMessageList(String key, List<String> messageList) {
        messageListMap.put(key.toLowerCase(), messageList);
    }

    public List<String> getMessageList(String key) {
        key = key.toLowerCase();

        if (messageListMap.containsKey(key)) {
            return messageListMap.get(key);
        }

        return Collections.emptyList();
    }
}
