package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Poll;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("PollRepository")
public interface PollRepository {
    Poll getPollById(UUID pollId);
}