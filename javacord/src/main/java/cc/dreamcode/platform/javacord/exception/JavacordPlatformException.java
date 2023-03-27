package cc.dreamcode.platform.javacord.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JavacordPlatformException extends RuntimeException {

    public JavacordPlatformException(String text) {
        super(text);
    }

    public JavacordPlatformException(Throwable throwable) {
        super(throwable);
    }

    public JavacordPlatformException(String text, Throwable throwable) {
        super(text, throwable);
    }

}
