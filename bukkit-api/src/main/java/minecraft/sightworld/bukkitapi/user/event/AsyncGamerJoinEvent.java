package minecraft.sightworld.bukkitapi.user.event;

import lombok.Getter;
import lombok.Setter;
import minecraft.sightworld.bukkitapi.user.BukkitUser;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public class AsyncGamerJoinEvent extends GamerEvent implements Cancellable {

    private boolean cancelled;

    public AsyncGamerJoinEvent(BukkitUser gamer) {
        super(gamer, true);
    }
}