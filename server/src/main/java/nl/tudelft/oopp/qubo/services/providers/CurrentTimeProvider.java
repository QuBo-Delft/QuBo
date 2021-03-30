package nl.tudelft.oopp.qubo.services.providers;

import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * An abstract time provider to make services testable.
 */
@Service
public class CurrentTimeProvider {
    /**
     * Gets current time.
     *
     * @return the current time
     */
    public Instant getCurrentTime() {
        return Instant.now();
    }
}
