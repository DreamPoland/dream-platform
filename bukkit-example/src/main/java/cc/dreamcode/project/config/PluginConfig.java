package cc.dreamcode.project.config;

import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import cc.dreamcode.menu.utilities.MenuUtil;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.platform.persistence.StorageConfig;
import cc.dreamcode.project.menu.MenuItem;
import cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import com.cryptomorin.xseries.XMaterial;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.CustomKey;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Configuration(child = "config.yml")
public class PluginConfig extends OkaeriConfig {

    @CustomKey("storage-config")
    public StorageConfig storageConfig = new StorageConfig("dreamtemplate");

    @CustomKey("menu-builder")
    public BukkitMenuBuilder menuBuilder = new BukkitMenuBuilder("Menu title", 3, new MapBuilder<Integer, ItemStack>()
            .put(MenuUtil.countSlot(3, 5), ItemBuilder.of(XMaterial.RED_DYE.parseItem())
                    .setName("&cClose menu")
                    .toItemStack())
            .build());

    @CustomKey("menu-close-item-slot")
    public int menuCloseItemSlot = MenuUtil.countSlot(3, 5);

    @CustomKey("menu-custom-items")
    public List<MenuItem> menuItemList = new ListBuilder<MenuItem>()
            .add(new MenuItem(
                    "Test item",
                    XMaterial.DIAMOND.parseItem(),
                    MenuUtil.countSlot(1, 1)
            ))
            .build();
}
