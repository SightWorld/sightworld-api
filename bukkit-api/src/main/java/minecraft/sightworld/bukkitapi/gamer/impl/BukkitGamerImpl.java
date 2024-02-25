package minecraft.sightworld.bukkitapi.gamer.impl;

import com.viaversion.viaversion.api.Via;
import lombok.Getter;
import lombok.Setter;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitGamer;
import minecraft.sightworld.bukkitapi.gamer.event.AsyncGamerLoadSectionEvent;
import minecraft.sightworld.bukkitapi.scheduler.BukkitScheduler;
import minecraft.sightworld.defaultlib.gamer.GamerBase;
import minecraft.sightworld.defaultlib.gamer.section.Section;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.inventory.ItemStack;

import java.net.InetAddress;
import java.util.Set;

public class BukkitGamerImpl extends GamerBase implements BukkitGamer {

    @Getter
    final InetAddress ip;

    @Setter
    Player player;
    ItemStack head;

    public BukkitGamerImpl(AsyncPlayerPreLoginEvent e) {
        super(e.getName());
        this.ip = e.getAddress();
    }

    @Override
    public CommandSender getCommandSender() {
        return this.getPlayer();
    }

    @Override
    public String getChatName() {
        return getPrefix() + getName();
    }

    @Override
    public boolean isHuman() {
        return true;
    }

    @Override
    public Player getPlayer() {
        if (player == null) {
            return (player = Bukkit.getPlayer(this.name));
        }

        return player;
    }

    @Override
    public ItemStack getHead() {
        return head;
    }

    @Override
    public ChatColor getPrefixColor() {
        try {
            return ChatColor.getByChar(getPrefix().charAt(1));
        } catch (Exception ignored) {}

        return ChatColor.GRAY;
    }

    @Override
    public void sendMessage(BaseComponent... baseComponents) {
        if (player == null) {
            return;
        }

        player.spigot().sendMessage(baseComponents);
    }

    @Override
    public int getVersion() {
        return Via.getAPI().getPlayerVersion(getPlayer().getUniqueId());
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public void sendTitle(String title, String subTitle) {
        sendTitle(title, subTitle, 20, 40, 20);
    }

    @Override
    public void sendTitle(String title, String subTitle, long fadeInTime, long stayTime, long fadeOutTime) {
        player.sendTitle(title, subTitle, (int) fadeInTime, (int) stayTime, (int) fadeOutTime);
    }

    @Override
    public void sendActionBar(String msg) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
    }

    @Override
    protected Set<Class<? extends Section>> initSections() {
        AsyncGamerLoadSectionEvent event = new AsyncGamerLoadSectionEvent(this);
        BukkitScheduler.callEvent(event);
        return event.getSections();
    }




    @Override
    public boolean hasPermission(String permission) {
        return getPlayer().hasPermission(permission);
    }
}
