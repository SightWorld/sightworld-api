package minecraft.sightworld.bukkitapi.gamer.event;

import lombok.Getter;
import lombok.Setter;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitGamer;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public class AsyncGamerJoinEvent extends GamerEvent implements Cancellable {

    private boolean cancelled;

    public AsyncGamerJoinEvent(BukkitGamer gamer) {
        super(gamer, true);
    }
}