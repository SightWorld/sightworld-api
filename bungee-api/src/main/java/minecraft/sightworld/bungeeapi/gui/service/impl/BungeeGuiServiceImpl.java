package minecraft.sightworld.bungeeapi.gui.service.impl;

import lombok.val;
import minecraft.sightworld.bungeeapi.gui.service.BungeeGuiService;
import minecraft.sightworld.defaultlib.gui.BungeeGuiItem;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BungeeGuiServiceImpl implements BungeeGuiService {

    private final Map<String, BungeeGuiItem> itemMap = new HashMap<>();

    @Override
    public Map<String, BungeeGuiItem> items() {
        return itemMap;
    }

    @Override
    public void addItem(BungeeGuiItem bungeeGuiItem) {
        itemMap.put(bungeeGuiItem.getUuid(), bungeeGuiItem);
    }

    @Override
    public BungeeGuiItem getItem(String uuid) {
        return itemMap.get(uuid);
    }

    @Override
    public void clearItems(ProxiedPlayer player) {
        List<String> deleteKeys = new ArrayList<>();
        for (val key : itemMap.keySet()) {
            if (key.split("-")[0].equalsIgnoreCase(player.getName())) {
                deleteKeys.add(key);
            }
        }
        for (val key : deleteKeys) {
            itemMap.remove(key);
        }
    }
}
