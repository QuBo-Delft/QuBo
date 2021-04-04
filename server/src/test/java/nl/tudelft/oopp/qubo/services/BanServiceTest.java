package nl.tudelft.oopp.qubo.services;

import nl.tudelft.oopp.qubo.entities.Ban;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.BanRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "mockCurrentTimeProvider"})
public class BanServiceTest {
    @Autowired
    private BanRepository banRepository;

    @Autowired
    private BanService banService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Test
    public void banIp_withValidData() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        board.setClosed(false);
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setQuestionBoard(board);
        question.setSecretCode(UUID.randomUUID());
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setIp("0.0.0.0");
        questionRepository.save(question);

        // Act
        banService.banIp(question.getId());

        Set<Ban> banSet = banRepository.getBanByQuestionBoard(board);
        assertEquals(1, banSet.size());
        Ban ban = banSet.iterator().next();
        assertEquals(question.getIp(), ban.getIp());
        assertEquals(board.getId(), ban.getQuestionBoard().getId());
    }

    @Test
    public void banIp_withNullIp_throwsConflictException() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        board.setClosed(false);
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setQuestionBoard(board);
        question.setSecretCode(UUID.randomUUID());
        question.setAuthorName("Author");
        question.setText("Test question");
        questionRepository.save(question);

        // Act
        ConflictException exception = assertThrows(ConflictException.class,
            () -> banService.banIp(question.getId()));

        // Assert
        assertEquals("The IP of the question is null.", exception.getMessage());
        assertTrue(banRepository.getBanByQuestionBoard(board).isEmpty());
    }

    @Test
    public void banIp_withAlreadyBannedIp_throwsConflictException() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        board.setClosed(false);
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setQuestionBoard(board);
        question.setSecretCode(UUID.randomUUID());
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setIp("0.0.0.0");
        questionRepository.save(question);

        Ban ban = new Ban();
        ban.setQuestionBoard(board);
        ban.setIp(question.getIp());
        banRepository.save(ban);

        // Act
        ConflictException exception = assertThrows(ConflictException.class,
            () -> banService.banIp(question.getId()));

        // Assert
        assertEquals("This IP has already been banned from the requested question board.",
            exception.getMessage());
    }

    @Test
    public void banIp_withMultipleAlreadyBannedIp_throwsConflictException() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        board.setClosed(false);
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setQuestionBoard(board);
        question.setSecretCode(UUID.randomUUID());
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setIp("0.0.0.0");
        questionRepository.save(question);

        Ban ban1 = new Ban();
        ban1.setQuestionBoard(board);
        ban1.setIp(question.getIp());
        banRepository.save(ban1);

        Ban ban2 = new Ban();
        ban2.setQuestionBoard(board);
        ban2.setIp("1.1.1.1");
        banRepository.save(ban2);

        Ban ban3 = new Ban();
        ban3.setQuestionBoard(board);
        ban3.setIp("2.2.2.2");
        banRepository.save(ban3);

        // Act
        ConflictException exception = assertThrows(ConflictException.class,
            () -> banService.banIp(question.getId()));

        // Assert
        assertEquals("This IP has already been banned from the requested question board.",
            exception.getMessage());
        assertFalse(banRepository.getBanByQuestionBoard(board).isEmpty());
    }
}
