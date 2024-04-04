package minecraft.sightworld.bukkitapi.gamer.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.defaultlib.gamer.GamerAPI;
import minecraft.sightworld.defaultlib.localization.Language;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class BukkitServer implements BukkitEntity {

    SightWorld mainPlugin;
    String name;

    @Override
    public CommandSender getCommandSender() {
        return mainPlugin.getServer().getConsoleSender();
    }

    @Override
    public void sendMessage(String message) {
        getCommandSender().sendMessage(message);
    }

    @Override
    public void sendMessages(List<String> messages) {
        messages.forEach(this::sendMessage);
    }

    @Override
    public void sendMessageLocale(String key, Object... args) {
        sendMessage(SightWorld.getLocalizationService().get(Language.RUSSIAN, key, args));
    }

    @Override
    public String getChatName() {
        return "console-" + name;
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    public void broadcast(String message) {
        sendMessage(message);
        GamerAPI.getGamers().values().forEach(gamer -> gamer.sendMessage(message));
    }

}
