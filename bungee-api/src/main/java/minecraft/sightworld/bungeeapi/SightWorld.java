package minecraft.sightworld.bungeeapi;

import lombok.Getter;
import lombok.val;
import minecraft.sightworld.bungeeapi.announce.AnnounceManager;
import minecraft.sightworld.bungeeapi.gui.acceptor.AcceptorListener;
import minecraft.sightworld.bungeeapi.gui.service.BungeeGuiService;
import minecraft.sightworld.bungeeapi.gui.service.impl.BungeeGuiServiceImpl;
import minecraft.sightworld.bungeeapi.gui.test.TestCommand;
import minecraft.sightworld.bungeeapi.listener.GamerListener;
import minecraft.sightworld.bungeeapi.manager.LuckPermsManager;
import minecraft.sightworld.bungeeapi.scheduler.SchedulerManager;
import minecraft.sightworld.defaultlib.messaging.MessageService;
import minecraft.sightworld.defaultlib.messaging.impl.MessageServiceImpl;
import minecraft.sightworld.defaultlib.redis.DefaultRedisFactory;
import minecraft.sightworld.defaultlib.redis.RedisFactory;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.score.Scoreboard;
import net.md_5.bungee.api.score.Team;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.redisson.api.RedissonClient;

import java.io.File;
import java.nio.file.Files;

public final class SightWorld extends Plugin {

    @Getter
    private static SightWorld instance;

    @Getter
    private final SchedulerManager schedulerManager = new SchedulerManager();

    @Getter
    private static MessageService messagingService;

    @Getter
    private static BungeeGuiService bungeeGuiService;

    private final AnnounceManager announceManager = new AnnounceManager();

    @Override
    public void onEnable() {
        instance = this;
//        loadConfigs();

        new GamerListener(this);
        LuckPermsManager.registerEvents(this);

        bungeeGuiService = new BungeeGuiServiceImpl();
        messagingService = registerMessageService();
        getProxy().getPluginManager().registerCommand(this, new TestCommand());
    }

    @Override
    public void onDisable() {

    }

    public void loadConfigs() {
        loadConfig();
        announceManager.load();
    }

    private MessageService registerMessageService() {
        RedisFactory redisFactory = new DefaultRedisFactory();
        RedissonClient redissonClient = redisFactory.create();

        MessageServiceImpl messagingService = new MessageServiceImpl(redissonClient);
        messagingService.addListener(new AcceptorListener(bungeeGuiService), "gui-acceptor");

        return messagingService;

    }

    private Configuration loadConfig() {
        val config = new File(getDataFolder(),"config.yml");
        Configuration cfg = null;
        try {
            if (!config.exists()) {
                Files.copy(getInstance().getResourceAsStream("config.yml"), config.toPath());
            }
            cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return cfg;
    }
}
