package extentions;

public class UnitTestException extends RuntimeException {

    public UnitTestException(String message) {
        super(message);
    }

    public UnitTestException(String message, Throwable cause) {
        super(message, cause);
    }
}


