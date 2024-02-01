package minecraft.sightworld.bungeeapi.gui.acceptor;

import lombok.RequiredArgsConstructor;
import minecraft.sightworld.bungeeapi.gui.service.BungeeGuiService;
import minecraft.sightworld.defaultlib.gui.BungeeGuiItem;
import minecraft.sightworld.defaultlib.messaging.impl.BaseMessageListener;

@RequiredArgsConstructor
public class AcceptorListener extends BaseMessageListener<BungeeGuiItem> {

    private final BungeeGuiService bungeeGuiService;


    @Override
    public void onMessage(CharSequence charSequence, BungeeGuiItem item) {
        BungeeGuiItem guiItem =  bungeeGuiService.getItem(item.getUuid());
        if (guiItem == null) {
            return;
        }
        guiItem.getOnClick().accept(item.getOwner(), item.getClickType());
    }
}
