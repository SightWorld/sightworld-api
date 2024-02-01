package minecraft.sightworld.bukkitapi.gamer.event;

import lombok.Getter;
import lombok.Setter;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitGamer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

@Getter
public class AsyncGamerPreLoginEvent extends GamerEvent implements Cancellable {

    private final AsyncPlayerPreLoginEvent event;

    @Setter
    private boolean cancelled;

    public AsyncGamerPreLoginEvent(BukkitGamer gamer, AsyncPlayerPreLoginEvent event) {
        super(gamer, true);
        this.event = event;
    }
}