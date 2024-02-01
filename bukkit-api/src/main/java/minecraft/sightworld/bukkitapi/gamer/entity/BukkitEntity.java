package minecraft.sightworld.bukkitapi.gamer.entity;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface BukkitEntity {
    CommandSender getCommandSender();

    void sendMessage(String message);
    void sendMessages(List<String> messages);

    String getName();

    String getChatName();

    boolean hasPermission(String permission);
    boolean isHuman();
}
