package nl.tudelft.oopp.qubo.repositories;

import java.util.UUID;
import nl.tudelft.oopp.qubo.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Answer repository.
 */
@Repository("AnswerRepository")
public interface AnswerRepository extends JpaRepository<Answer, UUID> {
}
