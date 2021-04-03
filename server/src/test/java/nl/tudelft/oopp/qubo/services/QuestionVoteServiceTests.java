package nl.tudelft.oopp.qubo.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.entities.QuestionVote;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionVoteRepository;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("mockCurrentTimeProvider")
public class QuestionVoteServiceTests {
    @Autowired
    private QuestionVoteRepository questionVoteRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private QuestionVoteService questionVoteService;

    @Test
    public void registerVote_withValidQuestionId_addsVote() {
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

        Question question2 = new Question();
        question2.setAuthorName("Author");
        question2.setText("Test question2");
        question2.setSecretCode(UUID.randomUUID());
        question2.setTimestamp(Timestamp.from(Instant.now()));
        question2.setQuestionBoard(board);
        questionRepository.save(question2);

        // Act
        QuestionVote result = questionVoteService.registerVote(question.getId());

        // Assert
        assertNotNull(result);
        QuestionVote inDb = questionVoteRepository.getQuestionVoteById(result.getId());
        assertEquals(inDb, result);
        assertEquals(question.getId(), inDb.getQuestion().getId());
    }

    @Test
    public void registerVote_withNonexistentQuestionId_throwsException() {
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
        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> questionVoteService.registerVote(UUID.randomUUID()));

        // Assert
        assertEquals("Question does not exist", exception.getMessage());
    }
}
