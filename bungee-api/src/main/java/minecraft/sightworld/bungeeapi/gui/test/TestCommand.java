package minecraft.sightworld.bungeeapi.gui.test;

import minecraft.sightworld.bungeeapi.command.SightCommand;
import minecraft.sightworld.bungeeapi.gamer.BungeeGamer;
import minecraft.sightworld.bungeeapi.gamer.entity.BungeeEntity;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.score.Scoreboard;
import net.md_5.bungee.api.score.Team;

public class TestCommand extends SightCommand {
    public TestCommand() {
        super("testco");
        setOnlyPlayers(true);
    }


    @Override
    public void execute(BungeeEntity entity, String[] args) {
        BungeeGamer gamer = (BungeeGamer) entity;
        new TestGui(gamer.getPlayer());
        Scoreboard scoreboard = new Scoreboard();

        Team team = new Team("000" + gamer.getName());
        team.setDisplayName(gamer.getDisplayName());
        team.addPlayer(gamer.getName());
        scoreboard.addTeam(team);

    }

    @Override
    public Iterable<String> tabComplete(BungeeEntity entity, String[] args) {
        return null;
    }
}
