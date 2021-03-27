package nl.tudelft.oopp.demo.repositories;

import java.util.UUID;
import javax.transaction.Transactional;
import nl.tudelft.oopp.demo.entities.PaceType;
import nl.tudelft.oopp.demo.entities.PaceVote;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("PaceVoteRepository")
public interface PaceVoteRepository extends JpaRepository<PaceVote, UUID> {
    PaceVote getById(UUID paceVoteId);

    @Transactional
    void deletePaceVoteById(UUID id);

    int countByQuestionBoardAndPaceType(QuestionBoard board, PaceType paceType);
}
