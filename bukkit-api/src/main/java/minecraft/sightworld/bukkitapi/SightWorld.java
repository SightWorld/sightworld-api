package minecraft.sightworld.bukkitapi;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import minecraft.sightworld.bukkitapi.commands.impl.ApiCommand;
import minecraft.sightworld.bukkitapi.commands.impl.CrashClientCommand;
import minecraft.sightworld.bukkitapi.gui.*;
import minecraft.sightworld.bukkitapi.listener.GamerListener;
import minecraft.sightworld.bukkitapi.listener.message.BungeeGuiListener;
import minecraft.sightworld.bukkitapi.listener.message.BungeeSoundListener;
import minecraft.sightworld.bukkitapi.scoreboard.listener.BaseScoreboardListener;
import minecraft.sightworld.bukkitapi.user.service.UserServiceImpl;
import minecraft.sightworld.defaultlib.database.Database;
import minecraft.sightworld.defaultlib.localization.LocalizationService;
import minecraft.sightworld.defaultlib.localization.impl.LocalizationServiceImpl;
import minecraft.sightworld.defaultlib.messaging.MessageService;
import minecraft.sightworld.defaultlib.messaging.impl.MessageServiceImpl;
import minecraft.sightworld.defaultlib.redis.DefaultRedisFactory;
import minecraft.sightworld.defaultlib.redis.RedisFactory;
import minecraft.sightworld.defaultlib.user.service.UserService;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.api.RedissonClient;

import java.sql.SQLException;
import java.util.stream.Stream;

public class SightWorld extends JavaPlugin {

    @Getter
    private static SightWorld instance;

    @Getter
    private static Database database;

    @Getter
    private static ProtocolManager protocolManager;

    @Getter
    private static LocalizationService localizationService;

    @Getter
    private static UserService<Player> userService;

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        loadDatabase();
        loadLocalization();

        userService = new UserServiceImpl(database.getUserDao());

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

    public void loadLocalization() {
        localizationService = new LocalizationServiceImpl();

        Stream.of(localizationService.getFiles())
                .parallel()
                .forEach(file -> localizationService.download(file, "https://raw.githubusercontent.com/SightWorld/localization/main/lang/" + file +".json"));
    }
    private void registerCommands() {
        new ApiCommand(this);
        new CrashClientCommand(this);
    }

    private void registerListeners() {
        new GamerListener(this, userService);
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

    public void loadDatabase() {
        database = new Database();
        try {
            database.load();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
