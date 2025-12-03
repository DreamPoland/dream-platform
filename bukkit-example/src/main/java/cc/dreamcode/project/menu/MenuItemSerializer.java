package cc.dreamcode.project.menu;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

public class MenuItemSerializer implements ObjectSerializer<MenuItem> {
    @Override
    public boolean supports(@NonNull Class<? super MenuItem> type) {
        return MenuItem.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull MenuItem object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("display-name", object.getDisplayName());
        data.add("item", object.getItemStack());
        data.add("slot-in-menu", object.getSlotInMenu());
    }

    @Override
    public MenuItem deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new MenuItem(
                data.get("display-name", String.class),
                data.get("item", ItemStack.class),
                data.get("slot-in-menu", Integer.class)
        );
    }
}
