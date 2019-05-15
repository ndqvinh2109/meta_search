package sg.com.wego.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static sg.com.wego.util.ErrorMessageBundle.byStatus;

@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String error = "Malformed JSON request";
        return byStatus(HttpStatus.BAD_REQUEST).withError(error).whichException(ex).buildMessage().thenResponseEntity();
    }

    @ExceptionHandler({FareFlightException.class})
    public ResponseEntity<Object> handleMetaSearchParamNotValid(FareFlightException ex) {
        String error = "Param is not valid";
        return byStatus(HttpStatus.BAD_REQUEST).withError(error).whichException(ex).buildMessage().thenResponseEntity();
    }

}
