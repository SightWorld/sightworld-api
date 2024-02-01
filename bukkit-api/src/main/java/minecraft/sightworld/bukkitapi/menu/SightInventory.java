package minecraft.sightworld.bukkitapi.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class SightInventory implements InventoryHolder { // TODO: Дописать

    private final Inventory inventory;

    public SightInventory(String title, int rows) {
        this.inventory = Bukkit.createInventory(this, rows*9, title);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    protected abstract void onClickInventory(InventoryClickEvent e);
}
