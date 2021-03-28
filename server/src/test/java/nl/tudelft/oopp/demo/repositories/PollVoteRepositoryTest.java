package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.PollOption;
import nl.tudelft.oopp.demo.entities.PollVote;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PollVoteRepositoryTest {
    @Autowired
    private PollVoteRepository pollVoteRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Test
    public void getById_withCorrectId_returnsPollOption() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Poll poll = new Poll();
        poll.setText("Test poll");
        poll.setOpen(true);
        poll.setQuestionBoard(board);
        pollRepository.save(poll);

        PollOption option = new PollOption();
        option.setText("Option A");
        option.setPoll(poll);
        pollOptionRepository.save(option);

        PollVote vote = new PollVote();
        vote.setPollOption(option);
        pollVoteRepository.save(vote);

        // Act
        PollVote result = pollVoteRepository.getById(vote.getId());

        // Assert
        assertNotNull(result);
        assertEquals(vote.getId(), result.getId());
        assertEquals(vote.getPollOption().getId(), result.getPollOption().getId());
        assertEquals(vote.getPollOption().getPoll().getId(),
                result.getPollOption().getPoll().getId());
        assertEquals(vote.getPollOption().getPoll().getQuestionBoard().getId(),
                result.getPollOption().getPoll().getQuestionBoard().getId());
    }

    @Test
    public void getById_withNonexistentId_returnsNull() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Poll poll = new Poll();
        poll.setText("Test poll");
        poll.setOpen(true);
        poll.setQuestionBoard(board);
        pollRepository.save(poll);

        PollOption option = new PollOption();
        option.setText("Option A");
        option.setPoll(poll);
        pollOptionRepository.save(option);

        PollVote vote = new PollVote();
        vote.setPollOption(option);
        pollVoteRepository.save(vote);

        // Act
        PollVote result = pollVoteRepository.getById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }
}
