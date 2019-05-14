package sg.com.wego.exception;

public class FareFlightException extends RuntimeException {

    public FareFlightException() {
        super();
    }

    public FareFlightException(String message) {
       super(message);
    }
}
