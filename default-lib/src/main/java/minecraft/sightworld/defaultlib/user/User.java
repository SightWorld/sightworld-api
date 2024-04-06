package minecraft.sightworld.defaultlib.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import minecraft.sightworld.defaultlib.sound.SSound;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public abstract class User<P> {

    private String name;

    private UserData userData;

    public boolean isOnline() {
        return getPlayer() != null;
    }
    public abstract P getPlayer();
    public abstract void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut);
    public abstract void sendActionBar(String message);
    public abstract void playSound(SSound sound);
    public abstract void sendMessage(String message);
    public abstract void sendMessage(List<String> message);
    public abstract void sendMessageLocale(String key, Object... args);
    public abstract boolean hasPermission(String permission);

}
