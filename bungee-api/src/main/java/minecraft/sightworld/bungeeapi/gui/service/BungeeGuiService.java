package minecraft.sightworld.bungeeapi.gui.service;

import minecraft.sightworld.defaultlib.gui.BungeeGuiItem;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;

public interface BungeeGuiService {

    Map<String, BungeeGuiItem> items();

    void addItem(BungeeGuiItem bungeeGuiItem);

    BungeeGuiItem getItem(String uuid);

    void clearItems(ProxiedPlayer player);
}
