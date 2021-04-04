package nl.tudelft.oopp.qubo.repositories;

import java.util.UUID;

import nl.tudelft.oopp.qubo.entities.QuestionVote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * The Question vote repository.
 */
@Repository("QuestionVoteRepository")
public interface QuestionVoteRepository extends JpaRepository<QuestionVote, UUID> {

    /**
     * Gets question vote by id.
     *
     * @param questionVoteId the question vote id
     * @return the question vote by id
     */
    QuestionVote getQuestionVoteById(UUID questionVoteId);

    /**
     * Delete question vote by id.
     *
     * @param id The id.
     */
    @Transactional
    void deleteQuestionVoteById(UUID id);
}
