package minecraft.sightworld.bungeeapi.scheduler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import minecraft.sightworld.bungeeapi.SightWorld;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BungeeScheduler { // TODO: Разобрать говнокод

    static PluginManager PLUGIN_MANAGER = ProxyServer.getInstance().getPluginManager();
    static TaskScheduler SCHEDULER      = ProxyServer.getInstance().getScheduler();

    /**
     * Выполнить что то в асинк потоке банжи
     * @param runnable ранбл
     */
    public static void submitAsync(@NotNull final Runnable runnable) {
        SCHEDULER.runAsync(SightWorld.getInstance(), runnable);
    }

    public void runAsyncLater(final long delay, @NotNull final Runnable runnable) {
        SCHEDULER.schedule(SightWorld.getInstance(), runnable, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Вызвать ивент
     * @param event ивент который надо
     */
    public void callEvent(@NotNull final Event event) {
        PLUGIN_MANAGER.callEvent(event);
    }

    /**
     * Вызвать ивент в асинк потоке
     * @param event ивент который надо
     */
    public static void callEventAsync(@NotNull final Event event) {
        submitAsync(() -> callEvent(event));
    }

    public ScheduledTask runTask(Runnable task, long delay, long period, TimeUnit timeUnit) {
        return SCHEDULER.schedule(SightWorld.getInstance(), task, delay, period, timeUnit);
    }

    public ScheduledTask runTask(Runnable task, long delay, TimeUnit timeUnit) {
        return SCHEDULER.schedule(SightWorld.getInstance(), task, delay, timeUnit);
    }

    public void cancelTask(final int taskID) {
        SCHEDULER.cancel(taskID);
    }

}
