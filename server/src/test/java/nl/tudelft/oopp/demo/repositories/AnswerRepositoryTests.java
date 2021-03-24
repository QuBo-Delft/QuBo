package nl.tudelft.oopp.demo.repositories;

import java.util.Optional;
import java.util.UUID;
import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AnswerRepositoryTests {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Test
    public void findById_withCorrectId_returnsAnswer() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        questionBoardRepository.save(board);
        Question question = new Question();
        question.setQuestionBoard(board);
        questionRepository.save(question);
        Answer answer = new Answer();
        answer.setQuestion(question);
        answerRepository.save(answer);

        // Act
        Optional<Answer> result = answerRepository.findById(answer.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(answer.getId(), result.get().getId());
        assertEquals(answer.getQuestion().getId(), result.get().getQuestion().getId());
        assertEquals(answer.getQuestion().getQuestionBoard().getId(),
            result.get().getQuestion().getQuestionBoard().getId());
    }

    @Test
    public void findById_withNonexistentId_returnsNull() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        questionBoardRepository.save(board);
        Question question = new Question();
        question.setQuestionBoard(board);
        questionRepository.save(question);
        Answer answer = new Answer();
        answer.setQuestion(question);
        answerRepository.save(answer);

        // Act
        Optional<Answer> result = answerRepository.findById(UUID.randomUUID());

        // Assert
        assertTrue(result.isEmpty());
    }
}
