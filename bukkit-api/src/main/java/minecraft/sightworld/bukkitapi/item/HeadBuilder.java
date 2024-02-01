package minecraft.sightworld.bukkitapi.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class HeadBuilder {

    private ItemStack itemStack;
    private final SkullMeta meta;


    public HeadBuilder(String textureURL) {
        this.itemStack = new ItemStack(Material.PLAYER_HEAD);
        this.meta = (SkullMeta) itemStack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", textureURL));
        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public HeadBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }
    public HeadBuilder setDisplayName(String displayName) {
        meta.setDisplayName(displayName);
        return this;
    }
    public HeadBuilder addFlags(ItemFlag flag) {
        meta.addItemFlags(flag);
        return this;
    }
    public HeadBuilder addEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, false);
        return this;
    }
    public HeadBuilder setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }
    public HeadBuilder addPersistentValue(String key, PersistentDataType persistentDataType, Object value) {
        meta.getPersistentDataContainer().set(NamespacedKey.fromString(key), persistentDataType, value);
        return this;
    }

    public HeadBuilder removePersistentValue(String key) {
        meta.getPersistentDataContainer().remove(NamespacedKey.fromString(key));
        return this;
    }
    public HeadBuilder setCustomModelData(int modelData) {
        meta.setCustomModelData(modelData);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
