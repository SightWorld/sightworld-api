package minecraft.sightworld.defaultlib.manager;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class VanishManager {

    @Getter
    private static final Set<String> VANISH_GAMERS = new HashSet<>();

    public static void addGamer(String gamer) {
        VANISH_GAMERS.add(gamer);
    }

    public static void removeGamer(String gamer) {
        VANISH_GAMERS.remove(gamer);
    }

    public static boolean containsGamer(String gamer) {
        return VANISH_GAMERS.contains(gamer);
    }
}
