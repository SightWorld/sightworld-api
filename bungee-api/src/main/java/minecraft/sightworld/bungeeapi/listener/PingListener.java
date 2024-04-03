package minecraft.sightworld.bungeeapi.listener;

import com.google.common.base.Joiner;
import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.scheduler.BungeeScheduler;
import minecraft.sightworld.bungeeapi.util.HexUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Arrays;
import java.util.UUID;

public class PingListener extends EventListener<SightWorld> {
    public PingListener(SightWorld plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProxyPing(final ProxyPingEvent event) { // говно
        val response = event.getResponse();
        if (response == null || event instanceof Cancellable && ((Cancellable) event).isCancelled())
            return;

        val connection = event.getConnection();
        val protocol = response.getVersion();
        val players = response.getPlayers();

        event.registerIntent(plugin);
        BungeeScheduler.submitAsync(() -> {
            String motdHex = Joiner.on("\n")
                    .join(plugin.getConfig().getStringList("motd_hex"));
            String motd = Joiner.on("\n")
                    .join(plugin.getConfig().getStringList("motd"));

            if (plugin.getWhitelistManager().isEnable()) {
                val whitelist_hover = plugin.getWhitelistManager().getHoverText();
                motd = plugin.getWhitelistManager().getMotd();
                motdHex = plugin.getWhitelistManager().getMotd();

                players.setSample(Arrays.stream(whitelist_hover.split("\n"))
                        .map(line -> new ServerPing.PlayerInfo(line, UUID.fromString("00000000-0000-0000-0000-000000000000").toString()))
                        .toArray(ServerPing.PlayerInfo[]::new));
            }

            int ver = connection.getVersion();

            if (ver >= 47 && ver <= 719) {
                response.setDescriptionComponent(new TextComponent(motd));
            } else {
                response.setDescription(HexUtil.fromHexString(motdHex));
            }

            int online = ProxyServer.getInstance().getOnlineCount();

            if (plugin.getConfig().getBoolean("booster.enable"))
                players.setOnline(online * plugin.getConfig().getInt("booster.boost"));
            else
                players.setOnline(online);

            int maxPlayers = plugin.getConfig().getBoolean("justOneMore") ? online + 1 : 1;
            players.setMax(maxPlayers);

            protocol.setProtocol(Math.max(47, ver));
            response.setVersion(protocol);

            event.completeIntent(plugin);
        });
    }
}
