package nl.tudelft.oopp.qubo.repositories;

import java.util.UUID;
import javax.transaction.Transactional;
import nl.tudelft.oopp.qubo.entities.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Poll vote repository.
 */
@Repository("PollVoteRepository")
public interface PollVoteRepository extends JpaRepository<PollVote, UUID> {
    /**
     * Gets by id.
     *
     * @param pollVoteId The poll vote id.
     * @return The by id.
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
