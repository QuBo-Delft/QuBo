package nl.tudelft.oopp.qubo.services;

import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("mockCurrentTimeProvider")
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
    private final Instant pastInstant = currentStamp.toInstant().minus(1,ChronoUnit.DAYS);
    private final Instant currentInstant = currentStamp.toInstant();
    private final Instant futureInstant = currentStamp.toInstant().plus(1,ChronoUnit.DAYS);

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
        assertEquals(result.getTitle(), qbModel.getTitle());
        assertEquals(result.getStartTime(), qbModel.getStartTime());
        QuestionBoard inDb = questionBoardRepository.getById(result.getId());
        assertEquals(inDb.getId(), result.getId());
        assertEquals(inDb.getModeratorCode(), result.getModeratorCode());
        assertEquals(inDb.getTitle(), result.getTitle());
        assertEquals(inDb.getStartTime(), result.getStartTime());

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
        assertEquals(qb.getId(), result.getId());
        assertEquals(qb.getModeratorCode(), result.getModeratorCode());
        assertEquals(qb.getTitle(), result.getTitle());
        assertEquals(qb.getStartTime(), result.getStartTime());
    }

    @Test
    public void getBoardById_withNonExistentQuBo_returnsNull() {
        // Arrange
        UUID uuid2 = UUID.fromString("967716da-a6f1-412f-804a-9020ec6064f5");
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(false);
        qb.setId(uuid2);
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
        QuestionBoard result = questionBoardService.getBoardByModeratorCode(qb.getId());

        // Assert
        QuestionBoard inDb = questionBoardRepository.getById(qb.getId());
        assertEquals(inDb.getId(), result.getId());
        assertEquals(inDb.getModeratorCode(), result.getModeratorCode());
        assertEquals(inDb.getTitle(), result.getTitle());
        assertEquals(inDb.getStartTime(), result.getStartTime());
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

        HashSet<Question> questionSet = new HashSet<Question>();

        Question q1 = new Question();
        q1.setAuthorName("Author");
        q1.setText("Test question1");
        q1.setSecretCode(UUID.randomUUID());
        q1.setTimestamp(Timestamp.from(currentInstant.plus(1, ChronoUnit.HOURS)));
        q1.setQuestionBoard(qb);
        q1.setAnswers(null);
        q1.setVotes(null);
        questionSet.add(q1);
        questionRepository.save(q1);

        // Act
        Set<Question> result = questionBoardService.getQuestionsByBoardId(qb.getId());

        // Assert
        assertEquals(result, questionSet);
        Set<Question> inDb = questionRepository.getQuestionByQuestionBoard(qb);
        assertEquals(inDb, result);
    }

    @Test
    public void getQuestionsByBoardId_withEmptyQuestionSet_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setTitle("Test board");
        qb.setClosed(false);

        Set<Question> questionSet = new HashSet<Question>();
        questionRepository.saveAll(questionSet);
        qb.setQuestions(questionSet);
        questionBoardRepository.save(qb);

        // Act
        Set<Question> result = questionBoardService.getQuestionsByBoardId(qb.getId());

        // Assert
        assertEquals(qb.getQuestions(), result);
        Set<Question> inDb = questionRepository.getQuestionByQuestionBoard(qb);
        assertEquals(inDb, result);

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

        Set<Question> questionSet = new HashSet<Question>();

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
        assertEquals(inDb.getId(), result.getId());
        assertEquals(inDb.getModeratorCode(), result.getModeratorCode());
        assertEquals(inDb.getTitle(), result.getTitle());
        assertEquals(inDb.getStartTime(), result.getStartTime());
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
        assertEquals("This QuestionBoard does not exist", exception.getMessage());
    }

    @Test
    public void closeBoard_withClosedQuBo_throwsConflictException() {
        // Arrange

        // Act

        // Assert

    }



}
