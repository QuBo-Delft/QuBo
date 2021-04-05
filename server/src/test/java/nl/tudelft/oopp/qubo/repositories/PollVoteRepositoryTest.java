package nl.tudelft.oopp.qubo.repositories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import nl.tudelft.oopp.qubo.entities.PollVote;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    @Test
    public void deletePollVoteById_withValidId_deletesPollVote() {
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
        pollVoteRepository.deletePollVoteById(vote.getId());

        // Assert
        assertNull(pollVoteRepository.getById(vote.getId()));
    }
}
