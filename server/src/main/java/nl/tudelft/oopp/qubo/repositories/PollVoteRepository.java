package nl.tudelft.oopp.qubo.repositories;

import nl.tudelft.oopp.qubo.entities.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * The Poll vote repository.
 */
@Repository("PollVoteRepository")
public interface PollVoteRepository extends JpaRepository<PollVote, UUID> {
    /**
     * Gets by id.
     *
     * @param pollVoteId the poll vote id
     * @return the by id
     */
    PollVote getById(UUID pollVoteId);

    /**
     * Delete poll vote by id.
     *
     * @param pollVoteId The poll vote id.
     */
    @Transactional
    void deletePollVoteById(UUID pollVoteId);
}
