package cc.dreamcode.platform.bungee.exception;

import lombok.NoArgsConstructor;
import net.md_5.bungee.api.plugin.Plugin;

@NoArgsConstructor
public class BungeePluginException extends RuntimeException {

    public BungeePluginException(String text) {
        super(text);
    }

    public BungeePluginException(Throwable throwable) {
        super(throwable);
    }

    public BungeePluginException(String text, Throwable throwable) {
        super(text, throwable);
    }

    public BungeePluginException(String text, Plugin pluginToDisable) {
        super(text);
        pluginToDisable.getProxy().getPluginManager().unregisterCommands(pluginToDisable);
        pluginToDisable.getProxy().getPluginManager().unregisterListeners(pluginToDisable);
    }

    public BungeePluginException(Throwable throwable, Plugin pluginToDisable) {
        super(throwable);
        pluginToDisable.getProxy().getPluginManager().unregisterCommands(pluginToDisable);
        pluginToDisable.getProxy().getPluginManager().unregisterListeners(pluginToDisable);
    }

    public BungeePluginException(String text, Throwable throwable, Plugin pluginToDisable) {
        super(text, throwable);
        pluginToDisable.getProxy().getPluginManager().unregisterCommands(pluginToDisable);
        pluginToDisable.getProxy().getPluginManager().unregisterListeners(pluginToDisable);
    }

}
