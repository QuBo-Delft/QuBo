package nl.tudelft.oopp.qubo.services;

import nl.tudelft.oopp.qubo.dtos.poll.PollCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionResultDto;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import nl.tudelft.oopp.qubo.entities.PollVote;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.PollOptionRepository;
import nl.tudelft.oopp.qubo.repositories.PollRepository;
import nl.tudelft.oopp.qubo.repositories.PollVoteRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ConflictException;
import nl.tudelft.oopp.qubo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "mockCurrentTimeProvider"})
public class PollServiceTests {

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private PollVoteRepository pollVoteRepository;

    @Autowired
    private CurrentTimeProvider mockCurrentTimeProvider;

    @Autowired
    private PollService pollService;

    private final Instant currentInstant = Timestamp.valueOf("2021-04-01 00:00:00").toInstant();
    private final Instant futureInstant = currentInstant.plus(1, ChronoUnit.DAYS);

    private final Timestamp currentStamp = Timestamp.from(currentInstant);
    private final Timestamp futureStamp = Timestamp.from(futureInstant);

    /**
     * In all tests the time is an hour past the currentStamp,
     * and 23 hours before the futureStamp.
     */
    @BeforeEach
    public void setup() {
        Mockito.when(mockCurrentTimeProvider.getCurrentTime())
            .thenReturn(currentInstant.plus(1, ChronoUnit.HOURS));
    }

    // createPoll tests
    @Test
    public void createPoll_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        QuestionBoard qb2 = new QuestionBoard();
        qb2.setTitle("Test board");
        qb2.setModeratorCode(UUID.randomUUID());
        qb2.setStartTime(currentStamp);
        qb2.setClosed(false);
        questionBoardRepository.save(qb2);

        // Prepare model for poll creation
        PollCreationBindingModel pollModel = new PollCreationBindingModel();
        String option1String = "A";
        String option2String = "B";
        String option3String = "C";
        String option4String = "D";
        pollModel.setText("Is this a test poll?");
        pollModel.setPollOptions(Set.of(option1String, option2String, option3String, option4String));

        // Act
        Poll result = pollService.createPoll(pollModel, qb.getId());

        // Assert
        assertEquals(pollModel.getText(), result.getText());
        assertEquals(qb, result.getQuestionBoard());
        Set<String> resultPollOptions =
            result.getPollOptions().stream().map(PollOption::getText).collect(Collectors.toSet());
        assertEquals(pollModel.getPollOptions(), resultPollOptions);
        Poll inDb = pollRepository.getById(result.getId());
        assertEquals(inDb, result);
    }

    @Test
    public void createPoll_withNonExistentBoard_throwsNotFoundException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Prepare model for poll creation
        PollCreationBindingModel pollModel = new PollCreationBindingModel();
        String option1String = "A";
        String option2String = "B";
        String option3String = "C";
        String option4String = "D";
        pollModel.setText("Is this a test poll?");
        pollModel.setPollOptions(Set.of(option1String, option2String, option3String, option4String));

        // Act
        Exception exception = assertThrows(NotFoundException.class,
            () -> pollService.createPoll(pollModel, UUID.randomUUID()));

        // Assert
        assertEquals("Question board does not exist", exception.getMessage());
    }

    // ConflictException
    @Test
    public void createPoll_withAlreadyAssignedPoll_throwsConflictException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Make new poll
        Poll activePoll = new Poll();
        activePoll.setText("Do you want to test the polling mechanism?");
        activePoll.setQuestionBoard(qb);
        activePoll.setOpen(true);
        pollRepository.save(activePoll);

        // Add poll options
        PollOption option1 = new PollOption();
        option1.setPoll(activePoll);
        option1.setText("1");
        PollOption option2 = new PollOption();
        option2.setPoll(activePoll);
        option2.setText("2");
        PollOption option3 = new PollOption();
        option3.setPoll(activePoll);
        option3.setText("3");
        pollOptionRepository.saveAll(Set.of(option1, option2, option3));

        // Prepare model for poll creation
        PollCreationBindingModel pollModel = new PollCreationBindingModel();
        String option1String = "A";
        String option2String = "B";
        String option3String = "C";
        pollModel.setText("Is this a test poll?");
        pollModel.setPollOptions(Set.of(option1String, option2String, option3String));

        // Act
        Exception exception = assertThrows(ConflictException.class,
            () -> pollService.createPoll(pollModel, qb.getId()));

        // Assert
        assertEquals("There is already a poll registered for this question board", exception.getMessage());

    }

    @Test
    public void createPoll_withClosedBoard_throwsForbiddenException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        PollCreationBindingModel pollModel = new PollCreationBindingModel();
        String option1String = "A";
        String option2String = "B";
        String option3String = "C";
        String option4String = "D";
        pollModel.setText("Is this a test poll?");
        pollModel.setPollOptions(Set.of(option1String, option2String, option3String, option4String));

        // Act
        Exception exception = assertThrows(ForbiddenException.class,
            () -> pollService.createPoll(pollModel, qb.getId()));

        // Assert
        assertEquals("Question board is not active", exception.getMessage());
    }

    @Test
    public void createPoll_priorToStartTime_throwsForbiddenException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(futureStamp);
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        PollCreationBindingModel pollModel = new PollCreationBindingModel();
        String option1String = "A";
        String option2String = "B";
        String option3String = "C";
        String option4String = "D";
        pollModel.setText("Is this a test poll?");
        pollModel.setPollOptions(Set.of(option1String, option2String, option3String, option4String));

        // Act
        Exception exception = assertThrows(ForbiddenException.class,
            () -> pollService.createPoll(pollModel, qb.getId()));

        // Assert
        assertEquals("Question board is not active", exception.getMessage());
    }

    // closePoll tests
    @Test
    public void closePoll_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        QuestionBoard qb2 = new QuestionBoard();
        qb2.setTitle("Test board");
        qb2.setModeratorCode(UUID.randomUUID());
        qb2.setStartTime(currentStamp);
        qb2.setClosed(false);
        questionBoardRepository.save(qb2);

        // Make new polls
        Poll activePoll = new Poll();
        activePoll.setText("Do you want to test the polling mechanism?");
        activePoll.setQuestionBoard(qb);
        activePoll.setOpen(true);
        pollRepository.save(activePoll);

        Poll activePoll2 = new Poll();
        activePoll2.setText("Trying to access the wrong poll, you are?");
        activePoll2.setQuestionBoard(qb2);
        activePoll2.setOpen(true);
        pollRepository.save(activePoll2);

        // Add poll options to original activePoll
        PollOption option1 = new PollOption();
        option1.setPoll(activePoll);
        option1.setText("1");
        PollOption option2 = new PollOption();
        option2.setPoll(activePoll);
        option2.setText("2");
        PollOption option3 = new PollOption();
        option3.setPoll(activePoll);
        option3.setText("3");
        pollOptionRepository.saveAll(Set.of(option1, option2, option3));

        // Act
        Poll result = pollService.closePoll(activePoll.getId());

        // Assert
        assertFalse(result.isOpen());
        assertEquals(activePoll.getText(), result.getText());
        assertEquals(activePoll.getQuestionBoard(), result.getQuestionBoard());
        Poll inDb = pollRepository.getById(result.getId());
        assertEquals(inDb, result);
    }

    @Test
    public void closePoll_withNonExistentPoll_throwsNotFoundException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Make new poll
        Poll activePoll = new Poll();
        activePoll.setText("Do you want to test the polling mechanism?");
        activePoll.setQuestionBoard(qb);
        activePoll.setOpen(true);
        pollRepository.save(activePoll);

        // Add poll options to activePoll
        PollOption option1 = new PollOption();
        option1.setPoll(activePoll);
        option1.setText("1");
        PollOption option2 = new PollOption();
        option2.setPoll(activePoll);
        option2.setText("2");
        PollOption option3 = new PollOption();
        option3.setPoll(activePoll);
        option3.setText("3");
        pollOptionRepository.saveAll(Set.of(option1, option2, option3));

        // Act
        Exception exception = assertThrows(NotFoundException.class,
            () -> pollService.closePoll(UUID.randomUUID()));

        // Assert
        assertEquals("Poll does not exist", exception.getMessage());

    }

    @Test
    public void closePoll_withClosedPoll_throwsConflictException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Make new poll
        Poll activePoll = new Poll();
        activePoll.setText("Do you want to test the polling mechanism?");
        activePoll.setQuestionBoard(qb);
        activePoll.setOpen(false);
        pollRepository.save(activePoll);

        // Add poll options to activePoll
        PollOption option1 = new PollOption();
        option1.setPoll(activePoll);
        option1.setText("1");
        PollOption option2 = new PollOption();
        option2.setPoll(activePoll);
        option2.setText("2");
        PollOption option3 = new PollOption();
        option3.setPoll(activePoll);
        option3.setText("3");
        pollOptionRepository.saveAll(Set.of(option1, option2, option3));

        // Act
        Exception exception = assertThrows(ConflictException.class,
            () -> pollService.closePoll(activePoll.getId()));

        // Assert
        assertEquals("The poll has already been closed", exception.getMessage());
    }

    // getPollById tests
    @Test
    public void getPollById_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        QuestionBoard qb2 = new QuestionBoard();
        qb2.setTitle("Test board");
        qb2.setModeratorCode(UUID.randomUUID());
        qb2.setStartTime(currentStamp);
        qb2.setClosed(false);
        questionBoardRepository.save(qb2);

        // Make new polls
        Poll activePoll = new Poll();
        activePoll.setText("Do you want to test the polling mechanism?");
        activePoll.setQuestionBoard(qb);
        activePoll.setOpen(true);
        pollRepository.save(activePoll);

        Poll activePoll2 = new Poll();
        activePoll2.setText("Trying to access the wrong poll, you are?");
        activePoll2.setQuestionBoard(qb2);
        activePoll2.setOpen(true);
        pollRepository.save(activePoll2);

        // Add poll options to original activePoll
        PollOption option1 = new PollOption();
        option1.setPoll(activePoll);
        option1.setText("1");
        PollOption option2 = new PollOption();
        option2.setPoll(activePoll);
        option2.setText("2");
        PollOption option3 = new PollOption();
        option3.setPoll(activePoll);
        option3.setText("3");
        pollOptionRepository.saveAll(Set.of(option1, option2, option3));

        // Act
        Poll result = pollService.getPollById(activePoll.getId());

        // Assert
        assertEquals(activePoll, result);
    }

    @Test
    public void getPollById_withNonExistentPoll_returnsNull() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Make new poll
        Poll activePoll = new Poll();
        activePoll.setText("Do you want to test the polling mechanism?");
        activePoll.setQuestionBoard(qb);
        activePoll.setOpen(false);
        pollRepository.save(activePoll);

        // Act
        Poll result = pollService.getPollById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    // getPollOptionById tests
    @Test
    public void getPollOptionById_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        QuestionBoard qb2 = new QuestionBoard();
        qb2.setTitle("Test board");
        qb2.setModeratorCode(UUID.randomUUID());
        qb2.setStartTime(currentStamp);
        qb2.setClosed(false);

        questionBoardRepository.saveAll(Set.of(qb, qb2));

        // Make new polls
        Poll activePoll = new Poll();
        activePoll.setText("Do you want to test the polling mechanism?");
        activePoll.setQuestionBoard(qb);
        activePoll.setOpen(true);
        Poll activePoll2 = new Poll();
        activePoll2.setText("Trying to access the wrong poll, you are?");
        activePoll2.setQuestionBoard(qb2);
        activePoll2.setOpen(true);

        pollRepository.saveAll(Set.of(activePoll, activePoll2));

        // Add poll options for activePoll
        PollOption option1 = new PollOption();
        option1.setPoll(activePoll);
        option1.setText("1");
        PollOption option2 = new PollOption();
        option2.setPoll(activePoll);
        option2.setText("2");
        // add poll options for activePoll2
        PollOption option3 = new PollOption();
        option3.setPoll(activePoll2);
        option3.setText("A");
        PollOption option4 = new PollOption();
        option4.setPoll(activePoll2);
        option4.setText("B");

        pollOptionRepository.saveAll(Set.of(option1, option2, option3, option4));

        // Act
        PollOption result = pollService.getPollOptionById(option1.getId());

        // Assert
        assertEquals(option1, result);
    }

    @Test
    public void getPollOptionById_withNonExistentPoll_returnsNull() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Make new poll
        Poll activePoll = new Poll();
        activePoll.setText("Do you want to test the polling mechanism?");
        activePoll.setQuestionBoard(qb);
        activePoll.setOpen(true);
        pollRepository.save(activePoll);

        // Add poll options for activePoll
        PollOption option1 = new PollOption();
        option1.setPoll(activePoll);
        option1.setText("1");
        PollOption option2 = new PollOption();
        option2.setPoll(activePoll);
        option2.setText("2");

        // Act
        Poll result = pollService.getPollById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    // deletePoll tests
    @Test
    public void deletePoll_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        QuestionBoard qb2 = new QuestionBoard();
        qb2.setTitle("Test board");
        qb2.setModeratorCode(UUID.randomUUID());
        qb2.setStartTime(currentStamp);
        qb2.setClosed(false);

        questionBoardRepository.saveAll(Set.of(qb, qb2));

        // Make new polls
        Poll activePoll = new Poll();
        activePoll.setText("Do you want to test the polling mechanism?");
        activePoll.setQuestionBoard(qb);
        activePoll.setOpen(true);
        Poll activePoll2 = new Poll();
        activePoll2.setText("Trying to access the wrong poll, you are?");
        activePoll2.setQuestionBoard(qb2);
        activePoll2.setOpen(true);

        pollRepository.saveAll(Set.of(activePoll, activePoll2));

        // Add poll options for activePoll
        PollOption option1 = new PollOption();
        option1.setPoll(activePoll);
        option1.setText("1");
        PollOption option2 = new PollOption();
        option2.setPoll(activePoll);
        option2.setText("2");
        // add poll options for activePoll2
        PollOption option3 = new PollOption();
        option3.setPoll(activePoll2);
        option3.setText("A");
        PollOption option4 = new PollOption();
        option4.setPoll(activePoll2);
        option4.setText("B");

        pollOptionRepository.saveAll(Set.of(option1, option2, option3, option4));

        // Act
        pollService.deletePoll(activePoll.getId());

        // Assert
        Poll result = pollRepository.getById(activePoll.getId());
        assertNull(result);

        Poll remainingPoll = pollRepository.getById(activePoll2.getId());
        assertNotNull(remainingPoll);
    }

    // getPollResults tests
    @Test
    public void getPollResults_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(false);
        QuestionBoard qb2 = new QuestionBoard();
        qb2.setTitle("Test board");
        qb2.setModeratorCode(UUID.randomUUID());
        qb2.setStartTime(currentStamp);
        qb2.setClosed(false);

        questionBoardRepository.saveAll(Set.of(qb, qb2));

        // Make new polls
        Poll activePoll = new Poll();
        activePoll.setText("Do you want to test the polling mechanism?");
        activePoll.setQuestionBoard(qb);
        activePoll.setOpen(false);
        Poll activePoll2 = new Poll();
        activePoll2.setText("Trying to access the wrong poll, you are?");
        activePoll2.setQuestionBoard(qb2);
        activePoll2.setOpen(false);

        pollRepository.saveAll(Set.of(activePoll, activePoll2));

        // Add poll options for activePoll
        PollOption option1 = new PollOption();
        option1.setPoll(activePoll);
        option1.setText("1");
        PollOption option2 = new PollOption();
        option2.setPoll(activePoll);
        option2.setText("2");
        // add poll options for activePoll2
        PollOption option3 = new PollOption();
        option3.setPoll(activePoll2);
        option3.setText("A");
        PollOption option4 = new PollOption();
        option4.setPoll(activePoll2);
        option4.setText("B");

        pollOptionRepository.saveAll(Set.of(option1, option2, option3, option4));

        // Add poll votes to both polls
        for (int i = 0; i < 8; i++) {
            PollVote vote = new PollVote();
            vote.setPollOption(option1);
            pollVoteRepository.save(vote);
        }
        for (int i = 0; i < 6; i++) {
            PollVote vote = new PollVote();
            vote.setPollOption(option2);
            pollVoteRepository.save(vote);
        }
        for (int i = 0; i < 4; i++) {
            PollVote vote = new PollVote();
            vote.setPollOption(option3);
            pollVoteRepository.save(vote);
        }
        for (int i = 0; i < 2; i++) {
            PollVote vote = new PollVote();
            vote.setPollOption(option4);
            pollVoteRepository.save(vote);
        }

        // Set up expected result
        PollOptionResultDto option1expectation = new PollOptionResultDto();
        option1expectation.setText(option1.getText());
        option1expectation.setId(option1.getId());
        option1expectation.setVotes(8);

        PollOptionResultDto option2expectation = new PollOptionResultDto();
        option2expectation.setText(option2.getText());
        option2expectation.setId(option2.getId());
        option2expectation.setVotes(6);

        Set<PollOptionResultDto> expected = Set.of(option1expectation, option2expectation);

        // Act
        Set<PollOptionResultDto> result = pollService.getPollResults(qb.getId());

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void getPollResults_withNonExistentBoard_throwsNotFoundException() {
        /// Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        // Act
        Exception exception = assertThrows(NotFoundException.class,
            () -> pollService.getPollResults(UUID.randomUUID()));

        // Assert
        assertEquals("The question board does not exist.", exception.getMessage());
    }


    @Test
    public void getPollResults_withNonExistentPoll_throwsNotFoundException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        // Act
        Exception exception = assertThrows(NotFoundException.class,
            () -> pollService.getPollResults(qb.getId()));

        // Assert
        assertEquals("There is no poll in this question board.", exception.getMessage());
    }

    @Test
    public void getPollResults_withOpenPoll_throwsForbiddenException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setTitle("Test board");
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(currentStamp);
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        Poll activePoll = new Poll();
        activePoll.setText("Do you want to test the polling mechanism?");
        activePoll.setQuestionBoard(qb);
        activePoll.setOpen(true);
        pollRepository.save(activePoll);

        // Act
        Exception exception = assertThrows(ForbiddenException.class,
            () -> pollService.getPollResults(qb.getId()));

        // Assert
        assertEquals("The poll is open.", exception.getMessage());
    }

}
