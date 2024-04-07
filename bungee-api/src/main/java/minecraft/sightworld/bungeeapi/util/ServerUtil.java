package minecraft.sightworld.bungeeapi.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.connection.Server;

@UtilityClass
public class ServerUtil {

    public String getName(Server server) {
        if (server == null) {
            return "N/A";
        }
        if (server.getInfo() == null) {
            return "N/A";
        }
        return server.getInfo().getName();
    }

}
