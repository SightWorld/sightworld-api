package minecraft.sightworld.bungeeapi.command;

import com.google.common.collect.ImmutableSet;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.entity.BungeeEntityManager;
import minecraft.sightworld.defaultlib.user.User;
import minecraft.sightworld.defaultlib.utils.cooldown.GamerCooldown;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

import static lombok.AccessLevel.PRIVATE;

@Setter
@FieldDefaults(level = PRIVATE)
public abstract class BungeeCommand<T extends Plugin> extends Command implements TabExecutor {

    protected T plugin;

    final int FINAL_COOLDOWN = 3;

    boolean onlyPlayers;
    boolean onlyConsole;

    boolean onlyStaff;
    boolean onlyDonaters;

    int cooldown;
    String cooldownType;
    String cooldownError;

    String minPermission;

    public BungeeCommand(final T plugin, final String name, final String... aliases) {
        super(name, null, aliases);

        if (onlyConsole && onlyPlayers) {
            ProxyServer.getInstance().getLogger().severe("Далбоеб на кодере блять, onlyConsole и onlyPlayers не может сука стоять одновременно");
            return;
        }

        register(plugin);

        this.cooldown = FINAL_COOLDOWN;
        this.cooldownType = "command_cooldown";
    }

    private void register(final T plugin) {
        this.plugin = plugin;
        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, this);
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] strings) {
        val entity = BungeeEntityManager.getEntity(commandSender);
        if (entity == null)
            return;

        val checkPlayer = commandSender instanceof ProxiedPlayer;

        if (!checkPlayer && onlyPlayers) {
            entity.sendMessage("§cДанную команду нельзя использовать из консоли!");
            return;
        }

        if (checkPlayer) {
            val gamer = SightWorld.getUserService().getUser(commandSender.getName());
            if (gamer == null)
                return;

            val player = (ProxiedPlayer) commandSender;
            if (!player.isConnected())
                return;

            if ((minPermission != null && !player.hasPermission(minPermission))
                    || (!player.hasPermission("sightworld.staff") && onlyStaff)) {
                entity.sendMessageLocale("main_no_perm");
                return;
            }

            if (!player.hasPermission("sightworld.donate") && onlyDonaters) {
                entity.sendMessageLocale("main_no_perm_2");
                return;
            }

            if (onlyConsole) {
                entity.sendMessage("§cДанную команду можно использовать только из консоли!");
                return;
            }

            if (GamerCooldown.hasCooldown(player.getName(), cooldownType)) {
                if (cooldown != FINAL_COOLDOWN) {
                    if (cooldownError == null) {
                        val time = GamerCooldown.getSecondCooldown(player.getName(), cooldownType);
                        entity.sendMessage("§cДанную команду можно использовать 1 раз в " + time + " секунд!");
                    } else {
                        entity.sendMessage(cooldownError);
                    }
                }
                return;
            }
            GamerCooldown.addCooldown(player.getName(), cooldownType, cooldown);

        }

        this.execute(entity, strings);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (onlyPlayers && !(sender instanceof ProxiedPlayer))
            return ImmutableSet.of();

        val entity = BungeeEntityManager.getEntity(sender);
        if (entity == null) return ImmutableSet.of();

        Iterable<String> complete = tabComplete(entity, args);
        if (complete == null) return ImmutableSet.of();

        return complete;
    }

    public abstract void execute(final User<?> entity, final String[] args);

    public abstract Iterable<String> tabComplete(final User<?> entity, final String[] args);

    public void setCooldown(int second, String type) {
        this.cooldown = second * 20;
        this.cooldownType = type;
    }

    public int getSecondCooldown() {
        return cooldown / 20;
    }
}