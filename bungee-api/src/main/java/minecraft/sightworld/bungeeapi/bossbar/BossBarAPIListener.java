package minecraft.sightworld.bungeeapi.bossbar;

import lombok.AllArgsConstructor;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class BossBarAPIListener implements Listener {

    private final BossBarAPI api;

    @EventHandler(priority=-64)
    public void onDisconnect(@NotNull PlayerDisconnectEvent e) {
        api.getCachedBossBars().remove(e.getPlayer());
    }

}