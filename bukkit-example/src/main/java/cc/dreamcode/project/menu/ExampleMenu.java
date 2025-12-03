package cc.dreamcode.project.menu;

import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.menu.bukkit.setup.BukkitMenuSetup;
import cc.dreamcode.project.config.PluginConfig;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.HumanEntity;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ExampleMenu implements BukkitMenuSetup {

    private final PluginConfig pluginConfig;

    @Override
    public BukkitMenu build() {
        // 1. Build base layout from config
        final BukkitMenuBuilder menuBuilder = this.pluginConfig.menuBuilder;
        final BukkitMenu bukkitMenu = menuBuilder.buildEmpty();

        // 2. Set static items from builder (with logic)
        menuBuilder.getItems().forEach((slot, item) -> {
            if (this.pluginConfig.menuCloseItemSlot == slot) {
                bukkitMenu.setItem(slot, ItemBuilder.of(item).fixColors().toItemStack(), event ->
                        event.getWhoClicked().closeInventory());
                return;
            }
            bukkitMenu.setItem(slot, ItemBuilder.of(item).fixColors().toItemStack());
        });

        // 3. Set custom serialized items
        this.pluginConfig.menuItemList.forEach(menuItem ->
                bukkitMenu.setItem(menuItem.getSlotInMenu(), ItemBuilder.of(menuItem.getItemStack())
                        .fixColors()
                        .toItemStack(), event -> {
                    final HumanEntity player = event.getWhoClicked();
                    player.sendMessage("Clicked " + menuItem.getDisplayName());
                }));

        return bukkitMenu;
    }
}
