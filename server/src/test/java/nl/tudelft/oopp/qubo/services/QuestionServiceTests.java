package nl.tudelft.oopp.qubo.services;

import java.sql.Timestamp;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.question.QuestionEditingBindingModel;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ConflictException;
import nl.tudelft.oopp.qubo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "mockCurrentTimeProvider"})
public class QuestionServiceTests {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CurrentTimeProvider mockCurrentTimeProvider;

    @Test
    public void createQuestion_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:01:00"));
        board.setTitle("Test board");
        board.setClosed(false);
        questionBoardRepository.save(board);

        QuestionCreationBindingModel model = new QuestionCreationBindingModel();
        model.setAuthorName("Author");
        model.setText("Test question");

        String testIp = "1.2.3.4";

        Timestamp testTime = Timestamp.valueOf("2021-03-01 00:02:00");
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(testTime.toInstant());

        // Act
        Question result = questionService.createQuestion(model, board.getId(), testIp);

        // Assert
        assertNotNull(result);
        Question inDb = questionRepository.getQuestionById(result.getId());
        assertEquals(inDb.getText(), result.getText());
        assertEquals(inDb.getId(), result.getId());
        assertEquals(model.getText(), inDb.getText());
        assertEquals(model.getAuthorName(), inDb.getAuthorName());
        assertEquals(testTime, inDb.getTimestamp());
        assertEquals(testIp, inDb.getIp());
        assertEquals(board.getId(), inDb.getQuestionBoard().getId());
    }

    @Test
    public void createQuestion_withNonexistentBoardId_throwsNotFoundException() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:01:00"));
        board.setTitle("Test board");
        board.setClosed(false);
        questionBoardRepository.save(board);

        QuestionCreationBindingModel model = new QuestionCreationBindingModel();
        model.setAuthorName("Author");
        model.setText("Test question");

        String testIp = "1.2.3.4";

        Timestamp testTime = Timestamp.valueOf("2021-03-01 00:02:00");
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(testTime.toInstant());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> questionService.createQuestion(model, UUID.randomUUID(), testIp));

        // Assert
        assertEquals("Question board does not exist", exception.getMessage());
    }

    @Test
    public void createQuestion_withBoardStartTimeAfterCurrentTime_throwsForbiddenException() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:03:00"));
        board.setTitle("Test board");
        board.setClosed(false);
        questionBoardRepository.save(board);

        QuestionCreationBindingModel model = new QuestionCreationBindingModel();
        model.setAuthorName("Author");
        model.setText("Test question");

        String testIp = "1.2.3.4";

        Timestamp testTime = Timestamp.valueOf("2021-03-01 00:02:00");
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(testTime.toInstant());

        // Act
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> questionService.createQuestion(model, board.getId(), testIp));

        // Assert
        assertEquals("Question board is not active", exception.getMessage());
    }

    @Test
    public void createQuestion_withBoardClosed_throwsForbiddenException() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:01:00"));
        board.setTitle("Test board");
        board.setClosed(true);
        questionBoardRepository.save(board);

        QuestionCreationBindingModel model = new QuestionCreationBindingModel();
        model.setAuthorName("Author");
        model.setText("Test question");

        String testIp = "1.2.3.4";

        Timestamp testTime = Timestamp.valueOf("2021-03-01 00:02:00");
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(testTime.toInstant());

        // Act
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> questionService.createQuestion(model, board.getId(), testIp));

        // Assert
        assertEquals("Question board is not active", exception.getMessage());
    }

    @Test
    public void getQuestionById_withValidQuestionId_returnsCorrectQuestion() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:01:00"));
        board.setTitle("Test board");
        board.setClosed(true);
        questionBoardRepository.save(board);

        Question expected = new Question();
        expected.setAuthorName("Author");
        expected.setText("Test question");
        expected.setSecretCode(UUID.randomUUID());
        expected.setTimestamp(Timestamp.valueOf("2021-03-01 00:01:00"));
        expected.setQuestionBoard(board);
        questionRepository.save(expected);

        // Act
        Question actual = questionService.getQuestionById(expected.getId());

        // Assert
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(board.getId(), actual.getQuestionBoard().getId());
    }

    @Test
    public void getQuestionById_withNonexistentQuestionId_returnsNull() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:01:00"));
        board.setTitle("Test board");
        board.setClosed(true);
        questionBoardRepository.save(board);

        Question expected = new Question();
        expected.setAuthorName("Author");
        expected.setText("Test question");
        expected.setSecretCode(UUID.randomUUID());
        expected.setTimestamp(Timestamp.valueOf("2021-03-01 00:01:00"));
        expected.setQuestionBoard(board);
        questionRepository.save(expected);

        // Act
        Question actual = questionService.getQuestionById(UUID.randomUUID());

        // Assert
        assertNull(actual);
    }

    @Test
    public void editQuestion_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:01:00"));
        board.setTitle("Test board");
        board.setClosed(true);
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setSecretCode(UUID.randomUUID());
        question.setTimestamp(Timestamp.valueOf("2021-03-01 00:01:00"));
        question.setQuestionBoard(board);
        questionRepository.save(question);

        String newText = "New text of question";

        QuestionEditingBindingModel model = new QuestionEditingBindingModel();
        model.setText(newText);

        // Act
        Question modified = questionService.editQuestion(question.getId(), model);

        // Assert
        assertEquals(question.getId(), modified.getId());
        assertEquals(newText, modified.getText());

        Question inDb = questionRepository.getQuestionById(question.getId());
        assertEquals(newText, inDb.getText());
        assertEquals(board.getId(), inDb.getQuestionBoard().getId());
    }

    @Test
    public void editQuestion_withNonexistentQuestionId_throwsNotFoundException() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:01:00"));
        board.setTitle("Test board");
        board.setClosed(false);
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setSecretCode(UUID.randomUUID());
        question.setTimestamp(Timestamp.valueOf("2021-03-01 00:01:00"));
        question.setQuestionBoard(board);
        questionRepository.save(question);

        String newText = "New text of question";

        QuestionEditingBindingModel model = new QuestionEditingBindingModel();
        model.setText(newText);

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> questionService.editQuestion(UUID.randomUUID(), model));

        // Assert
        assertEquals("Question does not exist", exception.getMessage());
    }

    @Test
    public void deleteQuestionById_withValidId_worksCorrectly() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:01:00"));
        board.setTitle("Test board");
        board.setClosed(true);
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setSecretCode(UUID.randomUUID());
        question.setTimestamp(Timestamp.valueOf("2021-03-01 00:01:00"));
        question.setQuestionBoard(board);
        questionRepository.save(question);

        // Act
        questionService.deleteQuestionById(question.getId());

        // Assert
        Question inDb = questionRepository.getQuestionById(question.getId());
        assertNull(inDb);
    }

    @Test
    public void deleteQuestion_withNonexistentId_doesNotDeleteQuestion() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:01:00"));
        board.setTitle("Test board");
        board.setClosed(true);
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setSecretCode(UUID.randomUUID());
        question.setTimestamp(Timestamp.valueOf("2021-03-01 00:01:00"));
        question.setQuestionBoard(board);
        questionRepository.save(question);

        // Act
        questionService.deleteQuestionById(UUID.randomUUID());

        // Assert
        Question inDb = questionRepository.getQuestionById(question.getId());
        assertNotNull(inDb);
    }

    @Test
    public void markAsAnswered_withValidQuestionId_worksCorrectly() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:01:00"));
        board.setTitle("Test board");
        board.setClosed(true);
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setSecretCode(UUID.randomUUID());
        question.setTimestamp(Timestamp.valueOf("2021-03-01 00:01:00"));
        question.setQuestionBoard(board);
        questionRepository.save(question);

        Timestamp testTime = Timestamp.valueOf("2021-03-01 00:02:00");
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(testTime.toInstant());

        // Act
        Question marked = questionService.markAsAnswered(question.getId());

        // Assert
        assertEquals(question.getId(), marked.getId());
        assertEquals(testTime, marked.getAnswered());

        Question inDb = questionRepository.getQuestionById(question.getId());
        assertEquals(testTime, inDb.getAnswered());
    }

    @Test
    public void markAsAnswered_withAlreadyAnsweredQuestion_throwsConflictException() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.valueOf("2021-03-01 00:01:00"));
        board.setTitle("Test board");
        board.setClosed(true);
        questionBoardRepository.save(board);

        Question question = new Question();
        question.setAuthorName("Author");
        question.setText("Test question");
        question.setSecretCode(UUID.randomUUID());
        question.setTimestamp(Timestamp.valueOf("2021-03-01 00:01:00"));
        question.setAnswered(Timestamp.valueOf("2021-03-01 00:01:30"));
        question.setQuestionBoard(board);
        questionRepository.save(question);

        Timestamp testTime = Timestamp.valueOf("2021-03-01 00:02:00");
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(testTime.toInstant());

        // Act
        ConflictException exception = assertThrows(ConflictException.class,
            () -> questionService.markAsAnswered(question.getId()));

        // Assert
        assertEquals("Question was already marked as answered", exception.getMessage());
    }

    @Test
    public void canModifyQuestion_withSecretCode_returnsTrue() {
        // Arrange
        UUID testSecretCode = UUID.randomUUID();

        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());

        Question question = new Question();
        question.setSecretCode(testSecretCode);
        question.setQuestionBoard(board);

        // Act
        boolean actual = questionService.canModifyQuestion(question, testSecretCode);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void canModifyQuestion_withModeratorCode_returnsTrue() {
        // Arrange
        UUID testModeratorCode = UUID.randomUUID();

        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(testModeratorCode);

        Question question = new Question();
        question.setSecretCode(UUID.randomUUID());
        question.setQuestionBoard(board);

        // Act
        boolean actual = questionService.canModifyQuestion(question, testModeratorCode);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void canModifyQuestion_withInvalidCode_returnsFalse() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());

        Question question = new Question();
        question.setSecretCode(UUID.randomUUID());
        question.setQuestionBoard(board);

        // Act
        boolean actual = questionService.canModifyQuestion(question, UUID.randomUUID());

        // Assert
        assertFalse(actual);
    }

}
