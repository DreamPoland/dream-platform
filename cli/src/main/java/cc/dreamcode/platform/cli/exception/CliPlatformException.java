package cc.dreamcode.platform.cli.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CliPlatformException extends RuntimeException {

    public CliPlatformException(String text) {
        super(text);
    }

    public CliPlatformException(Throwable throwable) {
        super(throwable);
    }

    public CliPlatformException(String text, Throwable throwable) {
        super(text, throwable);
    }

}
