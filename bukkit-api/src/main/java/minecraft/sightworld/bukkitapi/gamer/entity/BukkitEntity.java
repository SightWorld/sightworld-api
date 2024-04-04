package minecraft.sightworld.bukkitapi.gamer.entity;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface BukkitEntity {
    CommandSender getCommandSender();

    void sendMessage(String message);
    void sendMessages(List<String> messages);
    void sendMessageLocale(String key, Object... args);
    String getName();

    String getChatName();

    boolean hasPermission(String permission);
    boolean isHuman();
}
