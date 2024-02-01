package minecraft.sightworld.bungeeapi;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.Getter;
import minecraft.sightworld.bungeeapi.command.TestGuiCommand;
import minecraft.sightworld.bungeeapi.gui.acceptor.AcceptorListener;
import minecraft.sightworld.bungeeapi.gui.service.BungeeGuiService;
import minecraft.sightworld.bungeeapi.gui.service.impl.BungeeGuiServiceImpl;
import minecraft.sightworld.bungeeapi.listener.GamerListener;
import minecraft.sightworld.bungeeapi.manager.LuckPermsManager;
import minecraft.sightworld.bungeeapi.messaging.MessagingServiceImpl;
import minecraft.sightworld.bungeeapi.scheduler.SchedulerManager;
import minecraft.sightworld.defaultlib.messaging.MessagingService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public final class SightWorld extends Plugin {

    public static final String CHANNEL = "sightworld";

    @Getter
    private static SightWorld instance;

    @Getter
    private final SchedulerManager schedulerManager = new SchedulerManager();

    @Getter
    private static MessagingService<ProxiedPlayer> messagingService;

    @Getter
    private static BungeeGuiService bungeeGuiService;

    @Override
    public void onEnable() {
        instance = this;

        ProxyServer.getInstance().registerChannel(CHANNEL); // TODO: channelAPI
        new GamerListener(this);
        LuckPermsManager.registerEvents(this);

        bungeeGuiService = new BungeeGuiServiceImpl();

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        messagingService = registerMessageService(gson, jsonParser, bungeeGuiService);

        getProxy().getPluginManager().registerCommand(this, new TestGuiCommand());
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().unregisterChannel(CHANNEL);
    }

    private MessagingService<ProxiedPlayer> registerMessageService(Gson gson, JsonParser jsonParser, BungeeGuiService bungeeGuiService) {
        MessagingServiceImpl messagingService = new MessagingServiceImpl(this, gson, jsonParser);
        messagingService.addListener(new AcceptorListener(bungeeGuiService), "gui-acceptor");
        messagingService.registerChannels();

        return messagingService;
    }
}
