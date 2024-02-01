package minecraft.sightworld.bukkitapi.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface GuiService {

    void addGui(IGui gui);

    void removeGui(IGui gui);

    @Nullable
    IGui getGui(InventoryInteractEvent event);

    @Nullable
    IGui getGui(Player player);

}