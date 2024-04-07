package minecraft.sightworld.bukkitapi.user;

import com.viaversion.viaversion.api.Via;
import lombok.val;
import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.defaultlib.localization.Language;
import minecraft.sightworld.defaultlib.sound.BungeeSoundDto;
import minecraft.sightworld.defaultlib.sound.SSound;
import minecraft.sightworld.defaultlib.user.SyncUserData;
import minecraft.sightworld.defaultlib.user.User;
import minecraft.sightworld.defaultlib.user.UserData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class BukkitUser extends User<Player> {
    public BukkitUser(String name, UserData userData) {
        super(name, userData);
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(getName());
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        val player = getPlayer();
        if (player == null)
            return;

        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void sendActionBar(String message) {
        val player = getPlayer();
        if (player == null)
            return;

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    @Override
    public void playSound(SSound sound) {
        val player = getPlayer();
        if (player == null)
            return;
        player.playSound(player.getLocation(), Sound.valueOf(sound.getSoundType().name()), sound.getVolume(), sound.getPitch());
    }

    @Override
    public void sendMessage(String message) {
        val player = getPlayer();
        if (player == null)
            return;
        player.sendMessage(message);
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
        SightWorld.getMessagingService().sendMessage(data, "proxy-user-sync");
    }

    public int getVersion() {
        return Via.getAPI().getPlayerVersion(getPlayer().getUniqueId());
    }
}
