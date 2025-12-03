package cc.dreamcode.project.menu;

import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ExampleMenuHolder {

    private final ExampleMenu exampleMenu;
    private BukkitMenu menu;

    public void update() {
        if (this.menu != null) {
            new ArrayList<>(this.menu.getInventory().getViewers()).forEach(HumanEntity::closeInventory);
        }

        this.menu = this.exampleMenu.build();
    }

    public void open(HumanEntity player) {
        if (this.menu == null) {
            this.update();
        }

        this.menu.open(player);
    }
}
