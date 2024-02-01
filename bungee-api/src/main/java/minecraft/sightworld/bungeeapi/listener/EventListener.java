package minecraft.sightworld.bungeeapi.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public abstract class EventListener<T extends Plugin> implements Listener {

    protected final T plugin;

    protected EventListener(T plugin) {
        this.plugin = plugin;
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    public void unregister() {
        ProxyServer.getInstance().getPluginManager().unregisterListener(this);
    }
}
