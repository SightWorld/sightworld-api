package minecraft.sightworld.bukkitapi.gamer.entity;

import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.defaultlib.gamer.IBaseGamer;
import minecraft.sightworld.defaultlib.gamer.OnlineGamer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface BukkitGamer extends BukkitEntity, OnlineGamer, IBaseGamer {

    static BukkitGamer getGamer(final @NotNull Player player) {
        return getGamer(player.getName());
    }

    static BukkitGamer getGamer(final @NotNull String name) {
        return SightWorld.getGamerManager().getGamer(name);
    }

    static BukkitGamer getGamer(final int id) {
        return SightWorld.getGamerManager().getGamer(id);
    }

    Player getPlayer();
    ItemStack getHead();

    ChatColor getPrefixColor();

    void sendMessage(BaseComponent... baseComponents);

    int getVersion();
}
