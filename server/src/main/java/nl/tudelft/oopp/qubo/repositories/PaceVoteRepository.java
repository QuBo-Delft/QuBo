package nl.tudelft.oopp.qubo.repositories;

import java.util.UUID;
import javax.transaction.Transactional;
import nl.tudelft.oopp.qubo.entities.PaceType;
import nl.tudelft.oopp.qubo.entities.PaceVote;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Pace vote repository.
 */
@Repository("PaceVoteRepository")
public interface PaceVoteRepository extends JpaRepository<PaceVote, UUID> {
    /**
     * Get pace vote by id.
     *
     * @param paceVoteId The pace vote id.
     * @return The by id.
     */
    PaceVote getById(UUID paceVoteId);

    /**
     * Delete pace vote by id.
     *
     * @param id The id.
     */
    @Transactional
    void deletePaceVoteById(UUID id);

    /**
     * Get the number of pace votes of a specific type in the given board.
     *
     * @param board    The board.
     * @param paceType The pace type.
     * @return The int.
     */
    int countByQuestionBoardAndPaceType(QuestionBoard board, PaceType paceType);
}
