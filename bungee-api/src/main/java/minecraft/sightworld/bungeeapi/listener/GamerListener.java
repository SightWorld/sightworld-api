package minecraft.sightworld.bungeeapi.listener;

import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.gamer.BungeeGamer;
import minecraft.sightworld.bungeeapi.gamer.impl.BungeeGamerImpl;
import minecraft.sightworld.bungeeapi.gamer.event.AsyncGamerLoginEvent;
import minecraft.sightworld.bungeeapi.gamer.event.AsyncGamerQuitEvent;
import minecraft.sightworld.bungeeapi.scheduler.BungeeScheduler;
import minecraft.sightworld.defaultlib.gamer.GamerAPI;
import minecraft.sightworld.defaultlib.gamer.section.JoinMessageSection;
import minecraft.sightworld.defaultlib.gamer.section.Section;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static net.md_5.bungee.event.EventPriority.LOW;
import static net.md_5.bungee.event.EventPriority.LOWEST;

public class GamerListener extends EventListener<SightWorld> {

    Object2ObjectMap<String, BungeeGamerImpl> gamers2 = new Object2ObjectOpenHashMap<>();

    protected Set<Class<? extends Section>> initSections() { // Секции, которые надо загрузить после инициализации плеера
        return Set.of(JoinMessageSection.class);
    }

    private final SightWorld plugin;

    public GamerListener(SightWorld plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = LOWEST)
    public void onPreLogin(final @NotNull PreLoginEvent event) {
        if (event.isCancelled())
            return;

        val connection = event.getConnection();
        val name = connection.getName();

        val regex = "^[a-zA-Z0-9_]+$";
        if (name.length() < 3 || name.length() > 16) {
            event.setCancelled(true);
            event.setCancelReason(new TextComponent(
                    """
                    §d§lSightWorld\s

                    §fДлина никнейма должна быть не меньше 3 символов и не больше 16
                    """
            ));
        } else if (name.trim().contains(" ") || !name.matches(regex)) {
            event.setCancelled(true);
            event.setCancelReason(new TextComponent(
                    """
                    §d§lSightWorld\s

                    §fВ нике есть недопустимые символы!
                    §7Разрешено использовать только цифры и английские буквы!
                    """
            ));
        } else if (name.equalsIgnoreCase("Kambet")) {
            event.setCancelled(true);
            event.setCancelReason(new TextComponent(
                    """
                    §d§lSightWorld\s

                    §cКамбету запрещен вход на данный сервер!
                    """
            ));
        }

    }

    // Инициализация игрока в API
    @EventHandler(priority = LOWEST)
    public void onLogin(final @NotNull LoginEvent event) {
        val connection = event.getConnection();
        val name = connection.getName();

        GamerAPI.removeOfflinePlayer(name);

        event.registerIntent(plugin);
        BungeeScheduler.submitAsync(() -> {
            val gamer = BungeeGamer.getOrCreate(name, connection.getAddress().getAddress());

            final Callback<AsyncGamerLoginEvent> callback = (asyncGamerLoginEvent, throwable) -> {

            };

            BungeeScheduler.callEventAsync(new AsyncGamerLoginEvent(gamer, connection, callback));
            System.out.println("Игрок " + gamer.getName() + " инициализировался в SightWorld API!");
            event.completeIntent(plugin);
        });
    }

    @EventHandler(priority = LOWEST)
    public void onLoadGamer(final @NotNull AsyncGamerLoginEvent event) {
        event.registerIntent(plugin);
        BungeeScheduler.submitAsync(() -> {
            val gamer = (BungeeGamerImpl) event.getGamer();

            if (initSections() != null) {
                initSections().forEach(gamer::initSection);
            }

            ProxyServer.getInstance().getLogger().info("§fДанные игрока §b" + gamer.getName() + "§f загружены за (§a"
                    + (System.currentTimeMillis() - gamer.getStart()) + "ms§f)");

            gamers2.put(gamer.getName().toLowerCase(), gamer);
            GamerAPI.addGamer(gamer);
            event.completeIntent(plugin);
        });
    }

    // Окончательная проверка и инициализация геймера
    @EventHandler(priority = LOW)
    public void onLoginGamer(final @NotNull AsyncGamerLoginEvent event) {
        event.registerIntent(plugin);
        BungeeScheduler.submitAsync(() -> {
            val connection = event.getConnection();
            val name = connection.getName();

            val bungeeGamer = gamers2.remove(name.toLowerCase());
            if (bungeeGamer == null) {
                event.setCancelReason(new TextComponent(
                        """
                        §d§lSightWorld

                        §fОшибка при загрузке данных
                        §fСообщите нам: §dvk.com/sightworld
                        """));
                event.setCancelled(true);

            }

            event.completeIntent(plugin);

        });
    }

    @EventHandler
    public void onPlayerDisconnect(final @NotNull PlayerDisconnectEvent event) {
        val player = event.getPlayer();
        val connection = player.getPendingConnection();

        val gamer = BungeeGamer.getGamer(player.getName());
        if (gamer != null) {
            final Callback<AsyncGamerQuitEvent> callback = (result, throwable) -> {
                gamer.disconnect(player.getServer());
            };

            BungeeScheduler.callEventAsync(new AsyncGamerQuitEvent(gamer, connection, callback));

        }
    }

    @EventHandler(priority = LOW) // TODO: Инициализация скина после захода на баккит сервер
    public void onServerConnect(final @NotNull ServerConnectedEvent event) {
        val player = event.getPlayer();
        val connection = player.getPendingConnection();
        val target = event.getServer();
        val gamer = BungeeGamer.getGamer(player.getName());
         BungeeScheduler.submitAsync(()-> {

        });
    }


}
