package cc.dreamcode.platform.bukkit;

import cc.dreamcode.platform.DreamLogger;
import lombok.RequiredArgsConstructor;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class DreamBukkitLogger implements DreamLogger {

    private final Logger logger;

    @Override
    public void info(String text) {
        this.logger.info(text);
    }

    @Override
    public void debug(String text) {
        this.logger.info("[DEBUG] " + text);
    }

    @Override
    public void warning(String text) {
        this.logger.warning(text);
    }

    @Override
    public void error(String text) {
        this.logger.severe(text);
    }

    @Override
    public void error(String text, Throwable throwable) {
        this.logger.log(Level.SEVERE, text, throwable);
    }
}
