package sg.com.wego.validator;

public interface FareFlightSearchValidator<T> {
    boolean isDepartureAndArrivalCodeSupported(T t);
    boolean isDepartureAndArriValNotSame(T t);
    boolean isDepartureAndArrivalExistsOnSchudules(T t);
}
