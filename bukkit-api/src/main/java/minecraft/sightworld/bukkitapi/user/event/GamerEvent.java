package minecraft.sightworld.bukkitapi.user.event;

import lombok.Getter;
import minecraft.sightworld.bukkitapi.event.SightEvent;
import minecraft.sightworld.bukkitapi.user.BukkitUser;

@Getter
public abstract class GamerEvent extends SightEvent {

    private final BukkitUser user;

    protected GamerEvent(BukkitUser gamer) {
        this(gamer, false);
    }


    protected GamerEvent(BukkitUser gamer, boolean async) {
        super(async);
        this.user = gamer;
    }
}
