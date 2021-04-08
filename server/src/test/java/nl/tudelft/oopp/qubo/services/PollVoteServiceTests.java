package nl.tudelft.oopp.qubo.services;

import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import nl.tudelft.oopp.qubo.entities.PollVote;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.PollOptionRepository;
import nl.tudelft.oopp.qubo.repositories.PollRepository;
import nl.tudelft.oopp.qubo.repositories.PollVoteRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.tudelft.oopp.qubo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "mockCurrentTimeProvider"})
public class PollVoteServiceTests {
    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private PollVoteRepository pollVoteRepository;

    @Autowired
    private PollVoteService pollVoteService;

    private Timestamp now;

    @BeforeEach
    public void setup() {
        now = Timestamp.from(Instant.now());
    }

    @Test
    public void registerVote_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard questionBoard = new QuestionBoard();
        questionBoard.setModeratorCode(UUID.randomUUID());
        questionBoard.setTitle("Test Board");
        questionBoard.setStartTime(now);
        questionBoard.setClosed(false);
        questionBoardRepository.save(questionBoard);

        Poll poll = new Poll();
        poll.setText("Test Poll");
        poll.setOpen(true);
        poll.setQuestionBoard(questionBoard);
        pollRepository.save(poll);

        PollOption pollOption1 = new PollOption();
        pollOption1.setText("Option 1");
        pollOption1.setPoll(poll);
        pollOptionRepository.save(pollOption1);

        // Act
        PollVote pollVote = pollVoteService.registerVote(pollOption1.getId());

        // Assert
        assertNotNull(pollVote);
        assertEquals(pollVote.getPollOption().getId(), pollOption1.getId());
    }

    @Test
    public void registerVote_withNonExistentPoll_returnsNotFoundException() {
        // Act
        Exception exception = assertThrows(NotFoundException.class,
            () -> pollVoteService.registerVote(UUID.randomUUID()));

        // Assert
        assertEquals("Poll option does not exist", exception.getMessage());
    }

    @Test
    public void registerVote_withClosedPoll_returnsForbiddenException() {
        // Arrange
        QuestionBoard questionBoard = new QuestionBoard();
        questionBoard.setModeratorCode(UUID.randomUUID());
        questionBoard.setTitle("Test Board");
        questionBoard.setStartTime(now);
        questionBoard.setClosed(false);
        questionBoardRepository.save(questionBoard);

        Poll poll = new Poll();
        poll.setText("Test Poll");
        poll.setOpen(false);
        poll.setQuestionBoard(questionBoard);
        pollRepository.save(poll);

        PollOption pollOption1 = new PollOption();
        pollOption1.setText("Option 1");
        pollOption1.setPoll(poll);
        pollOptionRepository.save(pollOption1);

        // Act
        Exception exception = assertThrows(ForbiddenException.class,
            () -> pollVoteService.registerVote(pollOption1.getId()));

        // Assert
        assertEquals("Poll is not active", exception.getMessage());
    }

    @Test
    public void getPollVote_withValidDate_worksCorrectly() {
        // Arrange
        QuestionBoard questionBoard = new QuestionBoard();
        questionBoard.setModeratorCode(UUID.randomUUID());
        questionBoard.setTitle("Test Board");
        questionBoard.setStartTime(now);
        questionBoard.setClosed(false);
        questionBoardRepository.save(questionBoard);

        Poll poll = new Poll();
        poll.setText("Test Poll");
        poll.setOpen(false);
        poll.setQuestionBoard(questionBoard);
        pollRepository.save(poll);

        PollOption pollOption1 = new PollOption();
        pollOption1.setText("Option 1");
        pollOption1.setPoll(poll);
        pollOptionRepository.save(pollOption1);

        PollVote pollVote1 = new PollVote();
        pollVote1.setPollOption(pollOption1);
        pollVoteRepository.save(pollVote1);

        // Act
        PollVote result = pollVoteService.getPollVote(pollVote1.getId());

        // Assert
        assertEquals(pollVote1, result);
    }

    @Test
    public void getPollVote_withNonExistentPollVote_returnsNull() {
        // Act
        PollVote result = pollVoteService.getPollVote(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    public void deletePollVote_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard questionBoard = new QuestionBoard();
        questionBoard.setModeratorCode(UUID.randomUUID());
        questionBoard.setTitle("Test Board");
        questionBoard.setStartTime(now);
        questionBoard.setClosed(false);
        questionBoardRepository.save(questionBoard);

        Poll poll = new Poll();
        poll.setText("Test Poll");
        poll.setOpen(false);
        poll.setQuestionBoard(questionBoard);
        pollRepository.save(poll);

        PollOption pollOption1 = new PollOption();
        pollOption1.setText("Option 1");
        pollOption1.setPoll(poll);
        pollOptionRepository.save(pollOption1);

        PollVote pollVote1 = new PollVote();
        pollVote1.setPollOption(pollOption1);
        pollVoteRepository.save(pollVote1);

        // Act
        pollVoteService.deletePollVote(pollVote1.getId());

        // Assert
        assertNull(pollVoteRepository.getById(pollVote1.getId()));
    }

    @Test
    public void deletePollVote_withNonExistentPollVoteId_doesNotDeleteExistingPollVote() {
        // Arrange
        QuestionBoard questionBoard = new QuestionBoard();
        questionBoard.setModeratorCode(UUID.randomUUID());
        questionBoard.setTitle("Test Board");
        questionBoard.setStartTime(now);
        questionBoard.setClosed(false);
        questionBoardRepository.save(questionBoard);

        Poll poll = new Poll();
        poll.setText("Test Poll");
        poll.setOpen(false);
        poll.setQuestionBoard(questionBoard);
        pollRepository.save(poll);

        PollOption pollOption1 = new PollOption();
        pollOption1.setText("Option 1");
        pollOption1.setPoll(poll);
        pollOptionRepository.save(pollOption1);

        PollVote pollVote1 = new PollVote();
        pollVote1.setPollOption(pollOption1);
        pollVoteRepository.save(pollVote1);

        // Act
        pollVoteService.deletePollVote(UUID.randomUUID());

        // Assert
        assertNotNull(pollVoteRepository.getById(pollVote1.getId()));
    }
}
