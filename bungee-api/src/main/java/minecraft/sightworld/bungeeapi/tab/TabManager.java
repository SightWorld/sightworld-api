package minecraft.sightworld.bungeeapi.tab;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.util.ChatUtil;
import minecraft.sightworld.defaultlib.localization.Language;
import minecraft.sightworld.defaultlib.localization.LocalizationService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TabManager {

    ScheduledTask task;


    public void load(LocalizationService localizationService) {
        if (task != null) task.cancel();

        List<String> header = localizationService.getList(Language.RUSSIAN, "tab_header");
        List<String> footer = localizationService.getList(Language.RUSSIAN, "tab_footer");
        startTabTask(header, footer);
    }

    void startTabTask(List<String> header, List<String> footer) {
        task = ProxyServer.getInstance().getScheduler().schedule(SightWorld.getInstance(), () -> ProxyServer.getInstance().getPlayers()
                .forEach(player -> {
                    String server = player.getServer() == null ? "" : player.getServer().getInfo().getName();
                    int globalOnline = ProxyServer.getInstance().getOnlineCount();


                    player.setTabHeader(getTabComponent(header, server, globalOnline),
                            getTabComponent(footer, server, globalOnline));
                }), 0L, 2L, TimeUnit.SECONDS);
    }
    BaseComponent getTabComponent(List<String> inputList, String serverName, int globalOnlineCount) {
        return ChatUtil.getComponentFromList(replacePlaceholdersInList(inputList, serverName, globalOnlineCount));
    }

    List<String> replacePlaceholdersInList(List<String> inputList, String serverName, int globalOnlineCount) {
        return inputList.stream()
                .map(str -> str.replace("%server%", serverName)
                        .replace("%global_online%", String.valueOf(globalOnlineCount)))
                .collect(Collectors.toList());
    }
}
