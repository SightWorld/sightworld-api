package minecraft.sightworld.bungeeapi;

import lombok.Getter;
import lombok.val;
import minecraft.sightworld.bungeeapi.announce.AnnounceManager;
import minecraft.sightworld.bungeeapi.command.impl.BungeeApiCommand;
import minecraft.sightworld.bungeeapi.command.impl.BungeeWhitelistCommand;
import minecraft.sightworld.bungeeapi.gui.acceptor.AcceptorListener;
import minecraft.sightworld.bungeeapi.gui.service.BungeeGuiService;
import minecraft.sightworld.bungeeapi.gui.service.impl.BungeeGuiServiceImpl;
import minecraft.sightworld.bungeeapi.listener.GamerListener;
import minecraft.sightworld.bungeeapi.listener.PingListener;
import minecraft.sightworld.bungeeapi.scheduler.SchedulerManager;
import minecraft.sightworld.bungeeapi.tab.TabManager;
import minecraft.sightworld.bungeeapi.user.service.UserServiceImpl;
import minecraft.sightworld.bungeeapi.whitelist.WhitelistManager;
import minecraft.sightworld.defaultlib.database.Database;
import minecraft.sightworld.defaultlib.localization.LocalizationService;
import minecraft.sightworld.defaultlib.localization.impl.LocalizationServiceImpl;
import minecraft.sightworld.defaultlib.messaging.MessageService;
import minecraft.sightworld.defaultlib.messaging.impl.MessageServiceImpl;
import minecraft.sightworld.defaultlib.redis.DefaultRedisFactory;
import minecraft.sightworld.defaultlib.redis.RedisFactory;
import minecraft.sightworld.defaultlib.user.service.UserService;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.redisson.api.RedissonClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.stream.Stream;

public final class SightWorld extends Plugin {

    @Getter
    private static SightWorld instance;

    @Getter
    private static Configuration config;


    @Getter
    private final SchedulerManager schedulerManager = new SchedulerManager();

    @Getter
    private final AnnounceManager announceManager = new AnnounceManager();

    @Getter
    private final WhitelistManager whitelistManager = new WhitelistManager();

    @Getter
    private final TabManager tabManager = new TabManager();


    @Getter
    private static MessageService messagingService;

    @Getter
    private static BungeeGuiService bungeeGuiService;

    @Getter
    private static LocalizationService localizationService;

    @Getter
    private static Database database;

    @Getter
    private static UserService<ProxiedPlayer> userService;

    @Override
    public void onEnable() {
        instance = this;

        loadDatabase();
        loadLocalization();
        loadConfigs();

        userService = new UserServiceImpl(database.getUserDao());

        registerListeners(localizationService, userService);
        registerCommands();

        bungeeGuiService = new BungeeGuiServiceImpl();
        messagingService = registerMessageService();
        loadTab(localizationService);
    }

    private void registerListeners(LocalizationService localizationService, UserService<ProxiedPlayer> userService) {
        new GamerListener(this, userService);
        new PingListener(this, localizationService);
    }

    private void registerCommands() {
        new BungeeApiCommand(this);
        new BungeeWhitelistCommand(this);
    }

    public void loadConfigs() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        config = loadConfig();
        whitelistManager.load();
        announceManager.load();
    }

    public void loadLocalization() {
        localizationService = new LocalizationServiceImpl();

        Stream.of(localizationService.getFiles())
                .parallel()
                .forEach(file -> localizationService.download(file, "https://raw.githubusercontent.com/SightWorld/localization/main/lang/" + file +".json"));
    }

    public void loadDatabase() {
        database = new Database();
        try {
            database.load();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private MessageService registerMessageService() {
        RedisFactory redisFactory = new DefaultRedisFactory();
        RedissonClient redissonClient = redisFactory.create();

        MessageServiceImpl messagingService = new MessageServiceImpl(redissonClient);
        messagingService.addListener(new AcceptorListener(bungeeGuiService), "gui-acceptor");

        return messagingService;

    }

    private Configuration loadConfig() {
        val configFile = new File(SightWorld.getInstance().getDataFolder(), "config.yml");
        Configuration configuration = null;

        try {
            if (!configFile.exists()) {
                FileOutputStream outputStream = new FileOutputStream(configFile);
                InputStream in = SightWorld.getInstance().getResourceAsStream("config.yml");
                in.transferTo(outputStream);
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            } else {
                // Если файл уже существует, загружаем существующую конфигурацию
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return configuration;
    }

    public void loadTab(LocalizationService localizationService) {
        tabManager.load(localizationService);
    }
}
