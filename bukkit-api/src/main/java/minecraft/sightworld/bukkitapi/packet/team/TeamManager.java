package minecraft.sightworld.bukkitapi.packet.team;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.Getter;
import minecraft.sightworld.bukkitapi.manager.LuckPermsManager;
import minecraft.sightworld.bukkitapi.user.BukkitUser;
import minecraft.sightworld.defaultlib.utils.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class TeamManager { // гавнокод но мне пахую я панк

    @Getter
    private static final List<WrapperPlayServerScoreboardTeam> teams = new ArrayList<>();

    public static WrapperPlayServerScoreboardTeam getTeam(int mode, BukkitUser gamer) {
        String prefix = gamer.getUserData().getPrefix();
        String name = gamer.getName();
        String tag = gamer.getTag();

        WrapperPlayServerScoreboardTeam team = new WrapperPlayServerScoreboardTeam();
        team.setName(LuckPermsManager.getTabPriority(name) + name);
        team.setMode(mode);

        team.setPrefix(WrappedChatComponent.fromText(prefix != null ? StringUtils.fixLength(16, prefix) : ""));
        team.setSuffix(WrappedChatComponent.fromText(tag != null ? StringUtils.fixLength(16, " " + tag) : ""));

        team.setNameTagVisibility("ALWAYS");
        team.setColor(prefix != null ? ChatColor.getByChar(prefix.replace(" ", "").charAt(1)) : ChatColor.WHITE);
        team.setPackOptionData(1);
        team.getPlayers().add(gamer.getName());

        return team;
    }
}