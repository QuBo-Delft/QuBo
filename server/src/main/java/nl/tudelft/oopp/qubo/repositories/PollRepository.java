package nl.tudelft.oopp.qubo.repositories;

import nl.tudelft.oopp.qubo.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository("PollRepository")
public interface PollRepository extends JpaRepository<Poll, UUID> {
    Poll getById(UUID pollId);

    @Transactional
    void deletePollById(UUID id);
}