package minecraft.sightworld.bukkitapi.gamer.event;

import lombok.Getter;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitGamer;
import minecraft.sightworld.bukkitapi.event.SightEvent;

@Getter
public abstract class GamerEvent extends SightEvent {

    private final BukkitGamer gamer;

    protected GamerEvent(BukkitGamer gamer) {
        this(gamer, false);
    }


    protected GamerEvent(BukkitGamer gamer, boolean async) {
        super(async);
        this.gamer = gamer;
    }
}
