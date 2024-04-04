package minecraft.sightworld.bungeeapi.gui.test;

import minecraft.sightworld.bungeeapi.command.SightCommand;
import minecraft.sightworld.bungeeapi.gamer.BungeeGamer;
import minecraft.sightworld.bungeeapi.gamer.entity.BungeeEntity;
import minecraft.sightworld.defaultlib.sound.SSound;
import minecraft.sightworld.defaultlib.sound.SSoundType;

public class TestCommand extends SightCommand {
    public TestCommand() {
        super("testco");
        setOnlyPlayers(true);
    }


    @Override
    public void execute(BungeeEntity entity, String[] args) {
        BungeeGamer gamer = (BungeeGamer) entity;

        // я конченый, бегите
        SSound sound = new SSound(SSoundType.AMBIENT_CAVE);
        gamer.playSound(sound);

    }

    @Override
    public Iterable<String> tabComplete(BungeeEntity entity, String[] args) {
        return null;
    }
}
