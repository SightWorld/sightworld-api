package minecraft.sightworld.bukkitapi.user.event;

import lombok.Getter;
import lombok.Setter;
import minecraft.sightworld.bukkitapi.user.BukkitUser;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

@Getter
public class AsyncGamerPreLoginEvent extends GamerEvent implements Cancellable {

    private final AsyncPlayerPreLoginEvent event;

    @Setter
    private boolean cancelled;

    public AsyncGamerPreLoginEvent(BukkitUser user, AsyncPlayerPreLoginEvent event) {
        super(user, true);
        this.event = event;
    }
}