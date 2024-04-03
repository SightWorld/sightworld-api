package minecraft.sightworld.bungeeapi.whitelist;

import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class WhitelistManager { // cringe

    boolean enable;
    Configuration CONFIG;

    final Collection<String> playerNames = new ArrayList<>();

    public void load() {
        CONFIG = loadConfig();

        enable = CONFIG.getBoolean("whitelist.ENABLE");

        playerNames.clear();
        playerNames.addAll(CONFIG.getStringList("whitelist.PLAYERS"));
    }

    public @NotNull String getKickMessage() {
        return Joiner.on("\n").join(CONFIG.getStringList("whitelist.KICK_MESSAGE"));
    }

    public @NotNull String getMotd() {
        return Joiner.on("\n").join(CONFIG.getStringList("whitelist.MOTD"));
    }

    public @NotNull String getHoverText() {
        return Joiner.on("\n").join(CONFIG.getStringList("whitelist.HOVER"));
    }

    public void setEnable(boolean enable) {
        this.enable = enable;

        CONFIG.set("whitelist.ENABLE", enable);
        saveConfig();
    }

    public void addPlayer(@NotNull String playerName) {
        playerNames.add(playerName.toLowerCase());
        CONFIG.set("whitelist.PLAYERS", playerNames);

        saveConfig();
    }

    public void removePlayer(@NotNull String playerName) {
        playerNames.remove(playerName.toLowerCase());
        CONFIG.set("whitelist.PLAYERS", playerNames);

        saveConfig();
    }


    Configuration loadConfig() {
        val configFile = new File(SightWorld.getInstance().getDataFolder(), "whitelist.yml");
        Configuration configuration;

        try {
            if (!configFile.exists()) {
                FileOutputStream outputStream = new FileOutputStream(configFile);
                InputStream in = SightWorld.getInstance().getResourceAsStream("whitelist.yml");
                in.transferTo(outputStream);
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            } else {
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return configuration;
    }


    void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(CONFIG,
                    new File(SightWorld.getInstance().getDataFolder(), "whitelist.yml"));

        } catch (IOException e) {
            SightWorld.getInstance().getLogger().severe("Error to save whitelist.yml - ");
            e.printStackTrace();
        }
    }
}