package nl.tudelft.oopp.qubo.repositories;

import java.util.UUID;
import javax.transaction.Transactional;
import nl.tudelft.oopp.qubo.entities.PaceType;
import nl.tudelft.oopp.qubo.entities.PaceVote;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("PaceVoteRepository")
public interface PaceVoteRepository extends JpaRepository<PaceVote, UUID> {
    PaceVote getById(UUID paceVoteId);

    @Transactional
    void deletePaceVoteById(UUID id);

    int countByQuestionBoardAndPaceType(QuestionBoard board, PaceType paceType);
}
