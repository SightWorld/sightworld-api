package minecraft.sightworld.bukkitapi.commands.impl;

import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.commands.BukkitCommand;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitEntity;

import java.util.List;

public class ApiCommand extends BukkitCommand<SightWorld> {
    public ApiCommand(SightWorld plugin) {
        super(plugin, "bukkit-api");
    }

    @Override
    public void execute(BukkitEntity entity, String[] args) {
        if (!entity.hasPermission("sightworld.admin")) {
            entity.sendMessage("§cНет прав!");
            return;
        }

        if (args.length == 0) {
            entity.sendMessage("§cИспользуйте /bukkit-api <reload>");
            return;
        }

        switch (args[0]) {
            case "reload" -> {
                entity.sendMessage("§aконфигурация API (v1.0, made by Dwyur) успешно перезагружена!");
            }
        }
    }

    @Override
    public List<String> tabComplete(BukkitEntity entity, String[] args) {
        if (entity.hasPermission("sightworld.admin")) {
            return List.of("reload");
        }
        return null;
    }
}
