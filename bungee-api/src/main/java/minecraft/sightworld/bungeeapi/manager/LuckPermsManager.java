package minecraft.sightworld.bungeeapi.manager;

import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.gamer.BungeeGamer;
import minecraft.sightworld.defaultlib.gamer.GamerAPI;
import minecraft.sightworld.defaultlib.gamer.OfflineGamer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.context.ContextUpdateEvent;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeClearEvent;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Objects;

public class LuckPermsManager {

    private static final LuckPerms api = LuckPermsProvider.get();

    private static User playerToUser(String name) {
        return api.getUserManager().getUser(name);
    }

    public static String getPrefix(String name) {
        return playerToUser(name).getCachedData().getMetaData().getPrefix();
    }

    public static void registerEvents(SightWorld plugin) {
        EventBus eventBus = api.getEventBus();

        eventBus.subscribe(plugin, NodeMutateEvent.class, e -> {
            if (e.isUser()) {
                User user = (User) e.getTarget();
                String prefix = user.getCachedData().getMetaData().getPrefix();

                syncPrefix(user.getUsername(), Objects.requireNonNullElse(prefix, "§7"));
            }
        });
//        eventBus.subscribe(plugin, NodeAddEvent.class, e -> {
//            if (e.isUser() && e.getNode().getType() == NodeType.PREFIX) {
//                User user = (User) e.getTarget();
//                String name = user.getUsername();
//                String prefix = user.getCachedData().getMetaData().getPrefix();
//
//                syncPrefix(name, prefix);
//            }
//        });
//
//        eventBus.subscribe(plugin, NodeRemoveEvent.class, e -> {
//            if (e.isUser() && e.getNode().getType() == NodeType.PREFIX) {
//                User user = (User) e.getTarget();
//                String name = user.getUsername();
//
//                syncPrefix(name, "§7");
//            }
//        });
    }

    private static void syncPrefix(String name, String prefix) {
        BungeeGamer gamer = BungeeGamer.getGamer(name);
        if (gamer != null && gamer.isOnline()) {
            gamer.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix), true);
        } else {
            OfflineGamer offlineGamer = GamerAPI.getOfflinePlayer(name);
            if (offlineGamer != null) {
                offlineGamer.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix), true);
            }
        }
    }
}
