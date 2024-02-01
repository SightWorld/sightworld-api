package minecraft.sightworld.bukkitapi.gamer.entity;

import minecraft.sightworld.bukkitapi.gamer.entity.BukkitEntity;
import minecraft.sightworld.defaultlib.gamer.IBaseGamer;
import minecraft.sightworld.defaultlib.gamer.OnlineGamer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BukkitGamer extends BukkitEntity, OnlineGamer, IBaseGamer {
    Player getPlayer();
    ItemStack getHead();

    ChatColor getPrefixColor();

    void sendMessage(BaseComponent... baseComponents);


}
