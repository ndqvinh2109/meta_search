package sg.com.wego.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sg.com.wego.exception.FareFlightException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;


public class Validator<T> {

    private static final Logger logger = LogManager.getLogger(Validator.class);

    private final T t;
    private final List<Throwable> exceptions = new ArrayList<>();


    private Validator(T t) {
        this.t = t;
    }

    public static <T> Validator<T> of(T t) {
        return new Validator<>(Objects.requireNonNull(t));
    }


    public Validator<T> validate(Predicate<T> validation, String message) {
        if (!validation.test(t)) {
            logger.info("Exception message: " + message);
            exceptions.add(new FareFlightException(message));
        }
        return this;
    }

     public T get() throws FareFlightException {
        if (exceptions.isEmpty()) {
            return t;
        }
        FareFlightException e = new FareFlightException();
        exceptions.forEach(e::addSuppressed);
        throw e;
    }
}
