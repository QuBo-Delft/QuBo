package nl.tudelft.oopp.qubo.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Not found exception.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    /**
     * Instantiates a new Not found exception.
     */
    public NotFoundException() {
    }

    /**
     * Instantiates a new Not found exception.
     *
     * @param message The message.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
