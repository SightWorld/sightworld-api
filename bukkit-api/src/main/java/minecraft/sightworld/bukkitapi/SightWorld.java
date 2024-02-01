package minecraft.sightworld.bukkitapi;

import lombok.Getter;
import minecraft.sightworld.bukkitapi.gamer.GamerManager;
import minecraft.sightworld.bukkitapi.gui.*;
import minecraft.sightworld.bukkitapi.listener.GamerListener;
import minecraft.sightworld.bukkitapi.listener.message.BungeeGuiListener;
import minecraft.sightworld.defaultlib.messaging.MessageService;
import minecraft.sightworld.defaultlib.messaging.impl.MessageServiceImpl;
import minecraft.sightworld.defaultlib.redis.DefaultRedisFactory;
import minecraft.sightworld.defaultlib.redis.RedisFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.api.RedissonClient;

public class SightWorld extends JavaPlugin {


    @Getter
    private static SightWorld instance;

    @Getter
    private static GamerManager gamerManager;



    @Override
    public void onEnable() {
        instance = this;
        gamerManager = new GamerManager(this);
        registerListeners();
        registerGuiService();

        MessageService messagingService = registerMessageService();
    }

    private void registerListeners() {
        new GamerListener(this);

    }

    @Override
    public void onDisable() {

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

        return messagingService;

    }
}