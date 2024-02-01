package minecraft.sightworld.bungeeapi.gamer.entity;

import lombok.Getter;
import minecraft.sightworld.bungeeapi.gamer.BungeeGamer;
import minecraft.sightworld.bungeeapi.gamer.entity.BungeeEntity;
import minecraft.sightworld.bungeeapi.gamer.entity.impl.BungeeProxyServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BungeeEntityManager {

    static @Getter
    final BungeeProxyServer bungee = new BungeeProxyServer();

    public static @Nullable BungeeEntity getEntity(@NotNull CommandSender sender) {


        if (sender instanceof ProxiedPlayer) {
            return (BungeeEntity) BungeeGamer.getGamer((ProxiedPlayer) sender);
        } else if (sender == ProxyServer.getInstance().getConsole()) {
            return getBungee();
        } else {
            return null;
        }
    }
}
