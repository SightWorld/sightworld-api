package minecraft.sightworld.bungeeapi.user;

import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.defaultlib.localization.Language;
import minecraft.sightworld.defaultlib.sound.BungeeSoundDto;
import minecraft.sightworld.defaultlib.sound.SSound;
import minecraft.sightworld.defaultlib.user.SyncUserData;
import minecraft.sightworld.defaultlib.user.User;
import minecraft.sightworld.defaultlib.user.UserData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.packet.Title;

import java.util.List;

public class ProxiedUser extends User<ProxiedPlayer> {
    public ProxiedUser(String name, UserData userData) {
        super(name, userData);
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return ProxyServer.getInstance().getPlayer(getName());
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        val player = getPlayer();
        if (player == null)
            return;

        player.sendTitle(ProxyServer.getInstance().createTitle()
                .title(new TextComponent(title))
                .subTitle(new TextComponent(subtitle))
                .fadeIn(fadeIn)
                .fadeOut(fadeOut)
                .stay(stay));
    }

    @Override
    public void sendActionBar(String message) {
        val player = getPlayer();
        if (player == null)
            return;

        player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    @Override
    public void playSound(SSound sound) {
        val player = getPlayer();
        if (player == null)
            return;

        BungeeSoundDto dto = new BungeeSoundDto(sound, getName());
        SightWorld.getMessagingService().sendMessage(dto, "sound");
    }

    @Override
    public void sendMessage(String message) {
        val player = getPlayer();
        if (player == null)
            return;
        player.sendMessage(new TextComponent(message));
    }

    @Override
    public void sendMessage(List<String> message) {
        for (String s : message) {
            sendMessage(s);
        }
    }

    @Override
    public void sendMessageLocale(String key, Object... args) {
        String message = SightWorld.getLocalizationService().get(Language.RUSSIAN, key, args);
        sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        val player = getPlayer();
        if (player == null)
            return false;
        return player.hasPermission(permission); //TODO
    }

    @Override
    public void syncData(SyncUserData data) {
        SightWorld.getMessagingService().sendMessage(data, "bukkit-user-sync");
    }

}
