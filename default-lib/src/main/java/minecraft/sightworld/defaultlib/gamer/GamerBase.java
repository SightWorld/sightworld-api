package minecraft.sightworld.defaultlib.gamer;

import com.google.common.collect.ImmutableSet;
import lombok.val;
import minecraft.sightworld.defaultlib.manager.VanishManager;
import minecraft.sightworld.defaultlib.utils.AddressUtil;

import java.util.List;
import java.util.Set;

public abstract class GamerBase extends IBaseGamerImpl implements OnlineGamer {

    protected GamerBase(String name) {
        super(name);

        //initSection(SettingsSection.class);

        for (val section : getSections().values()) {
            if (section == null)
                return;

            if (section.loadData()) {
                continue;
            }

            throw new IllegalArgumentException("кажется что-то пошло не так, секция - "
                    + section.getClass().getSimpleName() + ", ник игрока - " + name);
        }
    }

    public void sendMessages(List<String> messages) {
        messages.forEach(this::sendMessage);
    }

    @Override
    public boolean isOnline() {
        return true;
    }


    @Override
    public String toString() {
        return "OnlineGamer{name=" + this.getName() + '}';
    }

    @Override
    public void remove() {
        GamerAPI.removeGamer(name);
    }

    @Override
    public boolean isVanish() {
        return VanishManager.containsGamer(name);
    }
}
