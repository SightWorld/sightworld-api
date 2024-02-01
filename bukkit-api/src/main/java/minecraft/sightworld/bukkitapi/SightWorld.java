package minecraft.sightworld.bukkitapi;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.Getter;
import minecraft.sightworld.bukkitapi.gamer.GamerManager;
import minecraft.sightworld.bukkitapi.gui.*;
import minecraft.sightworld.bukkitapi.listener.GamerListener;
import minecraft.sightworld.bukkitapi.listener.message.BungeeGuiListener;
import minecraft.sightworld.bukkitapi.messaging.MessagingServiceImpl;
import minecraft.sightworld.defaultlib.messaging.MessagingService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        MessagingService<Player> messagingService = registerMessageService(gson, jsonParser);
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

    private MessagingService<Player> registerMessageService(Gson gson, JsonParser jsonParser) {
        MessagingServiceImpl messagingService = new MessagingServiceImpl(this, gson, jsonParser);
        messagingService.registerChannels();
        messagingService.addListener(new BungeeGuiListener(messagingService), "gui");
        return messagingService;
    }
}