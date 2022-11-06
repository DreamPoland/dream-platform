package cc.dreamcode.platform.bukkit.exception;

import lombok.NoArgsConstructor;
import org.bukkit.plugin.Plugin;

@NoArgsConstructor
public class BukkitPluginException extends RuntimeException {

    public BukkitPluginException(String text) {
        super(text);
    }

    public BukkitPluginException(Throwable throwable) {
        super(throwable);
    }

    public BukkitPluginException(String text, Throwable throwable) {
        super(text, throwable);
    }

    public BukkitPluginException(String text, Plugin pluginToDisable) {
        super(text);
        pluginToDisable.getServer().getPluginManager().disablePlugin(pluginToDisable);
    }

    public BukkitPluginException(Throwable throwable, Plugin pluginToDisable) {
        super(throwable);
        pluginToDisable.getServer().getPluginManager().disablePlugin(pluginToDisable);
    }

    public BukkitPluginException(String text, Throwable throwable, Plugin pluginToDisable) {
        super(text, throwable);
        pluginToDisable.getServer().getPluginManager().disablePlugin(pluginToDisable);
    }

}
