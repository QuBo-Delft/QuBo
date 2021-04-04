package nl.tudelft.oopp.qubo.repositories;

import java.util.List;
import java.util.UUID;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Question board repository.
 */
@Repository("QuestionBoardRepository")
public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, UUID> {
    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    QuestionBoard getById(UUID id);

    /**
     * Gets by moderator code.
     *
     * @param code the code
     * @return the by moderator code
     */
    QuestionBoard getByModeratorCode(UUID code);

    List<QuestionBoard> findAll();

}
