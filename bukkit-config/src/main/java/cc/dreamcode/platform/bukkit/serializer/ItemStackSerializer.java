package cc.dreamcode.platform.bukkit.serializer;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import eu.okaeri.configs.yaml.bukkit.serdes.itemstack.ItemStackFormat;
import eu.okaeri.configs.yaml.bukkit.serdes.itemstack.ItemStackSpecData;
import eu.okaeri.configs.yaml.bukkit.serdes.serializer.experimental.CraftItemStackSerializer;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.atomic.AtomicReference;

public class ItemStackSerializer implements ObjectSerializer<ItemStack> {

    private static final ItemMetaSerializer ITEM_META_SERIALIZER = new ItemMetaSerializer();
    private static final CraftItemStackSerializer CRAFT_ITEM_STACK_SERIALIZER = new CraftItemStackSerializer();

    @Override
    public boolean supports(@NonNull Class<? super ItemStack> type) {
        return ItemStack.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull ItemStack itemStack, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {

        data.add("material", itemStack.getType());

        if (itemStack.getAmount() != 1) {
            data.add("amount", itemStack.getAmount());
        }

        if (itemStack.getDurability() != 0) {
            data.add("durability", itemStack.getDurability());
        }

        if (!itemStack.hasItemMeta()) {
            return;
        }

        ItemStackFormat format = data.getContext().getAttachment(ItemStackSpecData.class)
                .map(ItemStackSpecData::getFormat)
                .orElse(ItemStackFormat.NATURAL);

        switch (format) {
            case NATURAL:
                data.add("item-meta", itemStack.getItemMeta(), ItemMeta.class);
                break;
            case COMPACT:
                ITEM_META_SERIALIZER.serialize(itemStack.getItemMeta(), data, generics);
                break;
            default:
                throw new IllegalArgumentException("Unknown format: " + format);
        }

        // check if serialized stack is deserializable
        DeserializationData deserializationData = new DeserializationData(data.asMap(), data.getConfigurer(), data.getContext());
        ItemStack deserializedStack = this.deserialize(deserializationData, generics);

        // normal form is most likely complete
        if (deserializedStack.equals(itemStack)) {
            return;
        }

        // use legacy instead
        data.clear();
        data.add("legacy", true);
        CRAFT_ITEM_STACK_SERIALIZER.serialize(itemStack, data, generics);
    }

    @Override
    public ItemStack deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {

        // legacy
        if (data.containsKey("legacy")) {
            return CRAFT_ITEM_STACK_SERIALIZER.deserialize(data, generics);
        }

        String materialName = data.get("material", String.class);
        Material material = Material.valueOf(materialName);

        int amount = data.containsKey("amount")
                ? data.get("amount", Integer.class)
                : 1;

        short durability = data.containsKey("durability")
                ? data.get("durability", Short.class)
                : 0;

        ItemStackFormat format = data.getContext().getAttachment(ItemStackSpecData.class)
                .map(ItemStackSpecData::getFormat)
                .orElse(ItemStackFormat.NATURAL);

        ItemMeta itemMeta;
        switch (format) {
            case NATURAL:
                // support conversion COMPACT->NATURAL
                if (data.containsKey("display-name")) {
                    itemMeta = ITEM_META_SERIALIZER.deserialize(data, generics);
                }
                // standard deserialize
                else {
                    itemMeta = data.containsKey("item-meta")
                            ? data.get("item-meta", ItemMeta.class)
                            : null;
                }
                break;
            case COMPACT:
                // support conversion NATURAL->COMPACT
                if (data.containsKey("item-meta")) {
                    itemMeta = data.get("item-meta", ItemMeta.class);
                }
                // standard deserialize
                else {
                    itemMeta = ITEM_META_SERIALIZER.deserialize(data, generics);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown format: " + format);
        }

        // create ItemStack base
        AtomicReference<ItemStack> itemStack = new AtomicReference<>(new ItemStack(material, amount));
        // set ItemMeta FIRST due to 1.16+ server
        // ItemStacks storing more and more data
        // here, in the attributes of ItemMeta
        itemStack.get().setItemMeta(itemMeta);
        // then override durability with setter
        itemStack.get().setDurability(durability);

        // woah, it works
        return itemStack.get();
    }
}
