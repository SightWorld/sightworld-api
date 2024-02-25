package minecraft.sightworld.bukkitapi.manager;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

public class LuckPermsManager {

    private static final LuckPerms api = LuckPermsProvider.get();

    private static User playerToUser(String name) {
        return api.getUserManager().getUser(name);
    }

    public static String getPrefix(String name) {
        return playerToUser(name).getCachedData().getMetaData().getPrefix();
    }

    public static String getTabPriority(String name) {
        return playerToUser(name).getCachedData().getMetaData().getMetaValue("height");
    }
}
