package nl.tudelft.oopp.demo.repositories;

import java.util.Optional;
import java.util.UUID;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.entities.QuestionVote;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class QuestionVoteRepositoryTests {
    @Autowired
    private QuestionVoteRepository questionVoteRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Test
    public void findById_withCorrectId_returnsQuestionVote() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        questionBoardRepository.save(board);
        Question question = new Question();
        question.setQuestionBoard(board);
        questionRepository.save(question);
        QuestionVote vote = new QuestionVote();
        vote.setQuestion(question);
        questionVoteRepository.save(vote);

        // Act
        Optional<QuestionVote> result = questionVoteRepository.findById(vote.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(vote.getId(), result.get().getId());
        assertEquals(vote.getQuestion().getId(), result.get().getQuestion().getId());
        assertEquals(vote.getQuestion().getQuestionBoard().getId(),
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
        QuestionVote vote = new QuestionVote();
        vote.setQuestion(question);
        questionVoteRepository.save(vote);

        // Act
        Optional<QuestionVote> result = questionVoteRepository.findById(UUID.randomUUID());

        // Assert
        assertTrue(result.isEmpty());
    }
}
