package nl.tudelft.oopp.demo.repositories;

import java.util.Set;
import java.util.UUID;

import nl.tudelft.oopp.demo.entities.PaceVote;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository("PaceVoteRepository")
public interface PaceVoteRepository extends JpaRepository<PaceVote, UUID> {
    Set<PaceVote> getPaceVotesByQuestionBoard(QuestionBoard boardId);

    PaceVote getById(UUID paceVoteId);

    @Transactional
    void deletePaceVoteById(UUID id);
}
