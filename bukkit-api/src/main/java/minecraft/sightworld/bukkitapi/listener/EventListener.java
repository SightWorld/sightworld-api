package minecraft.sightworld.bukkitapi.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class EventListener<T extends JavaPlugin> implements Listener {

    public final T javaPlugin;

    public EventListener(final T javaPlugin) {
        this.javaPlugin = javaPlugin;
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    public void unregisterListener() {
        HandlerList.unregisterAll(this);
    }
}