package minecraft.sightworld.bungeeapi.messaging;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.defaultlib.messaging.BaseMessagingService;
import minecraft.sightworld.defaultlib.messaging.MessageListener;
import minecraft.sightworld.defaultlib.messaging.MessagingService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagingServiceImpl extends BaseMessagingService<ProxiedPlayer> implements MessagingService<ProxiedPlayer>, Listener {
    private final SightWorld bungee;
    private final ProxyServer proxyServer;

    public MessagingServiceImpl(SightWorld bungee, Gson gson, JsonParser jsonParser) {
        super(gson, jsonParser);
        this.bungee = bungee;
        this.proxyServer = bungee.getProxy();
    }

    public void registerChannels() {
        proxyServer.getPluginManager().registerListener(bungee, this);
        proxyServer.registerChannel("sightworld:main");
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        String channel = event.getTag();
        if (!channel.equals("sightworld:main")) return;


        byte[] bytes = event.getData();

        try {
            JsonElement jsonElement = jsonParser.parse(new String(bytes, StandardCharsets.UTF_8));
            String subChannel = jsonElement.getAsJsonObject().getAsJsonPrimitive("__sub_channel").getAsString();
            String playerName = jsonElement.getAsJsonObject().getAsJsonPrimitive("__player").getAsString();

            ProxiedPlayer player = proxyServer.getPlayer(playerName);

            if (player == null) {
                return;
            }

            Class<?> messageClass = subChannelMap.get(subChannel);

            if (messageClass == null) {
                return;
            }

            List<MessageListener<ProxiedPlayer, ?>> messageListeners = listenerMap.get(subChannel);

            if (messageListeners == null) {
                return;
            }

            Object o = gson.fromJson(jsonElement, messageClass);
            messageListeners.forEach(messageListener -> messageListener.onMessage(player, channel, convertObject(o)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("ignore all")
    public void addListener(MessageListener<ProxiedPlayer, ?> messageListener, String... channels) {
        Arrays.stream(channels).forEach(channel -> {
            listenerMap.computeIfAbsent(channel, k -> new ArrayList<>()).add(messageListener);
            subChannelMap.put(channel, messageListener.getMessageClass());
        });
    }

    @Override
    public void sendMessage(ProxiedPlayer player, Object o, String... channels) {
        Arrays.stream(channels).forEach(channel -> {
            JsonElement jsonElement = gson.toJsonTree(o);
            jsonElement.getAsJsonObject().addProperty("__sub_channel", channel);
            player.getServer().sendData("sightworld:main", jsonElement.toString().getBytes(StandardCharsets.UTF_8));
        });
    }
}
