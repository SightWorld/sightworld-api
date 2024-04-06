package minecraft.sightworld.bukkitapi.user;

import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.defaultlib.localization.Language;
import minecraft.sightworld.defaultlib.sound.SSound;
import minecraft.sightworld.defaultlib.user.User;
import minecraft.sightworld.defaultlib.user.UserData;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.util.List;

public class ConsoleUser extends User<ConsoleCommandSender> {
    public ConsoleUser(String name, UserData userData) {
        super(name, userData);
    }

    @Override
    public ConsoleCommandSender getPlayer() {
        return null;
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {

    }

    @Override
    public void sendActionBar(String message) {

    }

    @Override
    public void playSound(SSound sound) {

    }

    @Override
    public void sendMessage(String message) {
        Bukkit.getLogger().info(message);
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
        return true;
    }
}
