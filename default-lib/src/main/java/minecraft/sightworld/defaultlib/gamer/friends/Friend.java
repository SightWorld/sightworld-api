package minecraft.sightworld.defaultlib.gamer.friends;

import minecraft.sightworld.defaultlib.gamer.IBaseGamer;

public interface Friend {

    int getPlayerId();

    boolean isOnline();

    <T extends IBaseGamer> T getGamer();
}
