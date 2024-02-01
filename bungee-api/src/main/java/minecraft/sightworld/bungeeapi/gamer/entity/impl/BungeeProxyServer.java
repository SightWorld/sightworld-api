package minecraft.sightworld.bungeeapi.gamer.entity.impl;

import lombok.NonNull;
import minecraft.sightworld.bungeeapi.gamer.entity.BungeeEntity;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class BungeeProxyServer implements BungeeEntity {
    String name = "proxy";

    @Override
    public void sendMessage(String msg) {
        sendMessage(new TextComponent(msg));
    }

    @Override
    public void sendMessage(@NonNull BaseComponent component) {
        ProxyServer.getInstance().getConsole().sendMessage(component);
    }

    @Override
    public void sendMessage(BaseComponent[] components) {
        sendMessage(new TextComponent(components));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getChatName() {
        return name;
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

}
