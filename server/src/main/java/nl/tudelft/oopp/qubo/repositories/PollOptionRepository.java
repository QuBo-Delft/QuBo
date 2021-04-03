package nl.tudelft.oopp.qubo.repositories;

import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository("PollOptionRepository")
public interface PollOptionRepository extends JpaRepository<PollOption, UUID> {
    Set<PollOption> getPollOptionsByPoll(Poll poll);

    PollOption getById(UUID pollOptionId);
}
