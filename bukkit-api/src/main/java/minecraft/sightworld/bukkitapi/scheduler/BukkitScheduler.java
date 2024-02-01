package minecraft.sightworld.bukkitapi.scheduler;

import lombok.experimental.UtilityClass;
import minecraft.sightworld.bukkitapi.SightWorld;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

@UtilityClass
public class BukkitScheduler {

    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public  void runTaskLater(long delay, Runnable runnable){
        Bukkit.getScheduler().runTaskLater(SightWorld.getInstance(), runnable, delay);
    }

    public void runTaskLaterAsync(long delay, Runnable runnable){
        Bukkit.getScheduler().runTaskLaterAsynchronously(SightWorld.getInstance(), runnable, delay);
    }

    public void runTask(Runnable runnable){
        Bukkit.getScheduler().runTask(SightWorld.getInstance(), runnable);
    }

    public void runTaskAsync(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(SightWorld.getInstance(), runnable);
    }
}
