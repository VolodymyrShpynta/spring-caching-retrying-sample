package hello;

public class RecoverNeededException extends RuntimeException {

    public RecoverNeededException() {
    }

    public RecoverNeededException(String message) {
        super(message);
    }
}
