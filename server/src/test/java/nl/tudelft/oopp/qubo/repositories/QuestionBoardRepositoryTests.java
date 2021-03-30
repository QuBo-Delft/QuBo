package nl.tudelft.oopp.qubo.repositories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
public class QuestionBoardRepositoryTests {
    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Test
    public void getById_withCorrectId_returnsQuestionBoard() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        QuestionBoard board2 = new QuestionBoard();
        board2.setModeratorCode(UUID.randomUUID());
        board2.setStartTime(Timestamp.from(Instant.now()));
        board2.setTitle("Test board 2");
        questionBoardRepository.save(board2);

        // Act
        QuestionBoard result = questionBoardRepository.getById(board.getId());

        // Assert
        assertNotNull(result);
        assertEquals(board.getId(), result.getId());
    }

    @Test
    public void getById_withNonexistentId_returnsQuestionBoard() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        // Act
        QuestionBoard result = questionBoardRepository.getById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    public void getByModeratorCode_withCorrectCode_returnsQuestionBoard() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        QuestionBoard board2 = new QuestionBoard();
        board2.setModeratorCode(UUID.randomUUID());
        board2.setStartTime(Timestamp.from(Instant.now()));
        board2.setTitle("Test board 2");
        questionBoardRepository.save(board2);

        // Act
        QuestionBoard result = questionBoardRepository.getByModeratorCode(board.getModeratorCode());

        // Assert
        assertNotNull(result);
        assertEquals(board.getId(), result.getId());
    }

    @Test
    public void getByModeratorCode_withNonexistentCode_returnsQuestionBoard() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        // Act
        QuestionBoard result = questionBoardRepository.getByModeratorCode(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    public void findAll_withBoards_returnsQuestionBoards() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        QuestionBoard board2 = new QuestionBoard();
        board2.setModeratorCode(UUID.randomUUID());
        board2.setStartTime(Timestamp.from(Instant.now()));
        board2.setTitle("Test board 2");
        questionBoardRepository.save(board2);

        // Act
        List<QuestionBoard> result = questionBoardRepository.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsAll(Set.of(board, board2)));
    }

    @Test
    public void findAll_withNoBoards_returnsEmptyList() {
        // Arrange
        questionBoardRepository.deleteAll();

        // Act
        List<QuestionBoard> result = questionBoardRepository.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
