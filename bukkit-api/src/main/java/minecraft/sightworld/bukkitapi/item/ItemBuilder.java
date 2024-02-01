package minecraft.sightworld.bukkitapi.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class ItemBuilder {
    private ItemStack itemStack;
    private final ItemMeta meta;
    private final Material material;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.meta = itemStack.getItemMeta();
        this.material = material;
    }
    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }
    public ItemBuilder setDisplayName(String displayName) {
        meta.setDisplayName(displayName);
        return this;
    }
    public ItemBuilder addFlags(ItemFlag flag) {
        meta.addItemFlags(flag);
        return this;
    }
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, false);
        return this;
    }
    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }
    public ItemBuilder addPersistentValue(String key, PersistentDataType persistentDataType, Object value) {
        meta.getPersistentDataContainer().set(NamespacedKey.fromString(key), persistentDataType, value);
        return this;
    }

    public ItemBuilder removePersistentValue(String key) {
        meta.getPersistentDataContainer().remove(NamespacedKey.fromString(key));
        return this;
    }
    public ItemBuilder setCustomModelData(int modelData) {
        meta.setCustomModelData(modelData);
        return this;
    }

    public ItemBuilder setHeadTexture(String texture) {
        if ((material == Material.PLAYER_HEAD) && (texture != null)) {
            UUID hashAsId = new UUID(texture.hashCode(), texture.hashCode());
            this.itemStack = Bukkit.getUnsafe().modifyItemStack(itemStack, "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + texture + "\"}]}}}");
        }
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }


}
