package minecraft.sightworld.bukkitapi.messaging;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import minecraft.sightworld.defaultlib.messaging.BaseMessagingService;
import minecraft.sightworld.defaultlib.messaging.MessageListener;
import minecraft.sightworld.defaultlib.messaging.MessagingService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagingServiceImpl extends BaseMessagingService<Player> implements PluginMessageListener, MessagingService<Player> {
    private final Plugin plugin;

    public MessagingServiceImpl(Plugin plugin, Gson gson, JsonParser jsonParser) {
        super(gson, jsonParser);
        this.plugin = plugin;
    }

    public void registerChannels() {
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(plugin, "sightworld:main", this);
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "sightworld:main");
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] bytes) {
        if (!channel.equals("sightworld:main")) return;

        try {
            JsonElement jsonElement = jsonParser.parse(new String(bytes, StandardCharsets.UTF_8));
            String subChannel = jsonElement.getAsJsonObject().getAsJsonPrimitive("__sub_channel").getAsString();
            Class<?> messageClass = subChannelMap.get(subChannel);

            if (messageClass == null) {
                return;
            }

            List<MessageListener<Player, ?>> messageListeners = listenerMap.get(subChannel);

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
    public void addListener(MessageListener<Player, ?> messageListener, String... channels) {
        Arrays.stream(channels).forEach(channel -> {
            listenerMap.computeIfAbsent(channel, k -> new ArrayList<>()).add(messageListener);
            subChannelMap.put(channel, messageListener.getMessageClass());
        });
    }

    @Override
    public void sendMessage(Player player, Object o, String... channels) {
        Arrays.stream(channels).forEach(channel -> {
            JsonElement jsonElement = gson.toJsonTree(o);
            jsonElement.getAsJsonObject().addProperty("__sub_channel", channel);
            jsonElement.getAsJsonObject().addProperty("__player", player.getName());
            player.sendPluginMessage(plugin, "sightworld:main", jsonElement.toString().getBytes(StandardCharsets.UTF_8));
        });
    }
}
