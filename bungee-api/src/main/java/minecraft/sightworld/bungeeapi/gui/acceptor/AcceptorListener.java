package minecraft.sightworld.bungeeapi.gui.acceptor;

import lombok.RequiredArgsConstructor;
import minecraft.sightworld.bungeeapi.gui.service.BungeeGuiService;
import minecraft.sightworld.defaultlib.gui.BungeeGuiItem;
import minecraft.sightworld.defaultlib.messaging.BaseMessageListener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@RequiredArgsConstructor
public class AcceptorListener extends BaseMessageListener<ProxiedPlayer, BungeeGuiItem> {

    private final BungeeGuiService bungeeGuiService;

    @Override
    public void onMessage(ProxiedPlayer player, String messageChannel, BungeeGuiItem item) {
        BungeeGuiItem guiItem =  bungeeGuiService.getItem(item.getUuid());
        if (guiItem == null) {
            return;
        }
        guiItem.getOnClick().accept(player.getName(), item.getClickType());
    }
}
