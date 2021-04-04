package nl.tudelft.oopp.qubo.repositories;

import java.util.Set;
import java.util.UUID;
import javax.transaction.Transactional;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Question repository.
 */
@Repository("QuestionRepository")
public interface QuestionRepository extends JpaRepository<Question, QuestionBoard> {
    /**
     * Gets question by question board.
     *
     * @param boardId The board id.
     * @return The question by question board.
     */
    Set<Question> getQuestionByQuestionBoard(QuestionBoard boardId);

    /**
     * Gets question by id.
     *
     * @param questionId The question id.
     * @return The question by id.
     */
    Question getQuestionById(UUID questionId);

    /**
     * Delete question by id.
     *
     * @param id The id.
     */
    @Transactional
    void deleteQuestionById(UUID id);
}
