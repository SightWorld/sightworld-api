package minecraft.sightworld.bukkitapi;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import minecraft.sightworld.bukkitapi.commands.impl.ApiCommand;
import minecraft.sightworld.bukkitapi.commands.impl.CrashClientCommand;
import minecraft.sightworld.bukkitapi.gamer.GamerManager;
import minecraft.sightworld.bukkitapi.gui.*;
import minecraft.sightworld.bukkitapi.listener.GamerListener;
import minecraft.sightworld.bukkitapi.listener.message.BungeeGuiListener;
import minecraft.sightworld.bukkitapi.listener.message.BungeeSoundListener;
import minecraft.sightworld.bukkitapi.scoreboard.listener.BaseScoreboardListener;
import minecraft.sightworld.defaultlib.messaging.MessageService;
import minecraft.sightworld.defaultlib.messaging.impl.MessageServiceImpl;
import minecraft.sightworld.defaultlib.redis.DefaultRedisFactory;
import minecraft.sightworld.defaultlib.redis.RedisFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.api.RedissonClient;

public class SightWorld extends JavaPlugin {

    @Getter
    private static SightWorld instance;

    @Getter
    private static GamerManager gamerManager;

    @Getter
    private static ProtocolManager protocolManager;

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        instance = this;
        gamerManager = new GamerManager(this);
        registerGuiService();
        registerMessageService();
        registerListeners();
        registerCommands();
        setGameRule();

        getLogger().info("SightAPI [BUKKIT] has enabled! Version - 1.0.1");
    }

    @Override
    public void onDisable() {
        getLogger().info("SightAPI [BUKKIT] has disabled! Version - 1.0.1");
    }

    private void setGameRule() {
        getServer().getWorlds().forEach(world -> {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, getConfig().getBoolean("announce-advancements"));
        });
    }

    private void registerCommands() {
        new ApiCommand(this);
        new CrashClientCommand(this);
    }

    private void registerListeners() {
        new GamerListener(this);
        new BaseScoreboardListener(this);
    }

    private void registerGuiService() {
        GuiService guiService = new GuiServiceImpl();
        Gui.init(this, guiService);
        PagedGui.init(this, guiService);
        getServer().getPluginManager().registerEvents(new GuiListener(guiService), this);
    }

    private MessageService registerMessageService() {
        RedisFactory redisFactory = new DefaultRedisFactory();
        RedissonClient redissonClient = redisFactory.create();

        MessageServiceImpl messagingService = new MessageServiceImpl(redissonClient);
        messagingService.addListener(new BungeeGuiListener(messagingService), "gui");
        messagingService.addListener(new BungeeSoundListener(), "sound");

        return messagingService;

    }
}
