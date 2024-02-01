package minecraft.sightworld.bukkitapi.gamer;

import lombok.Getter;
import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitEntity;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitGamer;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitServer;
import minecraft.sightworld.defaultlib.gamer.GamerAPI;
import minecraft.sightworld.defaultlib.gamer.GamerBase;
import minecraft.sightworld.defaultlib.gamer.IBaseGamer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GamerManager {

    @Getter
    private final BukkitServer server;

    public GamerManager(SightWorld sightworld) {
        server = new BukkitServer(sightworld, "console");
    }

    public BukkitEntity getEntity(CommandSender sender) {
        if (sender instanceof Player) {
            return getGamer(sender.getName());
        } else if (sender instanceof ConsoleCommandSender) {
            return server;
        } else {
            return null;
        }
    }

    public BukkitGamer getGamer(String name) {
        return (BukkitGamer) GamerAPI.getByName(name);
    }

    public BukkitGamer getGamer(Player player) {
        if (player == null) {
            return null;
        }
        return getGamer(player.getName());
    }

    public BukkitGamer getGamer(int playerID) {
        return (BukkitGamer) GamerAPI.getOnline(playerID);
    }

    public void removeGamer(String name) {
        GamerAPI.removeGamer(name.toLowerCase());
    }

    public void removeGamer(@NotNull Player player) {
        removeGamer(player.getName());
    }

    public void removeGamer(@NotNull BukkitGamer gamer) {
        removeGamer(gamer.getName());
    }

    public boolean containsGamer(@NotNull Player player) {
        return containsGamer(player.getName());
    }

    public boolean containsGamer(String name) {
        return GamerAPI.contains(name);
    }

    public Map<String, BukkitEntity> getGamerEntities() {
        Map<String, BukkitEntity> gamerEntities = new HashMap<>();
        gamerEntities.put(server.getName(), server);
        for (GamerBase gamerBase : GamerAPI.getGamers().values()) {
            gamerEntities.put(gamerBase.getName(), (BukkitGamer) gamerBase);
        }

        return gamerEntities;
    }

    public Map<String, BukkitGamer> getGamers() {
        Map<String, BukkitGamer> gamers = new HashMap<>();
        for (GamerBase gamerBase : GamerAPI.getGamers().values()) {
            gamers.put(gamerBase.getName().toLowerCase(), (BukkitGamer) gamerBase);
        }
        return gamers;
    }

    public IBaseGamer getOrCreate(int playerID) {
        IBaseGamer gamer = GamerAPI.getOnline(playerID);
        if (gamer != null) {
            return gamer;
        }
        return GamerAPI.getById(playerID);
    }

    public GamerBase getOnlineGamer(final @NotNull String name) {
        return GamerAPI.getOnline(getGamer(name).getPlayerID());
    }

    public IBaseGamer getOrCreate(String name) {
        return GamerAPI.getOrCreate(name);
    }
}
