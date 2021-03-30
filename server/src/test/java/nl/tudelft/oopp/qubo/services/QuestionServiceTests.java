package nl.tudelft.oopp.qubo.services;

import java.sql.Timestamp;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.question.QuestionEditingBindingModel;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("mockCurrentTimeProvider")
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

}
