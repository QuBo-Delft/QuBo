package nl.tudelft.oopp.qubo.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.Answer;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.AnswerRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
public class AnswerServiceTests {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private CurrentTimeProvider mockCurrentTimeProvider;

    @Test
    public void addAnswerToQuestion_withValidQuestion_worksCorrectly() {
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

        AnswerCreationBindingModel model = new AnswerCreationBindingModel();
        model.setText("Test answer");

        Timestamp testTime = Timestamp.valueOf("2021-03-01 00:02:00");
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(testTime.toInstant());

        // Act
        Answer result = answerService.addAnswerToQuestion(model, question);

        // Assert
        assertNotNull(result);
        Optional<Answer> inDbOptional = answerRepository.findById(result.getId());
        assertTrue(inDbOptional.isPresent());
        Answer inDb = inDbOptional.get();
        assertEquals(inDb, result);
        assertEquals(model.getText(), result.getText());
        assertEquals(question.getId(), result.getQuestion().getId());
        assertEquals(testTime, result.getTimestamp());
    }
}
