package minecraft.sightworld.bukkitapi.listener;


import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitGamer;
import minecraft.sightworld.bukkitapi.gamer.impl.BukkitGamerImpl;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitServer;
import minecraft.sightworld.bukkitapi.gamer.event.AsyncGamerJoinEvent;
import minecraft.sightworld.bukkitapi.gamer.event.AsyncGamerLoadSectionEvent;
import minecraft.sightworld.bukkitapi.gamer.event.AsyncGamerPreLoginEvent;
import minecraft.sightworld.bukkitapi.gamer.event.AsyncGamerQuitEvent;
import minecraft.sightworld.bukkitapi.packet.team.WrapperPlayServerScoreboardTeam;
import minecraft.sightworld.bukkitapi.scheduler.BukkitScheduler;
import minecraft.sightworld.bukkitapi.scoreboard.BaseScoreboardBuilder;
import minecraft.sightworld.bukkitapi.scoreboard.BaseScoreboardScope;
import minecraft.sightworld.bukkitapi.scoreboard.ScoreboardAPI;
import minecraft.sightworld.bukkitapi.scoreboard.animation.ScoreboardDisplayCustomAnimation;
import minecraft.sightworld.bukkitapi.scoreboard.animation.ScoreboardDisplayFlickAnimation;
import minecraft.sightworld.defaultlib.gamer.GamerAPI;
import minecraft.sightworld.defaultlib.gamer.section.Section;
import minecraft.sightworld.defaultlib.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        e.setSections(loadedSections); //инициализируем дополнительные секции которые должны быть загружены
    }

    private WrapperPlayServerScoreboardTeam getTeam(int i, BukkitGamer gamer) {
        val team = new WrapperPlayServerScoreboardTeam();
        val prefix = gamer.getPrefix();
        val tag = " " + gamer.getTag();

        team.setName("000" + gamer.getName());
        team.setMode(i);
        team.setPrefix(WrappedChatComponent.fromText(prefix != null ? StringUtils.fixLength(16, prefix) : ""));
        team.setSuffix(WrappedChatComponent.fromText(StringUtils.fixLength(16, tag)));
        team.setNameTagVisibility("ALWAYS");
        team.setColor(prefix != null ? ChatColor.getByChar(prefix.replace(" ", "").charAt(1)) : ChatColor.WHITE);
        team.setPackOptionData(1);
        team.getPlayers().add(gamer.getName());

        return team;
    }

    private void sendScoreboard(BukkitGamer gamer) {

        BaseScoreboardBuilder scoreboardBuilder = ScoreboardAPI.newScoreboardBuilder();
        scoreboardBuilder.scoreboardScope(BaseScoreboardScope.PROTOTYPE);
        ScoreboardDisplayFlickAnimation displayFlickAnimation = new ScoreboardDisplayFlickAnimation();

        displayFlickAnimation.addColor(ChatColor.RED);
        displayFlickAnimation.addColor(ChatColor.GOLD);
        displayFlickAnimation.addColor(ChatColor.DARK_RED);
        displayFlickAnimation.addColor(ChatColor.BLUE);

        displayFlickAnimation.addTextToAnimation("§lDWYUR PIDORAS");

        ScoreboardDisplayCustomAnimation animation = new ScoreboardDisplayCustomAnimation();


        scoreboardBuilder.scoreboardDisplay(displayFlickAnimation);

        scoreboardBuilder.scoreboardLine(4, "§fНик: §c...");
        scoreboardBuilder.scoreboardLine(3, "§fГруппа: §c...");
        scoreboardBuilder.scoreboardLine(2, "");
        scoreboardBuilder.scoreboardLine(1, "§evikypat.lastcraft.net");

        scoreboardBuilder.scoreboardUpdater((baseScoreboard, player1) -> {

            baseScoreboard.updateScoreboardLine(4, player1, "§fНик: §7" + player1.getName());
            baseScoreboard.updateScoreboardLine(3, player1, "§fГруппа: §7" + gamer.getPrefix());
        }, 20);

        scoreboardBuilder.build().setScoreboardToPlayer(gamer.getPlayer());
    }

    @EventHandler
    public void onGamerJoin(AsyncGamerJoinEvent e) {
        val gamer = e.getGamer();

        Bukkit.getOnlinePlayers().forEach(player -> {
            getTeam(1, gamer).sendPacket(player);
            getTeam(0, gamer).sendPacket(player);
        });

        sendScoreboard(gamer);
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
