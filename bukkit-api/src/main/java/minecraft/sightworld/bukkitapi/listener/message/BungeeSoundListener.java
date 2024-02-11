package minecraft.sightworld.bukkitapi.listener.message;

import lombok.RequiredArgsConstructor;
import minecraft.sightworld.bukkitapi.gui.GuiItem;
import minecraft.sightworld.bukkitapi.gui.PagedGui;
import minecraft.sightworld.bukkitapi.item.ItemBuilder;
import minecraft.sightworld.bukkitapi.scheduler.BukkitScheduler;
import minecraft.sightworld.defaultlib.gui.BungeeGuiDto;
import minecraft.sightworld.defaultlib.gui.BungeeGuiItem;
import minecraft.sightworld.defaultlib.messaging.MessageService;
import minecraft.sightworld.defaultlib.messaging.impl.BaseMessageListener;
import minecraft.sightworld.defaultlib.sound.SSound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class BungeeSoundListener extends BaseMessageListener<SSound> {

    @Override
    public void onMessage(CharSequence charSequence, SSound sound) {
        Player player = Bukkit.getPlayer(sound.getPlayerName());
        if (player == null) return;

        Sound soundType = Sound.valueOf(sound.getSoundType().name());

        player.playSound(player.getLocation(), soundType, sound.getVolume(), sound.getPitch());
    }
}
