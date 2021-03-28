package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.PollOption;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("PollOptionRepository")
public interface PollOptionRepository {
    PollOption getPollOptionById(UUID pollOptionId);
}
