package minecraft.sightworld.bukkitapi.user.event;


import minecraft.sightworld.bukkitapi.user.BukkitUser;

public class AsyncGamerQuitEvent extends GamerEvent {
    public AsyncGamerQuitEvent(BukkitUser gamer) {
        super(gamer, true);
    }
}