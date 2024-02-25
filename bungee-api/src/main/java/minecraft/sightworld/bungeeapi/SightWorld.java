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
import minecraft.sightworld.bungeeapi.manager.LuckPermsManager;
import minecraft.sightworld.bungeeapi.scheduler.SchedulerManager;
import minecraft.sightworld.bungeeapi.tab.TabManager;
import minecraft.sightworld.bungeeapi.whitelist.WhitelistManager;
import minecraft.sightworld.defaultlib.messaging.MessageService;
import minecraft.sightworld.defaultlib.messaging.impl.MessageServiceImpl;
import minecraft.sightworld.defaultlib.redis.DefaultRedisFactory;
import minecraft.sightworld.defaultlib.redis.RedisFactory;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.redisson.api.RedissonClient;

import java.io.*;

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


    @Override
    public void onEnable() {
        instance = this;
        loadConfigs();

        registerListeners();
        registerCommands();

        bungeeGuiService = new BungeeGuiServiceImpl();
        messagingService = registerMessageService();

        loadTab();
    }

    private void registerListeners() {
        new GamerListener(this);
        new PingListener(this);
        LuckPermsManager.registerEvents(this);
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

    private MessageService registerMessageService() {
        RedisFactory redisFactory = new DefaultRedisFactory();
        RedissonClient redissonClient = redisFactory.create();

        MessageServiceImpl messagingService = new MessageServiceImpl(redissonClient);
        messagingService.addListener(new AcceptorListener(bungeeGuiService), "gui-acceptor");

        return messagingService;

    }

    Configuration loadConfig() {
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

    public void loadTab() {
        tabManager.load(config);
    }
}
