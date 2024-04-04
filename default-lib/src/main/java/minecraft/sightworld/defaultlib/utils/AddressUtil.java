package minecraft.sightworld.defaultlib.utils;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

@UtilityClass
public class AddressUtil {

    public String getLocationFromIP(InetAddress ip) {
        try {
            String ipAddress = ip.getHostAddress();
            URL url = new URL("https://ipinfo.io/" + ipAddress + "/json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            String city = jsonObject.get("city").getAsString();
            String country = jsonObject.get("country").getAsString();

            return country + ", " + city;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
