package sg.com.wego.service;

public interface MetaSearchValidator<T> {
    boolean isDepartureAndArrivalCodeSupported(T t);
    boolean isDepartureAndArriValNotSame(T t);
    boolean isDepartureAndArrivalExistsOnSchudules(T t);
}
