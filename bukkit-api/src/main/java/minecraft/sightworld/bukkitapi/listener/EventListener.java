package minecraft.sightworld.bukkitapi.listener;

import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.gamer.GamerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class EventListener<T extends JavaPlugin> implements Listener {

    public static final GamerManager GAMER_MANAGER = SightWorld.getGamerManager();

    public final T javaPlugin;

    public EventListener(final T javaPlugin) {
        this.javaPlugin = javaPlugin;
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    public void unregisterListener() {
        HandlerList.unregisterAll(this);
    }
}