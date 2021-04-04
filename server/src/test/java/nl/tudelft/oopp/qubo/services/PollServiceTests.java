package nl.tudelft.oopp.qubo.services;

import nl.tudelft.oopp.qubo.entities.PollVote;
import nl.tudelft.oopp.qubo.repositories.PollRepository;
import nl.tudelft.oopp.qubo.repositories.PollVoteRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.services.profiles.MockCurrentTimeProviderConfiguration;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "mockCurrentTimeProvider"})
public class PollServiceTests {

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private PollVoteRepository pollVoteRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private CurrentTimeProvider mockCurrentTimeProvider;

    @Autowired
    private PollService pollService;

    private final Timestamp currentStamp = Timestamp.valueOf("2021-04-01 00:00:00");

    private final Instant pastInstant = currentStamp.toInstant().minus(1, ChronoUnit.DAYS);
    private final Instant currentInstant = currentStamp.toInstant();
    private final Instant futureInstant = currentStamp.toInstant().plus(1, ChronoUnit.DAYS);

    @BeforeEach
    public void setup() {
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(currentInstant);
    }

    // createPoll tests
    @Test
    public void createPoll_withValidData_worksCorrectly() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    public void createPoll_withNonExistentBoard_throwsNotFoundException() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    public void createPoll_withClosedBoard_throwsForbiddenException() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    public void createPoll_priorToStartTime_throwsForbiddenException() {
        // Arrange

        // Act

        // Assert

    }

    // closePoll tests
    @Test
    public void closePoll_withValidData_worksCorrectly() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    public void closePoll_withNonExistentBoard_throwsNotFoundException() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    public void closePoll_withClosedBoard_throwsForbiddenException() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    public void closePoll_priorToStartTime_throwsForbiddenException() {
        // Arrange

        // Act

        // Assert

    }

    // getPollById tests
    @Test
    public void getPollById_withValidData_worksCorrectly() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    public void getPollById_withNonExistentPoll_returnsNull() {
        // Arrange

        // Act

        // Assert

    }

    // getPollOptionById tests
    @Test
    public void getPollOptionById_withValidData_worksCorrectly() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    public void getPollOptionById_withNonExistentPoll_returnsNull() {
        // Arrange

        // Act

        // Assert

    }

    // deletePoll tests
    @Test
    public void deletePoll_withValidData_worksCorrectly() {
        // Arrange

        // Act

        // Assert

    }

    // getPollResults tests
    @Test
    public void getPollResults_withValidData_worksCorrectly() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    public void getPollResults_withNonExistentBoard_throwsNotFoundException() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    public void getPollResults_withNonExistentPoll_throwsNotFoundException() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    public void getPollResults_withOpenPoll_throwsForbiddenException() {
        // Arrange

        // Act

        // Assert

    }

}
