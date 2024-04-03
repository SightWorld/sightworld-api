package minecraft.sightworld.bungeeapi.command.impl;

import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.command.BungeeCommand;
import minecraft.sightworld.bungeeapi.gamer.entity.BungeeEntity;
import minecraft.sightworld.defaultlib.utils.TimeUtil;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Locale;

public class BungeeApiCommand extends BungeeCommand<SightWorld> {
    public BungeeApiCommand(SightWorld plugin) {
        super(plugin, "bungee-api", "bungeeapi");
        setMinPermission("sightworld.admin");
    }

    @Override
    public void execute(BungeeEntity entity, String[] args) {
        if (args.length == 0 ) {
            entity.sendMessage("§dSWBungeeAPI §fmade by Dwyur");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "mem" -> {
                val runtime = Runtime.getRuntime();
                entity.sendMessage("§dСистема §8| §fСтатистика памяти§f:");
                entity.sendMessage(" §c▪ §fАптайм: §7"
                        + TimeUtil.formatTimeElapsed(
                        System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()));
                entity.sendMessage(" §c▪ §fМаксимально: §7" + runtime.maxMemory() / 1048576L + " МБ");
                entity.sendMessage(" §c▪ §fВыделено: §a" + runtime.totalMemory() / 1048576L + " МБ");
                entity.sendMessage(" §c▪ §fСвободно: §e" + runtime.freeMemory() / 1048576L + " МБ");
                entity.sendMessage(" §c▪ §fИспользуется: §c" + (runtime.totalMemory() - runtime.freeMemory())
                        / 1048576L + " МБ");
                entity.sendMessage(" ");
            }

            case "reload" -> {
                plugin.getAnnounceManager().getTask().cancel();
                plugin.getTabManager().getTask().cancel();
                plugin.loadConfigs();
                plugin.loadTab();

                entity.sendMessage("§dСистема §8| §fКонфиг BungeeAPI успешно перегружен! Made by Dwyur");
            }
        }

    }

    @Override
    public Iterable<String> tabComplete(BungeeEntity entity, String[] args) {
        if (entity.hasPermission("sightworld.admin")) {
            return List.of("reload", "mem");
        }
        return null;
    }
}
