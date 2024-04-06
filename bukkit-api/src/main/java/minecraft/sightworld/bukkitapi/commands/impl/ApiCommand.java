package minecraft.sightworld.bukkitapi.commands.impl;

import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.commands.BukkitCommand;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitEntity;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitUser;
import minecraft.sightworld.bukkitapi.util.ColorUtils;

import java.util.List;

public class ApiCommand extends BukkitCommand<SightWorld> {
    public ApiCommand(SightWorld plugin) {
        super(plugin, "bukkit-api");
    }

    @Override
    public void execute(BukkitEntity entity, String[] args) {
        if (!entity.hasPermission("sightworld.admin")) {
            entity.sendMessageLocale("main_no_perm");
            return;
        }

        if (args.length == 0) {
            entity.sendMessage("§cИспользуйте /bukkit-api <reload>");
            return;
        }

        switch (args[0]) {
            case "reload" -> {
                entity.sendMessage("§aКонфигурация API §f(v1.0, made by Dwyur)§a успешно перезагружена!");
            }
            case "test" -> {
                entity.sendMessage(ColorUtils.fixForLegacyPlayers((BukkitUser) entity, "&#FBA0A0 &lADMIN &r&#FBA0A0Dwyur", "&4&lADMIN &r&lDwyur"));
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
