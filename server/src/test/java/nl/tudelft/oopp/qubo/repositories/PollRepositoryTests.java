package nl.tudelft.oopp.qubo.repositories;

import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        assertEquals(poll.getId(), result.getId());
        assertEquals(poll.getText(), result.getText());
        assertEquals(poll.isOpen(), result.isOpen());
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
}
