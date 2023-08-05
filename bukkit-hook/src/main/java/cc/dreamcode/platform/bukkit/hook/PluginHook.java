package cc.dreamcode.platform.bukkit.hook;

import cc.dreamcode.platform.hook.DreamHook;
import org.bukkit.Bukkit;

public interface PluginHook extends DreamHook {

    default boolean isPresent()  {
        return Bukkit.getPluginManager().getPlugin(this.getPluginName()) != null;
    }

}
