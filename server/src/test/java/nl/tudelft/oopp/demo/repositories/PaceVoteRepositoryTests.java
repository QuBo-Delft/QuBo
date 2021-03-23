package nl.tudelft.oopp.demo.repositories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import nl.tudelft.oopp.demo.entities.PaceType;
import nl.tudelft.oopp.demo.entities.PaceVote;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PaceVoteRepositoryTests {
    @Autowired
    private PaceVoteRepository paceVoteRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Test
    public void getById_withCorrectId_returnsPaceVote() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        PaceVote vote = new PaceVote();
        vote.setPaceType(PaceType.JUST_RIGHT);
        vote.setQuestionBoard(board);
        paceVoteRepository.save(vote);

        // Act
        PaceVote result = paceVoteRepository.getById(vote.getId());

        // Assert
        assertNotNull(result);
        assertEquals(vote.getId(), result.getId());
        assertEquals(vote.getQuestionBoard().getId(), result.getQuestionBoard().getId());
    }

    @Test
    public void getById_withNonexistentId_returnsNull() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        PaceVote vote = new PaceVote();
        vote.setPaceType(PaceType.JUST_RIGHT);
        vote.setQuestionBoard(board);
        paceVoteRepository.save(vote);

        // Act
        PaceVote result = paceVoteRepository.getById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    public void deletePaceVoteById_withCorrectId_deletesPaceVote() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board");
        questionBoardRepository.save(board);

        PaceVote vote = new PaceVote();
        vote.setPaceType(PaceType.JUST_RIGHT);
        vote.setQuestionBoard(board);
        paceVoteRepository.save(vote);

        // Act
        paceVoteRepository.deletePaceVoteById(vote.getId());

        // Assert
        assertNull(paceVoteRepository.getById(vote.getId()));
    }
}
