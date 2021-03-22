package nl.tudelft.oopp.demo.repositories;

import java.util.UUID;

import nl.tudelft.oopp.demo.entities.PaceVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository("PaceVoteRepository")
public interface PaceVoteRepository extends JpaRepository<PaceVote, UUID> {
    PaceVote getById(UUID paceVoteId);

    @Transactional
    void deletePaceVoteById(UUID id);
}
