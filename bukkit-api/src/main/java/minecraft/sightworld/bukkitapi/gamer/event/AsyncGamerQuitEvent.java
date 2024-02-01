package minecraft.sightworld.bukkitapi.gamer.event;

import minecraft.sightworld.bukkitapi.gamer.entity.BukkitGamer;

public class AsyncGamerQuitEvent extends GamerEvent {
    public AsyncGamerQuitEvent(BukkitGamer gamer) {
        super(gamer, true);
    }
}