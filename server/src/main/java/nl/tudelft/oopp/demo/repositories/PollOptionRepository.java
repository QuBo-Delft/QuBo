package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.PollOption;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("PollOptionRepository")
public interface PollOptionRepository extends JpaRepository<PollOption, UUID> {
    PollOption getById(UUID pollOptionId);
}
