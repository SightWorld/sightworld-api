package minecraft.sightworld.defaultlib.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@UtilityClass
public class PastebinUtil {

    @SneakyThrows
    public String generateURL(String pasteContent) {

        URL url = new URL("https://paste.md-5.net/documents");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "text/plain");
        con.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(pasteContent.getBytes());
        }

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        String key = jsonResponse.get("key").getAsString();

        return "https://paste.md-5.net/" + key;
    }
}
