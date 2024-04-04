package minecraft.sightworld.bungeeapi.announce;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE)
public class AnnounceManager {
    Configuration config;
    ScheduledTask task;

    public void load() {
        if (task != null) task.cancel();

        config = loadConfig();
        startAnnounceTask(config.getInt("announcer_period"));
    }

    private @NotNull LinkedList<AnnounceMessage> getAnnounceMessages() {
        LinkedList<AnnounceMessage> announceMessages = new LinkedList<>();
        for (val announceMessageKey : config.getSection("announcer").getKeys()) {
            val messageSection = config.getSection("announcer").getSection(announceMessageKey);

            ClickEvent.Action clickAction = null;
            String clickActionContext = null;

            if (messageSection.get("click") != null) {
                clickAction = ClickEvent.Action.valueOf(
                        messageSection.getString("click.action"));

                clickActionContext = messageSection.getString("click.context");
            }
            announceMessages.add(new AnnounceMessage(messageSection.getStringList("messages"),
                    messageSection.getString("click.hover"), clickAction, clickActionContext));
        }

        return announceMessages;
}

    public void startAnnounceTask(int periodInMinutes) {
        val announceMessages = getAnnounceMessages();

        if (announceMessages.isEmpty())
            return;

        task = SightWorld.getInstance()
                .getProxy()
                .getScheduler()
                .schedule(SightWorld.getInstance(), new Runnable() {
                    private int messageCounter = 0;

                    @Override
                    public void run() {
                        val announceMessage = announceMessages.get(messageCounter);
                        for (val player : ProxyServer.getInstance().getPlayers())
                            announceMessage.sendTo(player);

                        ++messageCounter;
                        if (messageCounter >= announceMessages.size()) {
                            messageCounter = 0;
                        }}

                }, periodInMinutes, periodInMinutes, TimeUnit.MINUTES);
    }

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    private static class AnnounceMessage {
        List<String> listMessages;
        String hover;
        ClickEvent.Action clickAction;
        String clickActionContext;

        private void sendTo(ProxiedPlayer player) {
            if (listMessages.isEmpty() || player.getServer().getInfo().getName().startsWith("auth"))
                return;

            //val gamer = BungeeGamer.getGamer(player);


            val componentBuilder = new ComponentBuilder(
                    Joiner.on("\n").join(listMessages));

            if (clickAction != null && clickActionContext != null) {
                componentBuilder.event(new ClickEvent(clickAction, clickActionContext));
            }

            if (hover != null) {
                componentBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        TextComponent.fromLegacyText(hover)));
            }

            player.sendMessage(ChatMessageType.CHAT, componentBuilder.create());

        }
    }

    Configuration loadConfig() {
        val configFile = new File(SightWorld.getInstance().getDataFolder(), "announce.yml");
        Configuration configuration = null;

        try {
            if (!configFile.exists()) {
                FileOutputStream outputStream = new FileOutputStream(configFile);
                InputStream in = SightWorld.getInstance().getResourceAsStream("announce.yml");
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

}