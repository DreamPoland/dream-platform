package cc.dreamcode.platform.minestom.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MinestomExtensionException extends RuntimeException {

    public MinestomExtensionException(String text) {
        super(text);
    }

    public MinestomExtensionException(Throwable throwable) {
        super(throwable);
    }

    public MinestomExtensionException(String text, Throwable throwable) {
        super(text, throwable);
    }

}
