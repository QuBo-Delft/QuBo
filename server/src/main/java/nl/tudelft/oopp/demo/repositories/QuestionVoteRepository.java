package nl.tudelft.oopp.demo.repositories;

import java.util.UUID;

import nl.tudelft.oopp.demo.entities.QuestionVote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("QuestionVoteRepository")
public interface QuestionVoteRepository extends JpaRepository<QuestionVote, UUID> {
}
