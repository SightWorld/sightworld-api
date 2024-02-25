package minecraft.sightworld.bungeeapi.bossbar;

import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.BossBar;

import java.net.ProxySelector;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class BossBarAPI {

    private final Map<ProxiedPlayer, UUID> cachedBossBars = new ConcurrentHashMap<>();

    public BossBarAPI() {
        ProxyServer.getInstance().getPluginManager().registerListener(
                SightWorld.getInstance(), new BossBarAPIListener(this));
    }

    public void sendBossBar(ProxiedPlayer player, @NonNull String title, BossBarStyle style,
                            BossBarColor color, float health) {

        if (player == null || !player.isConnected()) {
            return;
        }

        if (health > 1.0f || health < 0.0f) {
            throw new IllegalArgumentException("health > 1 or < 0");
        }

        if (player.getPendingConnection().getVersion() <= 66) {
            return;
        }

        UUID uuid = cachedBossBars.get(player);

        if (uuid == null) {
            AtomicInteger atomicInteger = new AtomicInteger(1);
            uuid = UUID.nameUUIDFromBytes(("BBB:" + atomicInteger.getAndIncrement())
                    .getBytes(StandardCharsets.UTF_8));

            cachedBossBars.put(player, uuid);
        }

        val bossBar = new BossBar(uuid, 0);
        bossBar.setTitle(new TextComponent(title));
        bossBar.setDivision(style.getId());
        bossBar.setColor(color.getId());
        bossBar.setHealth(health);

        player.unsafe().sendPacket(bossBar);
    }

    public void removeBossBar(@NonNull ProxiedPlayer player) {
        if (player.getPendingConnection().getVersion() <= 66) {
            return;
        }

        val uuid = cachedBossBars.get(player);
        if (uuid == null)
            return;

        if (!player.isConnected())
            return;

        player.unsafe().sendPacket(new BossBar(uuid, 1));
        this.cachedBossBars.remove(player);
    }

}