package nl.tudelft.oopp.qubo.repositories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import nl.tudelft.oopp.qubo.entities.PaceType;
import nl.tudelft.oopp.qubo.entities.PaceVote;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
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

    @Test
    public void countByQuestionBoardAndPaceType_withPaceVotes_returnsCorrectCount() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board 1");
        questionBoardRepository.save(board);

        PaceVote vote1 = new PaceVote();
        vote1.setPaceType(PaceType.TOO_FAST);
        vote1.setQuestionBoard(board);
        paceVoteRepository.save(vote1);

        PaceVote vote2 = new PaceVote();
        vote2.setPaceType(PaceType.TOO_FAST);
        vote2.setQuestionBoard(board);
        paceVoteRepository.save(vote2);

        PaceVote vote3 = new PaceVote();
        vote3.setPaceType(PaceType.JUST_RIGHT);
        vote3.setQuestionBoard(board);
        paceVoteRepository.save(vote3);

        QuestionBoard board2 = new QuestionBoard();
        board2.setModeratorCode(UUID.randomUUID());
        board2.setStartTime(Timestamp.from(Instant.now()));
        board2.setTitle("Test board 2");
        questionBoardRepository.save(board2);

        PaceVote vote4 = new PaceVote();
        vote4.setPaceType(PaceType.TOO_FAST);
        vote4.setQuestionBoard(board2);
        paceVoteRepository.save(vote4);

        // Act
        int result = paceVoteRepository.countByQuestionBoardAndPaceType(board, PaceType.TOO_FAST);

        // Assert
        assertEquals(2, result);
    }

    @Test
    public void countByQuestionBoardAndPaceType_withNoPaceVotes_returnsZero() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        board.setModeratorCode(UUID.randomUUID());
        board.setStartTime(Timestamp.from(Instant.now()));
        board.setTitle("Test board 1");
        questionBoardRepository.save(board);

        PaceVote vote3 = new PaceVote();
        vote3.setPaceType(PaceType.JUST_RIGHT);
        vote3.setQuestionBoard(board);
        paceVoteRepository.save(vote3);

        QuestionBoard board2 = new QuestionBoard();
        board2.setModeratorCode(UUID.randomUUID());
        board2.setStartTime(Timestamp.from(Instant.now()));
        board2.setTitle("Test board 2");
        questionBoardRepository.save(board2);

        PaceVote vote4 = new PaceVote();
        vote4.setPaceType(PaceType.TOO_FAST);
        vote4.setQuestionBoard(board2);
        paceVoteRepository.save(vote4);

        // Act
        int result = paceVoteRepository.countByQuestionBoardAndPaceType(board, PaceType.TOO_FAST);

        // Assert
        assertEquals(0, result);
    }
}
