package minecraft.sightworld.defaultlib.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import minecraft.sightworld.defaultlib.group.Group;
import minecraft.sightworld.defaultlib.sound.SSound;
import minecraft.sightworld.defaultlib.user.session.UserSession;

import java.util.ArrayList;
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
    public long getPlayedTime() {
        long playedTime = 0;

        for (UserSession session : userData.getSessions()) {
            if (userData.getActiveSession() != null && session == userData.getActiveSession()) {
                playedTime += (System.currentTimeMillis() - userData.getActiveSession().getStartPlay()) / 1000;
                continue;
            }
            playedTime += session.getPlayed();
        }
        return playedTime;
    }

    public UserSession getLastSession() {
        try {
            return new ArrayList<>(userData.getSessions()).get(userData.getSessions().size() - 1);
        } catch(IndexOutOfBoundsException ex) {
            return null;
        }
    }
    public String getChatName() {
        return getUserData().getPrefix() + getName();
    }

    public String getDisplayName() {
        return getUserData().getPrefix() + getName() + getUserData().getTag();
    }

    public Group getGroup() {
        return Group.getGroupByName(getUserData().getGroup());
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
