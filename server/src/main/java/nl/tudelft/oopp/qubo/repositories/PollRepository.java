package nl.tudelft.oopp.qubo.repositories;

import java.util.UUID;
import javax.transaction.Transactional;
import nl.tudelft.oopp.qubo.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Poll repository.
 */
@Repository("PollRepository")
public interface PollRepository extends JpaRepository<Poll, UUID> {
    /**
     * Gets by id.
     *
     * @param pollId the poll id
     * @return The by id.
     */
    Poll getById(UUID pollId);

    /**
     * Delete poll by id.
     *
     * @param id The id.
     */
    @Transactional
    void deletePollById(UUID id);
}