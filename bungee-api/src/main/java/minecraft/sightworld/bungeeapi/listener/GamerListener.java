package minecraft.sightworld.bungeeapi.listener;

import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.gamer.entity.BungeeEntityManager;
import minecraft.sightworld.bungeeapi.gamer.event.AsyncGamerLoginEvent;
import minecraft.sightworld.bungeeapi.gamer.event.AsyncGamerQuitEvent;
import minecraft.sightworld.bungeeapi.scheduler.BungeeScheduler;
import minecraft.sightworld.bungeeapi.user.ProxiedUser;
import minecraft.sightworld.defaultlib.user.service.UserService;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import static net.md_5.bungee.event.EventPriority.LOW;
import static net.md_5.bungee.event.EventPriority.LOWEST;

public class GamerListener extends EventListener<SightWorld> {

    private final SightWorld plugin;

    private final UserService<ProxiedPlayer> userService;

    public GamerListener(SightWorld plugin, UserService<ProxiedPlayer> userService) {
        super(plugin);
        this.plugin = plugin;
        this.userService = userService;
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


        if (checkWl(event)) {
            event.registerIntent(plugin);
            BungeeScheduler.submitAsync(() -> {
//                Callback<DPreLoginEvent> callback = (result, throwable) -> {
//                    // nothing :(
//                };

            //    BungeeUtil.callEventAsync(new DPreLoginEvent(name, connection, callback));
                event.completeIntent(plugin);
            });
        }
    }

    private boolean checkWl(final @NotNull PreLoginEvent e) {
        val connection = e.getConnection();
        val name = connection.getName();

        if (plugin.getWhitelistManager().isEnable()
                && !plugin.getWhitelistManager().getPlayerNames().contains(name.toLowerCase())) {
            e.setCancelReason(new TextComponent(plugin.getWhitelistManager().getKickMessage()));
            e.setCancelled(true);

            BungeeEntityManager.getBungee().sendMessage("§fИгрок §b" + name +
                    "§f попытался зайти, но был исключен т.к включены тех. работы");
            return false;
        }

        return true;
    }

    // Инициализация игрока в API
    @EventHandler(priority = LOWEST)
    public void onLogin(final @NotNull LoginEvent event) {
        val connection = event.getConnection();
        val name = connection.getName();

        userService.removeOfflineUser(name);

        event.registerIntent(plugin);
        BungeeScheduler.submitAsync(() -> {
            val gamer = userService.getOrCreate(name);

            final Callback<AsyncGamerLoginEvent> callback = (asyncGamerLoginEvent, throwable) -> {

            };

            BungeeScheduler.callEventAsync(new AsyncGamerLoginEvent((ProxiedUser) gamer, connection, callback));
            System.out.println("Игрок " + gamer.getName() + " инициализировался в SightWorld API!");
            event.completeIntent(plugin);
        });
    }

    @EventHandler(priority = LOWEST)
    public void onLoadGamer(final @NotNull AsyncGamerLoginEvent event) {
        event.registerIntent(plugin);
        BungeeScheduler.submitAsync(() -> {
            val user = event.getUser();


            ProxyServer.getInstance().getLogger().info("§fДанные игрока §b" + user.getName() + "§f загружены §f)");

            userService.addOnlineUser(user);
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
