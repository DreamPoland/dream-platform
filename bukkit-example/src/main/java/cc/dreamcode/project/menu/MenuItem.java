package cc.dreamcode.project.menu;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public class MenuItem {

    private final String displayName;
    private final ItemStack itemStack;
    private final int slotInMenu;
}
