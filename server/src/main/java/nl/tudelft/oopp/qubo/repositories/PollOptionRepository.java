package nl.tudelft.oopp.qubo.repositories;

import java.util.Set;
import java.util.UUID;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Poll option repository.
 */
@Repository("PollOptionRepository")
public interface PollOptionRepository extends JpaRepository<PollOption, UUID> {
    /**
     * Gets poll options by poll.
     *
     * @param poll the poll
     * @return The poll options by poll.
     */
    Set<PollOption> getPollOptionsByPoll(Poll poll);

    /**
     * Gets by id.
     *
     * @param pollOptionId the poll option id
     * @return The by id.
     */
    PollOption getById(UUID pollOptionId);
}
