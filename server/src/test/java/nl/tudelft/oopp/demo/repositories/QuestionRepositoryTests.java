package nl.tudelft.oopp.demo.repositories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
public class QuestionRepositoryTests {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Test
    public void getQuestionById_withCorrectId_returnsQuestion() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setSecretCode(UUID.randomUUID());
        question.setTimestamp(Timestamp.from(Instant.now()));
        question.setQuestionBoard(board);
        questionRepository.save(question);

        // Act
        Question result = questionRepository.getQuestionById(question.getId());

        // Assert
        assertNotNull(result);
        assertEquals(question.getId(), result.getId());
        assertEquals(question.getQuestionBoard().getId(), result.getQuestionBoard().getId());
    }

    @Test
    public void getQuestionById_withNonexistentId_returnsNull() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        board.setModeratorCode(UUID.randomUUID());
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setSecretCode(UUID.randomUUID());
        question.setTimestamp(Timestamp.from(Instant.now()));
        question.setQuestionBoard(board);
        questionRepository.save(question);

        // Act
        Question result = questionRepository.getQuestionById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    public void deleteQuestionById_withCorrectId_deletesQuestion() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setSecretCode(UUID.randomUUID());
        question.setTimestamp(Timestamp.from(Instant.now()));
        question.setQuestionBoard(board);
        questionRepository.save(question);

        // Act
        questionRepository.deleteQuestionById(question.getId());

        // Assert
        Question result = questionRepository.getQuestionById(question.getId());
        assertNull(result);
    }

    @Test
    public void getQuestionByQuestionBoard_withQuestions_returnsCorrectQuestions() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board 1");
        questionBoardRepository.save(board);

        Question question1 = new Question();
        question1.setAuthorName("Author");
        question1.setText("Test question1");
        question1.setSecretCode(UUID.randomUUID());
        question1.setTimestamp(Timestamp.from(Instant.now()));
        question1.setQuestionBoard(board);
        questionRepository.save(question1);

        Question question2 = new Question();
        question2.setAuthorName("Author");
        question2.setText("Test question2");
        question2.setSecretCode(UUID.randomUUID());
        question2.setTimestamp(Timestamp.from(Instant.now()));
        question2.setQuestionBoard(board);
        questionRepository.save(question2);

        QuestionBoard board2 = new QuestionBoard();
        board2.setModeratorCode(UUID.randomUUID());
        board2.setStartTime(Timestamp.from(Instant.now()));
        board2.setTitle("Test board 2");
        questionBoardRepository.save(board2);

        Question question3 = new Question();
        question3.setAuthorName("Author");
        question3.setText("Test question3");
        question3.setSecretCode(UUID.randomUUID());
        question3.setTimestamp(Timestamp.from(Instant.now()));
        question3.setQuestionBoard(board2);
        questionRepository.save(question3);

        // Act
        Set<Question> result = questionRepository.getQuestionByQuestionBoard(board);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(question1));
        assertTrue(result.contains(question2));
        assertFalse(result.contains(question3));
    }

    @Test
    public void getQuestionByQuestionBoard_withNoQuestions_returnsNoQuestions() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board 1");
        questionBoardRepository.save(board);

        Question question1 = new Question();
        question1.setAuthorName("Author");
        question1.setText("Test question1");
        question1.setSecretCode(UUID.randomUUID());
        question1.setTimestamp(Timestamp.from(Instant.now()));
        question1.setQuestionBoard(board);
        questionRepository.save(question1);

        Question question2 = new Question();
        question2.setAuthorName("Author");
        question2.setText("Test question2");
        question2.setSecretCode(UUID.randomUUID());
        question2.setTimestamp(Timestamp.from(Instant.now()));
        question2.setQuestionBoard(board);
        questionRepository.save(question2);

        QuestionBoard board2 = new QuestionBoard();
        board2.setModeratorCode(UUID.randomUUID());
        board2.setStartTime(Timestamp.from(Instant.now()));
        board2.setTitle("Test board 2");
        questionBoardRepository.save(board2);

        // Act
        Set<Question> result = questionRepository.getQuestionByQuestionBoard(board2);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
