package nl.tudelft.oopp.qubo.repositories;

import nl.tudelft.oopp.qubo.entities.Ban;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BanRepositoryTests {
    @Autowired
    private BanRepository banRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Test
    public void getById_withCorrectId_returnsBan() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Ban ban = new Ban();
        ban.setQuestionBoard(board);
        ban.setIp("192.168.0.1");
        banRepository.save(ban);

        // Act
        Ban result = banRepository.getById(ban.getId());

        // Assert
        assertNotNull(result);
        assertEquals(ban, result);
        assertEquals(ban.getQuestionBoard().getId(), result.getQuestionBoard().getId());
    }

    @Test
    public void getById_withNonexistentId_returnsNull() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Ban ban = new Ban();
        ban.setQuestionBoard(board);
        ban.setIp("192.168.0.1");
        banRepository.save(ban);

        // Act
        Ban result = banRepository.getById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    public void deleteBanById_withCorrectId_deletesBan() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Ban ban = new Ban();
        ban.setQuestionBoard(board);
        ban.setIp("192.168.0.1");
        banRepository.save(ban);

        // Act
        banRepository.deleteBanById(ban.getId());

        // Assert
        Ban result = banRepository.getById(ban.getId());
        assertNull(result);
    }

    @Test
    public void deleteBanById_withIncorrectId_doesNotDeleteBan() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Ban ban = new Ban();
        ban.setQuestionBoard(board);
        ban.setIp("192.168.0.1");
        banRepository.save(ban);

        // Act
        banRepository.deleteBanById(UUID.randomUUID());

        // Assert
        Ban result = banRepository.getById(ban.getId());
        assertNotNull(result);
        assertEquals(ban, result);
    }

    @Test
    public void getBanByQuestionBoard_withValidId_returnsSetOfBan() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Ban ban = new Ban();
        ban.setQuestionBoard(board);
        ban.setIp("192.168.0.1");
        banRepository.save(ban);

        // Act
        Set<Ban> result = banRepository.getBanByQuestionBoard(board);

        // Assert
        assertNotNull(result);
        assertEquals(Set.of(ban), result);
    }

    @Test
    public void getByQuestionBoard_withNonExistentId_returnsEmptySet() {
        // Arrange
        QuestionBoard board1 = new QuestionBoard();
        board1.setModeratorCode(UUID.randomUUID());
        board1.setStartTime(Timestamp.from(Instant.now()));
        board1.setTitle("Test board1");
        questionBoardRepository.save(board1);

        QuestionBoard board2 = new QuestionBoard();
        board2.setModeratorCode(UUID.randomUUID());
        board2.setStartTime(Timestamp.from(Instant.now()));
        board2.setTitle("Test board2");
        questionBoardRepository.save(board2);

        Ban ban = new Ban();
        ban.setQuestionBoard(board1);
        ban.setIp("192.168.0.1");
        banRepository.save(ban);

        // Act
        Set<Ban> result = banRepository.getBanByQuestionBoard(board2);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getByQuestionBoard_withValidId_returnsSetOfBans() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board1");
        questionBoardRepository.save(board);

        Ban ban1 = new Ban();
        ban1.setQuestionBoard(board);
        ban1.setIp("192.168.0.1");
        banRepository.save(ban1);

        Ban ban2 = new Ban();
        ban2.setQuestionBoard(board);
        ban2.setIp("192.168.2.1");
        banRepository.save(ban2);

        // Act
        Set<Ban> result = banRepository.getBanByQuestionBoard(board);

        // Assert
        assertNotNull(result);
        assertEquals(Set.of(ban1, ban2), result);
    }
}
