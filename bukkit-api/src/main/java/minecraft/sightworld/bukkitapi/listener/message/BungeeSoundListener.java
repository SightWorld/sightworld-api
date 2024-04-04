package minecraft.sightworld.bukkitapi.listener.message;

import minecraft.sightworld.defaultlib.messaging.impl.BaseMessageListener;
import minecraft.sightworld.defaultlib.sound.BungeeSoundDto;
import minecraft.sightworld.defaultlib.sound.SSound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class BungeeSoundListener extends BaseMessageListener<BungeeSoundDto> {

    @Override
    public void onMessage(CharSequence charSequence, BungeeSoundDto dto) {
        Player player = Bukkit.getPlayer(dto.getName());
        SSound sound = dto.getSound();
        if (player == null) return;

        Sound soundType = Sound.valueOf(sound.getSoundType().name());

        player.playSound(player.getLocation(), soundType, sound.getVolume(), sound.getPitch());
    }
}
