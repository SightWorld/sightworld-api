package minecraft.sightworld.bukkitapi.gamer.event;

import lombok.Getter;
import minecraft.sightworld.bukkitapi.event.SightEvent;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitGamer;

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
