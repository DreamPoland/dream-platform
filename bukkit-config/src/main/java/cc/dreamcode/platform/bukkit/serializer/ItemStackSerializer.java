package cc.dreamcode.platform.bukkit.serializer;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import eu.okaeri.configs.yaml.bukkit.serdes.itemstack.ItemStackFormat;
import eu.okaeri.configs.yaml.bukkit.serdes.itemstack.ItemStackSpecData;
import eu.okaeri.configs.yaml.bukkit.serdes.serializer.experimental.CraftItemStackSerializer;
import eu.okaeri.configs.yaml.bukkit.serdes.transformer.experimental.StringBase64ItemStackTransformer;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.atomic.AtomicReference;

public class ItemStackSerializer implements ObjectSerializer<ItemStack> {

    private static final ItemMetaSerializer ITEM_META_SERIALIZER = new ItemMetaSerializer();
    private static final StringBase64ItemStackTransformer ITEM_STACK_TRANSFORMER = new StringBase64ItemStackTransformer();
    private static final CraftItemStackSerializer CRAFT_ITEM_STACK_SERIALIZER = new CraftItemStackSerializer(true);

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

        DeserializationData deserializationData = new DeserializationData(data.asMap(), data.getConfigurer(), data.getContext());
        ItemStack deserializedStack = this.deserialize(deserializationData, generics);

        if (deserializedStack.equals(itemStack) || CraftItemStackSerializer.compareDeep(deserializedStack, itemStack)) {
            return;
        }

        data.clear();

        try {
            CRAFT_ITEM_STACK_SERIALIZER.serialize(itemStack, data, generics);

            DeserializationData deserializationCraftData = new DeserializationData(data.asMap(), data.getConfigurer(), data.getContext());
            ItemStack deserializedCraftStack = CRAFT_ITEM_STACK_SERIALIZER.deserialize(deserializationCraftData, generics);

            if (deserializedCraftStack.equals(itemStack) || CraftItemStackSerializer.compareDeep(deserializedCraftStack, itemStack)) {
                return;
            }
        }
        catch (Throwable ignored) {}

        data.clear();

        String base64Stack = ITEM_STACK_TRANSFORMER.leftToRight(itemStack, data.getContext());
        data.add("base64", base64Stack);
    }

    @Override
    public ItemStack deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {

        if (data.getValueRaw() instanceof ItemStack) {
            return (ItemStack) data.getValueRaw();
        }

        if (data.containsKey("legacy") ||
                "org.bukkit.inventory.ItemStack".equals(data.get("==", String.class)) ||
                (data.containsKey("v") && data.containsKey("type"))) {
            return CRAFT_ITEM_STACK_SERIALIZER.deserialize(data, generics);
        }

        if (data.containsKey("base64")) {
            String base64Stack = data.get("base64", String.class);
            return ITEM_STACK_TRANSFORMER.rightToLeft(base64Stack, data.getContext());
        }

        if (!data.containsKey("material")) {
            throw new IllegalArgumentException("invalid stack: " + data.asMap());
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
                if (data.containsKey("display-name")) {
                    itemMeta = ITEM_META_SERIALIZER.deserialize(data, generics);
                }
                else {
                    itemMeta = data.containsKey("item-meta")
                            ? data.get("item-meta", ItemMeta.class)
                            : null;
                }
                break;
            case COMPACT:
                if (data.containsKey("item-meta")) {
                    itemMeta = data.get("item-meta", ItemMeta.class);
                }
                else {
                    itemMeta = ITEM_META_SERIALIZER.deserialize(data, generics);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown format: " + format);
        }

        AtomicReference<ItemStack> itemStack = new AtomicReference<>(new ItemStack(material, amount));
        itemStack.get().setItemMeta(itemMeta);
        itemStack.get().setDurability(durability);

        return itemStack.get();
    }
}
