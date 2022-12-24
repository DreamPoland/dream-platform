package cc.dreamcode.platform.discord4j.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Discord4JPlatformException extends RuntimeException {

    public Discord4JPlatformException(String text) {
        super(text);
    }

    public Discord4JPlatformException(Throwable throwable) {
        super(throwable);
    }

    public Discord4JPlatformException(String text, Throwable throwable) {
        super(text, throwable);
    }

}
