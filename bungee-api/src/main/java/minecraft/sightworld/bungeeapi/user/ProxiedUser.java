package minecraft.sightworld.bungeeapi.user;

import minecraft.sightworld.defaultlib.sound.SSound;
import minecraft.sightworld.defaultlib.user.User;
import minecraft.sightworld.defaultlib.user.UserData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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

    }

    @Override
    public void sendActionBar(String message) {

    }

    @Override
    public void playSound(SSound sound) {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(List<String> message) {

    }

    @Override
    public void sendMessageLocale(String key, Object... args) {

    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }
}
