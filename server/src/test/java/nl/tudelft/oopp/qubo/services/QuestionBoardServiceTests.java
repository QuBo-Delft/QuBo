package nl.tudelft.oopp.qubo.services;

import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ConflictException;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.hypermedia.Link;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "mockCurrentTimeProvider"})
public class QuestionBoardServiceTests {

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionBoardService questionBoardService;

    @Autowired
    private CurrentTimeProvider mockCurrentTimeProvider;

    private final Timestamp currentStamp = Timestamp.valueOf("2021-04-01 00:00:00");
    private final Instant currentInstant = currentStamp.toInstant();

    @BeforeEach
    public void setup() {
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(currentStamp.toInstant());
    }

    // saveBoard tests
    @Test
    public void saveBoard_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoardCreationBindingModel qbModel = new QuestionBoardCreationBindingModel();
        qbModel.setTitle("Test board");
        qbModel.setStartTime(currentStamp);

        // Act
        QuestionBoard result = questionBoardService.saveBoard(qbModel);

        // Assert
        assertEquals(qbModel.getTitle(), result.getTitle());
        assertEquals(qbModel.getStartTime(), result.getStartTime());
        QuestionBoard inDb = questionBoardRepository.getById(result.getId());
        assertEquals(inDb, result);
    }

    // getBoardById tests
    @Test
    public void getBoardById_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        QuestionBoard result = questionBoardService.getBoardById(qb.getId());

        // Assert
        assertEquals(qb, result);
    }

    @Test
    public void getBoardById_withNonExistentQuBo_returnsNull() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        QuestionBoard result = questionBoardService.getBoardById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    // getBoardByModeratorCode tests
    @Test
    public void getBoardByModeratorCode_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        QuestionBoard result = questionBoardService.getBoardByModeratorCode(qb.getModeratorCode());

        // Assert
        assertEquals(qb, result);
        QuestionBoard inDb = questionBoardRepository.getById(qb.getId());
        assertEquals(inDb, result);
    }

    @Test
    public void getBoardByModeratorCode_withNonExistentQuBo_returnsNull() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        QuestionBoard result = questionBoardService.getBoardByModeratorCode(UUID.randomUUID());
        // Assert
        assertNull(result);
    }

    // getQuestionsByBoardId tests
    @Test
    public void getQuestionsByBoardId_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Question q1 = new Question();
        q1.setAuthorName("Author");
        q1.setText("Test question1");
        q1.setSecretCode(UUID.randomUUID());
        q1.setTimestamp(Timestamp.from(currentInstant.plus(1, ChronoUnit.HOURS)));
        q1.setQuestionBoard(qb);

        Question q2 = new Question();
        q2.setAuthorName("Author");
        q2.setText("Test question2");
        q2.setSecretCode(UUID.randomUUID());
        q2.setTimestamp(Timestamp.from(currentInstant.plus(1, ChronoUnit.HOURS)));
        q2.setQuestionBoard(qb);

        HashSet<Question> questionSet = new HashSet<>();
        questionSet.add(q1);
        questionSet.add(q2);
        qb.setQuestions(questionSet);
        questionRepository.saveAll(questionSet);

        // Act
        Set<Question> result = questionBoardService.getQuestionsByBoardId(qb.getId());

        // Assert
        assertEquals(new LinkedHashSet<>(questionSet), result);
    }

    @Test
    public void getQuestionsByBoardId_withEmptyQuestionSet_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        Set<Question> result = questionBoardService.getQuestionsByBoardId(qb.getId());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void getQuestionsByBoardId_withNonExistentQuBo_returnsNull() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Question q1 = new Question();
        q1.setAuthorName("Author");
        q1.setText("Test question1");
        q1.setSecretCode(UUID.randomUUID());
        q1.setTimestamp(Timestamp.from(currentInstant.plus(1, ChronoUnit.HOURS)));
        q1.setQuestionBoard(qb);

        Question q2 = new Question();
        q2.setAuthorName("Author");
        q2.setText("Test question2");
        q2.setSecretCode(UUID.randomUUID());
        q2.setTimestamp(Timestamp.from(currentInstant.plus(1, ChronoUnit.HOURS)));
        q2.setQuestionBoard(qb);

        Set<Question> questionSet = new HashSet<>();
        questionSet.add(q1);
        questionSet.add(q2);

        questionRepository.saveAll(questionSet);
        qb.setQuestions(questionSet);

        // Act
        Set<Question> result = questionBoardService.getQuestionsByBoardId(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    // closeBoard tests
    @Test
    public void closeBoard_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        QuestionBoard result = questionBoardService.closeBoard(qb.getId());

        // Assert
        assertTrue(result.isClosed());
        assertEquals(qb.getId(), result.getId());
        assertEquals(qb.getModeratorCode(), result.getModeratorCode());
        assertEquals(qb.getTitle(), result.getTitle());
        assertEquals(qb.getStartTime(), result.getStartTime());

        QuestionBoard inDb = questionBoardRepository.getById(qb.getId());
        assertTrue(inDb.isClosed());
        assertEquals(inDb, result);
    }

    @Test
    public void closeBoard_withNonExistentQuBo_throwsNotFoundException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> questionBoardService.closeBoard(UUID.randomUUID()));

        // Assert
        assertEquals("Question board does not exist", exception.getMessage());
    }

    @Test
    public void closeBoard_withClosedQuBo_throwsConflictException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        // Act
        ConflictException exception = assertThrows(ConflictException.class,
            () -> questionBoardService.closeBoard(qb.getId()));

        // Assert
        assertEquals("This QuestionBoard is already closed", exception.getMessage());
    }
}
