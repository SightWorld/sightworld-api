package minecraft.sightworld.bukkitapi.commands.impl;

import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.commands.BukkitCommand;
import minecraft.sightworld.bukkitapi.user.BukkitUser;
import minecraft.sightworld.bukkitapi.util.ColorUtils;
import minecraft.sightworld.defaultlib.user.User;

import java.util.List;

public class ApiCommand extends BukkitCommand<SightWorld> {
    public ApiCommand(SightWorld plugin) {
        super(plugin, "bukkit-api");
    }

    @Override
    public void execute(User<?> entity, String[] args) {
        BukkitUser user = (BukkitUser) entity;
        user.sendMessage("Ваше наигранное время: " + user.getPlayedTime() + " секунд");
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
    public List<String> tabComplete(User<?> entity, String[] args) {
        if (entity.hasPermission("sightworld.admin")) {
            return List.of("reload");
        }
        return null;
    }
}
