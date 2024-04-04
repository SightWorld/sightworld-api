package minecraft.sightworld.bungeeapi.gamer.entity;

import net.md_5.bungee.api.chat.BaseComponent;

public interface BungeeEntity {

    void sendMessage(String msg);
    void sendMessage(BaseComponent component);
    void sendMessage(BaseComponent[] components);
    void sendMessageLocale(String key, Object... args);
    String getName();

    String getChatName();

    boolean isHuman();

    boolean hasPermission(String permission);
}
