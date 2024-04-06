package minecraft.sightworld.bungeeapi.command.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.command.BungeeCommand;
import minecraft.sightworld.defaultlib.user.User;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashSet;
import java.util.Set;

public class BungeeWhitelistCommand extends BungeeCommand<SightWorld> {
    public BungeeWhitelistCommand(SightWorld plugin) {
        super(plugin, "bwhitelist", "bungeewhitelist", "bwl");
    }

    @Override
    public void execute(User<?> entity, String[] args) {
        if (args.length == 0) {
            entity.sendMessage(
                    """
                        §d     /bwl on/off §f- Включить/Выключить белый список
                        §d     /bwl list §f- Вывести список игроков в белом списке
                        §d     /bwl add/del <игрок> §f- Добавить/удалить игрока из белого списка
                        """);
            return;
        }

        val command = args[0];
        switch (command.toLowerCase()) {
            case "on" -> {
                plugin.getWhitelistManager().setEnable(true);

                entity.sendMessage("§dСистема §8| §fБелый список §dвключен");
            }
            case "off" -> {
                plugin.getWhitelistManager().setEnable(false);

                entity.sendMessage("§dСистема §8| §fБелый список §5выключен");
            }
            case "add" -> {
                if (args.length < 2) {
                    entity.sendMessage("§dСистема §8| §fОшибка, используй: §d/wl add <ник>");
                    break;
                }
                plugin.getWhitelistManager().addPlayer(args[1].toLowerCase());

                entity.sendMessage("§dСистема §8| §fИгрок §d"
                        + args[1].toLowerCase() + " §fдобавлен в белый список!");

            }
            case "del", "delete" -> {
                if (args.length < 2) {
                    entity.sendMessage("§dСистема §8| §fОшибка, используй: §d/wl del <ник>");
                    break;
                }
                plugin.getWhitelistManager().removePlayer(args[1].toLowerCase());
                entity.sendMessage("§dСистема §8| §fИгрок §d"
                        + args[1].toLowerCase() + " §fудален из белого списка!");
            }
            case "list" -> {
                entity.sendMessage("§dСистема §8| §fИгроки в белом списке:");
                entity.sendMessage(" §5" + Joiner.on("§7, §5").join(
                        plugin.getWhitelistManager().getPlayerNames()));
            }
            default -> entity.sendMessage(
                    """
                            §d     /bwl on/off §f- Включить/Выключить белый список
                            §d     /bwl list §f- Вывести список игроков в белом списке
                            §d     /bwl add/del <игрок> §f- Добавить/удалить игрока из белого списка""");
        }
    }

    @Override
    public Iterable<String> tabComplete(User<?> entity, String[] args) {
        if (args.length == 0) return ImmutableSet.of();

        Set<String> matches = new HashSet<>();
        if (args.length == 1) {
            matches.add("on");
            matches.add("off");
            matches.add("list");
            matches.add("add");
            matches.add("del");
            return matches;
        }

        return ImmutableSet.of();
    }
}
