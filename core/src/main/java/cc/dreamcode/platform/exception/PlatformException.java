package cc.dreamcode.platform.exception;

public class PlatformException extends RuntimeException {

    public PlatformException(String text) {
        super(text);
    }

    public PlatformException(Throwable throwable) {
        super(throwable);
    }

    public PlatformException(String text, Throwable throwable) {
        super(text, throwable);
    }

}
