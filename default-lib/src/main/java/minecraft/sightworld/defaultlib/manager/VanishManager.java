package minecraft.sightworld.defaultlib.manager;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import minecraft.sightworld.defaultlib.gamer.GamerBase;
import minecraft.sightworld.defaultlib.gamer.IBaseGamer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
