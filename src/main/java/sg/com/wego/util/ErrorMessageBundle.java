package sg.com.wego.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ErrorMessageBundle {


    public static class Builder {
        private HttpStatus status;
        private String message;
        private String debugMessage;

        public Builder(HttpStatus status) {
            this.status = status;
        }

        public Builder withError(String message) {
            this.message = message;
            return this;
        }

        public Builder whichException(Throwable ex) {
            this.debugMessage = ex.getSuppressed()[0].getLocalizedMessage();
            return this;
        }

        public ErrorMessageBundle buildMessage() {
            return new ErrorMessageBundle(this.status, this.message, this.debugMessage);
        }
    }

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    private ErrorMessageBundle() {this.timestamp = LocalDateTime.now();}

    public static Builder byStatus(HttpStatus status) {
        return new Builder(status);
    }

    private ErrorMessageBundle(HttpStatus status, String message, String debugMessage) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;

    }

    public ResponseEntity<Object> thenResponseEntity() {
       return ResponseEntity.status(this.status).body(this);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    public String getDebugMessage() {
        return debugMessage;
    }
}
