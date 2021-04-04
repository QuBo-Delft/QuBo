package nl.tudelft.oopp.qubo.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Forbidden exception.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    /**
     * Instantiates a new Forbidden exception.
     */
    public ForbiddenException() {
    }

    /**
     * Instantiates a new Forbidden exception.
     *
     * @param message The message.
     */
    public ForbiddenException(String message) {
        super(message);
    }
}
