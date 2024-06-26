package cc.dreamcode.platform.bukkit.serializer;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemMetaSerializer implements ObjectSerializer<ItemMeta> {

    @Override
    public boolean supports(@NonNull Class<? super ItemMeta> type) {
        return ItemMeta.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull ItemMeta itemMeta, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {

        if (itemMeta.hasDisplayName()) {
            data.add("display-name", itemMeta.getDisplayName());
        }

        if (itemMeta.hasLore()) {
            data.addCollection("lore", itemMeta.getLore(), String.class);
        }

        if (!itemMeta.getEnchants().isEmpty()) {
            data.addAsMap("enchantments", itemMeta.getEnchants(), Enchantment.class, Integer.class);
        }

        if (!itemMeta.getItemFlags().isEmpty()) {
            data.addCollection("item-flags", itemMeta.getItemFlags(), ItemFlag.class);
        }

        try {
            Method methodHasCustomModelData = ItemMeta.class.getMethod("hasCustomModelData");
            boolean hasCustomModelData = (boolean) methodHasCustomModelData.invoke(itemMeta);
            if (hasCustomModelData) {
                Method methodGetCustomModelData =  ItemMeta.class.getMethod("getCustomModelData");
                int getCustomModelData = (int) methodGetCustomModelData.invoke(itemMeta);
                data.add("model-id", getCustomModelData, Integer.class);
            }
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
    }

    @Override
    public ItemMeta deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {

        String displayName = data.get("display-name", String.class);

        List<String> lore = data.containsKey("lore")
                ? data.getAsList("lore", String.class)
                : Collections.emptyList();

        Map<Enchantment, Integer> enchantments = data.containsKey("enchantments")
                ? data.getAsMap("enchantments", Enchantment.class, Integer.class)
                : Collections.emptyMap();

        List<ItemFlag> itemFlags = data.containsKey("item-flags")
                ? data.getAsList("item-flags", ItemFlag.class)
                : Collections.emptyList();

        ItemMeta itemMeta = new ItemStack(Material.COBBLESTONE).getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot extract empty ItemMeta from COBBLESTONE");
        }

        if (displayName != null) {
            itemMeta.setDisplayName(displayName);
        }

        itemMeta.setLore(lore);

        enchantments.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, level, true));
        itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));

        if (data.containsKey("model-id")) {
            try {
                int customModelData = data.get("model-id", Integer.class);
                Method method = ItemMeta.class.getMethod("setCustomModelData", Integer.class);
                method.invoke(itemMeta, customModelData);
            }
            catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
        }

        return itemMeta;
    }
}
