package minecraft.sightworld.bungeeapi.entity;

import lombok.experimental.UtilityClass;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.user.ConsoleUser;
import minecraft.sightworld.defaultlib.user.User;
import minecraft.sightworld.defaultlib.user.UserData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@UtilityClass
public class BungeeEntityManager {

    public User<?> getEntity(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return SightWorld.getUserService().getUser(sender.getName());
        } else {
            return new ConsoleUser(sender.getName(), new UserData());
        }
    }

}
