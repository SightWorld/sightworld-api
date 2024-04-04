package minecraft.sightworld.bungeeapi.gamer.entity.impl;

import lombok.NonNull;
import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.gamer.entity.BungeeEntity;
import minecraft.sightworld.defaultlib.localization.Language;
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
    public void sendMessageLocale(String key, Object... args) {
        val textComponent = new TextComponent(SightWorld.getLocalizationService().get(Language.RUSSIAN, key, args));
        sendMessage(textComponent);
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
