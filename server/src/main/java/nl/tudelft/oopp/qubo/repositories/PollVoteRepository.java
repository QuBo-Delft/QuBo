package nl.tudelft.oopp.qubo.repositories;

import nl.tudelft.oopp.qubo.entities.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("PollVoteRepository")
public interface PollVoteRepository extends JpaRepository<PollVote, UUID> {
    PollVote getById(UUID pollVoteId);
}