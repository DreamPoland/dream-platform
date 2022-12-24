package cc.dreamcode.platform.discord4j;

import cc.dreamcode.platform.DreamLogger;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor
public class DreamDiscord4JLogger implements DreamLogger {

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
        this.logger.warn(text);
    }

    @Override
    public void error(String text) {
        this.logger.error(text);
    }

    @Override
    public void error(String text, Throwable throwable) {
        this.logger.error(text, throwable);
    }
}
