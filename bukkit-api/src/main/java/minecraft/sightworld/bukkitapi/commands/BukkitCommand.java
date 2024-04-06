package minecraft.sightworld.bukkitapi.commands;

import com.google.common.collect.ImmutableList;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.entity.BukkitEntityManager;
import minecraft.sightworld.defaultlib.user.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Setter
@FieldDefaults(level = PRIVATE)
public abstract class BukkitCommand<T extends JavaPlugin> implements CommandExecutor, TabExecutor {

    protected T plugin;

    boolean onlyPlayers;
    boolean onlyConsole;
    String minPermission = null;

    public BukkitCommand(final T plugin, final String name) {

        if (onlyConsole && onlyPlayers) {
            SightWorld.getInstance().getLogger().severe("Далбоеб на кодере блять, onlyConsole и onlyPlayers не может сука стоять одновременно");
            return;
        }

        register(plugin, name);
    }


    private void register(final T plugin, String name) {
        this.plugin = plugin;
        SightWorld.getInstance().getCommand(name).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        val entity = BukkitEntityManager.getEntity(commandSender);
        if (entity == null)
            return true;

        val checkPlayer = commandSender instanceof Player;

        if (!checkPlayer && onlyPlayers) {
            entity.sendMessage("§cДанную команду нельзя использовать из консоли!");
            return true;
        }

        if (checkPlayer) {
            val gamer = SightWorld.getUserService().getUser(commandSender.getName());
            if (gamer == null)
                return true;

            val player = (Player) commandSender;
            if (!player.isOnline())
                return true;

            if ((minPermission != null) && (!player.hasPermission(minPermission))) {
                entity.sendMessageLocale("main_no_perm");
                return true;
            }
            if (onlyConsole) {
                entity.sendMessage("§cДанную команду можно использовать только из консоли!");
                return true;
            }


        }

        this.execute(entity, strings);
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (onlyPlayers && !(sender instanceof Player))
            return ImmutableList.of();

        val entity = BukkitEntityManager.getEntity(sender);
        if (entity == null) return ImmutableList.of();

        List<String> complete = tabComplete(entity, args);
        if (complete == null) return ImmutableList.of();

        return complete;
    }


    public abstract void execute(final User<?> entity, final String[] args);

    public abstract List<String> tabComplete(final User<?> entity, final String[] args);
}
