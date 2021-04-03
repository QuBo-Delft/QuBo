package nl.tudelft.oopp.qubo.repositories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;
import nl.tudelft.oopp.qubo.entities.Poll;
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
public class PollRepositoryTests {
    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Test
    public void getById_withCorrectId_returnsPoll() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Poll poll = new Poll();
        poll.setText("Test Poll");
        poll.setOpen(true);
        poll.setQuestionBoard(board);
        pollRepository.save(poll);

        board.setPoll(poll);

        // Act
        Poll result = pollRepository.getById(poll.getId());

        // Assert
        assertNotNull(result);
        assertEquals(poll, result);
        assertEquals(poll.getQuestionBoard().getId(), result.getQuestionBoard().getId());
        assertEquals(poll, questionBoardRepository.getById(board.getId()).getPoll());
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
        poll.setText("Test Poll");
        poll.setOpen(true);
        poll.setQuestionBoard(board);
        pollRepository.save(poll);

        board.setPoll(poll);

        // Act
        Poll result = pollRepository.getById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    public void deletePollById_withCorrectId_deletesPoll() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Poll poll = new Poll();
        poll.setText("Test poll");
        poll.setOpen(false);
        poll.setQuestionBoard(board);
        poll.setPollOptions(new HashSet<>(2));
        pollRepository.save(poll);

        // Act
        pollRepository.deletePollById(poll.getId());

        // Assert
        Poll result = pollRepository.getById(poll.getId());
        assertNull(result);
    }

    @Test
    public void deletePollById_withIncorrectId_doesNotDeletePoll() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Poll poll = new Poll();
        poll.setText("Test poll");
        poll.setOpen(false);
        poll.setQuestionBoard(board);
        poll.setPollOptions(new HashSet<>(2));
        pollRepository.save(poll);

        // Act
        pollRepository.deletePollById(UUID.randomUUID());

        // Assert
        Poll result = pollRepository.getById(poll.getId());
        assertNotNull(result);
        assertEquals(poll, result);
    }
}
