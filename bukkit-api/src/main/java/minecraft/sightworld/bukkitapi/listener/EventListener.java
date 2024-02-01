package minecraft.sightworld.bukkitapi.listener;

import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.gamer.GamerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class EventListener<T extends JavaPlugin> implements Listener {

    protected static final GamerManager GAMER_MANAGER = SightWorld.getGamerManager();

    protected final T javaPlugin;

    protected EventListener(final T javaPlugin) {
        this.javaPlugin = javaPlugin;
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    public void unregisterListener() {
        HandlerList.unregisterAll(this);
    }
}