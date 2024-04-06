package minecraft.sightworld.bukkitapi.entity;

import lombok.experimental.UtilityClass;
import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.user.ConsoleUser;
import minecraft.sightworld.defaultlib.user.User;
import minecraft.sightworld.defaultlib.user.UserData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@UtilityClass
public class BukkitEntityManager {

    public User<?> getEntity(CommandSender sender) {
        if (sender instanceof Player) {
            return SightWorld.getUserService().getUser(sender.getName());
        } else {
            return new ConsoleUser(sender.getName(), new UserData());
        }
    }

}
