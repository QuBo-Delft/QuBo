package nl.tudelft.oopp.demo.repositories;

import java.util.UUID;
import nl.tudelft.oopp.demo.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("AnswerRepository")
public interface AnswerRepository extends JpaRepository<Answer, UUID> {
}
