package minecraft.sightworld.defaultlib.utils.cooldown;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("unused")
public class PlayerCooldown {

    private final Table<String, String, Long> COOLDOWN_TABLE = HashBasedTable.create();

    public void putCooldown(@NonNull String delayName, @NonNull String name, long mills) {
        COOLDOWN_TABLE.put(delayName, name, System.currentTimeMillis() + mills);
    }

    public long getCooldown(@NonNull String delayName, @NonNull String name) {
        Long playerDelay = COOLDOWN_TABLE.get(delayName, name);
        return playerDelay == null ? 0 : playerDelay - System.currentTimeMillis();
    }

    public void removeCooldown(@NonNull String delayName, @NonNull String name) {
        COOLDOWN_TABLE.remove(delayName, name);
    }

    public boolean hasCooldown(@NonNull String delayName, @NonNull String name) {
        return getCooldown(delayName, name) > 0;
    }

}