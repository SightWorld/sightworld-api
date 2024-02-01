package minecraft.sightworld.bukkitapi.listener.message;

import lombok.RequiredArgsConstructor;
import minecraft.sightworld.bukkitapi.SightWorld;
import minecraft.sightworld.bukkitapi.gui.GuiItem;
import minecraft.sightworld.bukkitapi.gui.PagedGui;
import minecraft.sightworld.bukkitapi.item.ItemBuilder;
import minecraft.sightworld.bukkitapi.scheduler.BukkitScheduler;
import minecraft.sightworld.defaultlib.gui.BungeeGuiDto;
import minecraft.sightworld.defaultlib.gui.BungeeGuiItem;
import minecraft.sightworld.defaultlib.messaging.MessageService;
import minecraft.sightworld.defaultlib.messaging.impl.BaseMessageListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

@RequiredArgsConstructor
public class BungeeGuiListener extends BaseMessageListener<BungeeGuiDto> {

    private final MessageService messagingService;


    @Override
    public void onMessage(CharSequence charSequence, BungeeGuiDto gui) {
        Player player = Bukkit.getPlayer(gui.getOwner());
        if (player == null) return;
        BukkitScheduler.runTask(() -> {
            PagedGui pagedGui = new PagedGui(player, gui.getSize(), gui.getTitle()) {
                @Override
                public void draw() {
                    for (BungeeGuiItem item : gui.getItems()) {
                        ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(item.getItem().getMaterial().name().toUpperCase()))
                                .setDisplayName(item.getItem().getTitle())
                                .setAmount(item.getItem().getAmount())
                                .setLore(item.getItem().getLore())
                                .setHeadTexture(item.getItem().getHeadTexture());
                        if (item.getItem().isGlow()) {
                            itemBuilder.addEnchantment(Enchantment.PROTECTION_FALL, 1);
                            itemBuilder.addFlags(ItemFlag.HIDE_ENCHANTS);
                        }
                        GuiItem guiItem = new GuiItem(itemBuilder.build(), (player1, clickType) -> {
                            item.setClickType(clickType.name());
                            messagingService.sendMessage(item, "gui-acceptor");
                            System.out.println(item.isAutoClosable());
                            if (item.isAutoClosable()) {
                                System.out.println("Итем из аутоклозейбл");
                                player.closeInventory();
                            }
                        });
                        if (item.getSlot() != -1) {
                            setButton(item.getSlot(), guiItem);
                        } else {
                            addItem(guiItem);
                        }
                    }
                }
            };
            pagedGui.setMarkup(gui.getMarkup());
            pagedGui.setUpdater(20);
            pagedGui.draw();
            pagedGui.open();
        });
    }
}
