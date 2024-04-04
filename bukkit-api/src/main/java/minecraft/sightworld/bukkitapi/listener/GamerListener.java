package minecraft.sightworld.bukkitapi.listener;


import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitGamer;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitServer;
import minecraft.sightworld.bukkitapi.gamer.event.AsyncGamerJoinEvent;
import minecraft.sightworld.bukkitapi.gamer.event.AsyncGamerLoadSectionEvent;
import minecraft.sightworld.bukkitapi.gamer.event.AsyncGamerPreLoginEvent;
import minecraft.sightworld.bukkitapi.gamer.event.AsyncGamerQuitEvent;
import minecraft.sightworld.bukkitapi.gamer.impl.BukkitGamerImpl;
import minecraft.sightworld.bukkitapi.packet.team.TeamManager;
import minecraft.sightworld.bukkitapi.scheduler.BukkitScheduler;
import minecraft.sightworld.defaultlib.gamer.GamerAPI;
import minecraft.sightworld.defaultlib.gamer.section.Section;
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
    public GamerListener(SightWorld javaPlugin) {
        super(javaPlugin);
    }

    Map<String, BukkitGamerImpl> gamers = new ConcurrentHashMap<>();
    BukkitServer server = SightWorld.getGamerManager().getServer();

    ImmutableSet<Class<? extends Section>> loadedSections = ImmutableSet.of(

    );

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

        GamerAPI.removeOfflinePlayer(name);
        BukkitGamer gamer = null;
        try {
            gamer = new BukkitGamerImpl(e); //создаем геймера
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (gamer == null) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cОшибка при загрузке данных");
            return;
        }

        BukkitScheduler.callEvent(new AsyncGamerPreLoginEvent(gamer, e));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLoadGamer(final @NotNull AsyncGamerPreLoginEvent e) {
        val gamer = (BukkitGamerImpl) e.getGamer();

        server.sendMessage("§fДанные игрока §b" + gamer.getName() + "§r загружены за (§b"
                + (System.currentTimeMillis() - gamer.getStart()) + "ms§f)");

        gamers.put(gamer.getName().toLowerCase(), gamer);
    }

    @EventHandler
    public void onLoadSection(final @NotNull AsyncGamerLoadSectionEvent e) {
        e.getSections().addAll(loadedSections); //инициализируем дополнительные секции которые должны быть загружены
    }

    @EventHandler
    public void onGamerJoin(PlayerJoinEvent e) {
        val gamer = BukkitGamer.getGamer(e.getPlayer());
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

        if (gamer.isRedAdmin()) {
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
