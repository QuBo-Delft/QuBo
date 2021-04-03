package nl.tudelft.oopp.qubo.repositories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PollOptionRepositoryTests {
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

        // Act
        PollOption result = pollOptionRepository.getById(option.getId());

        // Assert
        assertNotNull(result);
        assertEquals(option.getId(), result.getId());
        assertEquals(option.getText(), result.getText());
        assertEquals(option.getPoll().getId(), result.getPoll().getId());
        assertEquals(option.getPoll().getQuestionBoard().getId(), result.getPoll().getQuestionBoard().getId());
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

        // Act
        PollOption result = pollOptionRepository.getById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    public void getPollOptionsByPoll_withPollVotes_returnCorrectPollOptions() {
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

        PollOption option2 = new PollOption();
        option2.setText("Option B");
        option2.setPoll(poll);
        pollOptionRepository.save(option2);

        QuestionBoard board2 = new QuestionBoard();
        board2.setModeratorCode(UUID.randomUUID());
        board2.setStartTime(Timestamp.from(Instant.now()));
        board2.setTitle("Test board 2");
        questionBoardRepository.save(board2);

        Poll poll2 = new Poll();
        poll2.setText("Test poll 2");
        poll2.setOpen(true);
        poll2.setQuestionBoard(board2);
        pollRepository.save(poll2);

        PollOption option3 = new PollOption();
        option3.setText("Option C");
        option3.setPoll(poll2);
        pollOptionRepository.save(option3);

        // Act
        Set<PollOption> pollOptions = pollOptionRepository.getPollOptionsByPoll(poll);

        // Assert
        assertNotNull(pollOptions);
        assertEquals(Set.of(option, option2), pollOptions);
    }

    @Test
    public void getPollOptionsByPoll_withPollVotes_returnNoPollOptions() {
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

        PollOption option2 = new PollOption();
        option2.setText("Option B");
        option2.setPoll(poll);
        pollOptionRepository.save(option2);

        QuestionBoard board2 = new QuestionBoard();
        board2.setModeratorCode(UUID.randomUUID());
        board2.setStartTime(Timestamp.from(Instant.now()));
        board2.setTitle("Test board 2");
        questionBoardRepository.save(board2);

        Poll poll2 = new Poll();
        poll2.setText("Test poll 2");
        poll2.setOpen(true);
        poll2.setQuestionBoard(board2);
        pollRepository.save(poll2);

        // Act
        Set<PollOption> pollOptions = pollOptionRepository.getPollOptionsByPoll(poll2);

        // Assert
        assertNotNull(pollOptions);
        assertTrue(pollOptions.isEmpty());
    }
}
