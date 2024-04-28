package cc.dreamcode.platform.bukkit.serializer;

import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.utilities.bukkit.nbt.ItemNbtUtil;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import eu.okaeri.configs.yaml.bukkit.serdes.itemstack.ItemStackFormat;
import eu.okaeri.configs.yaml.bukkit.serdes.itemstack.ItemStackSpecData;
import eu.okaeri.configs.yaml.bukkit.serdes.transformer.experimental.StringBase64ItemStackTransformer;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ItemStackSerializer implements ObjectSerializer<ItemStack> {

    private final DreamBukkitPlatform dreamPlatform;

    private static final ItemMetaSerializer ITEM_META_SERIALIZER = new ItemMetaSerializer();
    private static final StringBase64ItemStackTransformer ITEM_STACK_TRANSFORMER = new StringBase64ItemStackTransformer();

    @Inject
    public ItemStackSerializer(DreamBukkitPlatform dreamPlatform) {
        this.dreamPlatform = dreamPlatform;
    }

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

        Map<String, String> nbtMap = ItemNbtUtil.getValuesByPlugin(this.dreamPlatform, itemStack);
        if (!nbtMap.isEmpty()) {
            data.addAsMap("nbt", nbtMap, String.class, String.class);
        }

        ItemStackFormat format = data.getContext().getAttachment(ItemStackSpecData.class)
                .map(ItemStackSpecData::getFormat)
                .orElse(ItemStackFormat.NATURAL);

        if (!itemStack.hasItemMeta()) {
            return;
        }

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

        // human-friendly form is most likely complete
        if (deserializedStack.equals(itemStack)) {
            return;
        }

        // human-friendly failed, use base64 instead
        data.clear();
        String base64Stack = ITEM_STACK_TRANSFORMER.leftToRight(itemStack, data.getContext());
        data.add("base64", base64Stack);
    }

    @Override
    public ItemStack deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {

        // base64
        if (data.containsKey("base64")) {
            String base64Stack = data.get("base64", String.class);
            return ITEM_STACK_TRANSFORMER.rightToLeft(base64Stack, data.getContext());
        }

        // human-friendly
        String materialName = data.get("material", String.class);
        Material material = Material.valueOf(materialName);

        int amount = data.containsKey("amount")
                ? data.get("amount", Integer.class)
                : 1;

        short durability = data.containsKey("durability")
                ? data.get("durability", Short.class)
                : 0;

        Map<String, String> nbt = data.containsKey("nbt")
                ? data.getAsMap("nbt", String.class, String.class)
                : new HashMap<>();

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

        nbt.forEach((key, value) ->
                itemStack.set(ItemNbtUtil.setValue(this.dreamPlatform, itemStack.get(), key, value)));

        // woah, it works
        return itemStack.get();
    }
}
