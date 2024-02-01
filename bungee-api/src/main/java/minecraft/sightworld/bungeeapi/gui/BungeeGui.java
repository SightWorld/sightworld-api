package minecraft.sightworld.bungeeapi.gui;

import lombok.Getter;
import lombok.Setter;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.gui.service.BungeeGuiService;
import minecraft.sightworld.defaultlib.gui.BungeeGuiDto;
import minecraft.sightworld.defaultlib.gui.BungeeGuiItem;
import minecraft.sightworld.defaultlib.item.SItem;
import minecraft.sightworld.defaultlib.messaging.MessageService;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

@Getter
@Setter
public abstract class BungeeGui {

    private final MessageService messagingService = SightWorld.getMessagingService();

    private BungeeGuiService guiService = SightWorld.getBungeeGuiService();

    private final ProxiedPlayer player;

    private final String title;

    private final int size;

    protected BungeeGui(ProxiedPlayer player, String title, int size) {
        this.player = player;
        this.title = title;
        this.size = size;
        guiService.clearItems(player);
    }

    private List<Integer> markup = new ArrayList<>();

    private List<BungeeGuiItem> guiItems = new ArrayList<>();

    public void setDefaultMarkupItems() {
        setMarkup(
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34);
    }

    public void setMarkup(Integer... markup) {
        this.markup.clear();
        this.markup.addAll(Arrays.asList(markup));
    }

    public void addItem(SItem sItem, BiConsumer<String, String> onClick) {
        setItem(sItem, onClick, -1);
    }
    public void addItem(BungeeGuiItem item) {
        setItem(item, -1);
    }
    public void setItem(SItem sItem, BiConsumer<String, String> onClick, int slot) {
        setItem(new BungeeGuiItem(sItem, onClick), slot);
    }
    public void setItem(BungeeGuiItem bungeeGuiItem, int slot) {
        bungeeGuiItem.setSlot(slot);
        add(bungeeGuiItem);
    }

    private void add(BungeeGuiItem bungeeGuiItem) {
        String uuid = player.getName() + "-" + UUID.randomUUID();
        bungeeGuiItem.setUuid(uuid);
        bungeeGuiItem.setOwner(player.getName());
        guiItems.add(bungeeGuiItem);
        guiService.addItem(bungeeGuiItem);
    }

    public abstract void draw();

    public void open() {
        BungeeGuiDto defaultGui = new BungeeGuiDto(player.getName(), title, size);
        defaultGui.setItems(guiItems);
        defaultGui.setMarkup(markup);

        messagingService.sendMessage(defaultGui, "gui");
    }
}
