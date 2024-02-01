package minecraft.sightworld.bungeeapi;

import lombok.Getter;
import minecraft.sightworld.bungeeapi.command.TestGuiCommand;
import minecraft.sightworld.bungeeapi.gui.acceptor.AcceptorListener;
import minecraft.sightworld.bungeeapi.gui.service.BungeeGuiService;
import minecraft.sightworld.bungeeapi.gui.service.impl.BungeeGuiServiceImpl;
import minecraft.sightworld.bungeeapi.listener.GamerListener;
import minecraft.sightworld.bungeeapi.manager.LuckPermsManager;
import minecraft.sightworld.bungeeapi.scheduler.SchedulerManager;
import minecraft.sightworld.defaultlib.messaging.MessageService;
import minecraft.sightworld.defaultlib.messaging.impl.MessageServiceImpl;
import minecraft.sightworld.defaultlib.redis.DefaultRedisFactory;
import minecraft.sightworld.defaultlib.redis.RedisFactory;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.redisson.api.RedissonClient;

public final class SightWorld extends Plugin {

    public static final String CHANNEL = "sightworld";

    @Getter
    private static SightWorld instance;

    @Getter
    private final SchedulerManager schedulerManager = new SchedulerManager();

    @Getter
    private static MessageService messagingService;

    @Getter
    private static BungeeGuiService bungeeGuiService;

    @Override
    public void onEnable() {
        instance = this;

        ProxyServer.getInstance().registerChannel(CHANNEL); // TODO: channelAPI
        new GamerListener(this);
        LuckPermsManager.registerEvents(this);

        bungeeGuiService = new BungeeGuiServiceImpl();
        messagingService = registerMessageService();

        getProxy().getPluginManager().registerCommand(this, new TestGuiCommand());
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().unregisterChannel(CHANNEL);
    }

    private MessageService registerMessageService() {
        RedisFactory redisFactory = new DefaultRedisFactory();
        RedissonClient redissonClient = redisFactory.create();

        MessageServiceImpl messagingService = new MessageServiceImpl(redissonClient);
        messagingService.addListener(new AcceptorListener(bungeeGuiService), "gui-acceptor");

        return messagingService;

    }
}
