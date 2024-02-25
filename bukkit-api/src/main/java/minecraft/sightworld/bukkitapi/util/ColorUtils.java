package minecraft.sightworld.bukkitapi.util;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitGamer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    @Deprecated
    public static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
    private static final Pattern HEX_PATTERN = Pattern.compile("(&#[0-9a-fA-F]{6})");

    public static String translateColorCodes(@NotNull String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String hex = matcher.group(1).substring(1);
            matcher.appendReplacement(sb, "" + ChatColor.of(hex));
        }
        matcher.appendTail(sb);

        String hexColored = sb.toString();

        return ChatColor.translateAlternateColorCodes('&', hexColored);
    }

    public static TextComponent translateColorCodesToTextComponent(@NotNull String text) {
        String colored = translateColorCodes(text);

        TextComponent base = new TextComponent();
        BaseComponent[] converted = TextComponent.fromLegacyText(colored);

        for (BaseComponent comp : converted) {
            base.addExtra(comp);
        }

        return base;
    }

    /**
     * Фиксит отображение градиента у 1.12 плееров
     * ебанный костыль
     * @param gamer - игрок
     * @param hexText - хекс текст для 1.16.5+ плееров
     * @param defaultText - дефолтный, если он не поддерживается у игрока
     * @return - возвращает итоговую строку
     */
    public static String fixForLegacyPlayers(BukkitGamer gamer, String hexText, String defaultText) {
        return (gamer.getVersion() > ProtocolVersion.v1_16_4.getVersion()) ? translateColorCodes(hexText) : defaultText;
    }
}

