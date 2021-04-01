package nl.tudelft.oopp.qubo.services;

import nl.tudelft.oopp.qubo.dtos.pace.PaceDetailsDto;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.PaceVote;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.PaceVoteRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.management.timer.TimerMBean;
import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("mockCurrentTimeProvider")
public class PaceVoteServiceTests {

    @Autowired
    private PaceVoteRepository paceVoteRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private PaceVoteService paceVoteService;

    @Autowired
    private CurrentTimeProvider mockCurrentTimeProvider;

    @Test
    public void registerVote_withValidData_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-04-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        PaceVoteCreationBindingModel creationModel = new PaceVoteCreationBindingModel();
        creationModel.setPaceType(PaceType.JUST_RIGHT);

        // Act
        PaceVote result = paceVoteService.registerVote(creationModel, qb.getId());

        // Assert
        PaceVote voteInDatabase = paceVoteRepository.getById(result.getId());
        assertNotNull(result);
        assertEquals(creationModel.getPaceType().toString(), voteInDatabase.getPaceType().toString());
        assertEquals(qb.getId(), voteInDatabase.getQuestionBoard().getId());
        assertEquals(result.getPaceType().toString(), voteInDatabase.getPaceType().toString());

    }

    @Test
    public void registerVote_withNonExistentBoard_throwsNotFoundException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-04-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);
        PaceVoteCreationBindingModel creationModel = new PaceVoteCreationBindingModel();
        creationModel.setPaceType(PaceType.JUST_RIGHT);

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> paceVoteService.registerVote(creationModel, UUID.randomUUID())
        );
        // Assert
        assertEquals("Question board does not exist", exception.getMessage());
    }

    @Test
    public void registerVote_withClosedQuestionBoard_throwsForbiddenException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-04-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        PaceVoteCreationBindingModel creationModel = new PaceVoteCreationBindingModel();
        creationModel.setPaceType(PaceType.JUST_RIGHT);

        // Act
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> paceVoteService.registerVote(creationModel, qb.getId())
        );
        // Assert
        assertEquals("Question board is not active", exception.getMessage());
    }

    @Test
    public void registerVote_withNonActiveQuestionBoard_throwsForbiddenException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        Timestamp testTime = Timestamp.valueOf("2021-04-01 00:00:00");
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(testTime.toInstant());
        qb.setStartTime(Timestamp.valueOf("2021-05-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        PaceVoteCreationBindingModel creationModel = new PaceVoteCreationBindingModel();
        creationModel.setPaceType(PaceType.JUST_RIGHT);

        // Act
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> paceVoteService.registerVote(creationModel, qb.getId())
        );
        // Assert
        assertEquals("Question board is not active", exception.getMessage());
    }

    @Test
    public void getById_withExistentPaceVote_worksCorrectly() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-04-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        ModelMapper modelMapper = new ModelMapper();
        PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
        model.setPaceType(PaceType.JUST_RIGHT);
        PaceVote vote = modelMapper.map(model, PaceVote.class);
        vote.setQuestionBoard(qb);
        paceVoteRepository.save(vote);

        // Act
        PaceVote result = paceVoteService.getById(vote.getId());
        // Assert
        assertEquals(qb.getId(), result.getQuestionBoard().getId());
        assertEquals(model.getPaceType().toString(), result.getPaceType().toString());
        assertEquals(vote.getPaceType().toString(), result.getPaceType().toString());

    }

    @Test
    public void getById_withNonExistentPaceVote_returnsNull() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-04-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Add a paceVote to the database
        ModelMapper modelMapper = new ModelMapper();
        PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
        model.setPaceType(PaceType.JUST_RIGHT);
        PaceVote vote = modelMapper.map(model, PaceVote.class);
        vote.setQuestionBoard(qb);
        paceVoteRepository.save(vote);
        // Act
        PaceVote result = paceVoteService.getById(UUID.randomUUID());
        // Assert
        assertNull(result);
    }

    @Test
    public void deleteById_withExistentPaceVote_removesPaceVoteFromRepository() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-04-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Add a paceVote to the database
        ModelMapper modelMapper = new ModelMapper();
        PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
        model.setPaceType(PaceType.JUST_RIGHT);
        PaceVote vote = modelMapper.map(model, PaceVote.class);
        vote.setQuestionBoard(qb);
        paceVoteRepository.save(vote);

        // Act
        paceVoteService.deleteVote(vote);

        // Assert
        assertNull(paceVoteRepository.getById(vote.getId()));
    }

    @Test
    public void getAggregatedVotes_withValidData_returnsCorrectAmounts() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-04-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);
        ModelMapper modelMapper = new ModelMapper();

        for (int i = 0; i < 4; i++) {
            PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
            model.setPaceType(PaceType.TOO_FAST);
            PaceVote vote = modelMapper.map(model, PaceVote.class);
            vote.setQuestionBoard(qb);
            paceVoteRepository.save(vote);
        }
        for (int i = 0; i < 6; i++) {
            PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
            model.setPaceType(PaceType.JUST_RIGHT);
            PaceVote vote = modelMapper.map(model, PaceVote.class);
            vote.setQuestionBoard(qb);
            paceVoteRepository.save(vote);
        }
        for (int i = 0; i < 3; i++) {
            PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
            model.setPaceType(PaceType.TOO_SLOW);
            PaceVote vote = modelMapper.map(model, PaceVote.class);
            vote.setQuestionBoard(qb);
            paceVoteRepository.save(vote);
        }

        // Act
        PaceDetailsDto result = paceVoteService.getAggregatedVotes(qb.getId());

        // Assert
        assertEquals(4, result.getTooFastVotes());
        assertEquals(6,result.getJustRightVotes());
        assertEquals(3, result.getTooSlowVotes());
    }

    @Test
    public void getAggregatedVotes_withEmptyDatabase_returnsCorrectAmounts() {
        // Arrange
        QuestionBoard filledUpBoard = new QuestionBoard();
        filledUpBoard.setModeratorCode(UUID.randomUUID());
        filledUpBoard.setStartTime(Timestamp.valueOf("2021-04-01 00:00:00"));
        filledUpBoard.setTitle("Test board");
        filledUpBoard.setClosed(false);
        questionBoardRepository.save(filledUpBoard);
        ModelMapper modelMapper = new ModelMapper();
        for (int i = 0; i < 3; i++) {
            PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
            model.setPaceType(PaceType.JUST_RIGHT);
            PaceVote vote = modelMapper.map(model, PaceVote.class);
            vote.setQuestionBoard(filledUpBoard);
            paceVoteRepository.save(vote);
        }
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-04-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        PaceDetailsDto result = paceVoteService.getAggregatedVotes(qb.getId());

        // Assert
        assertEquals(0,result.getTooFastVotes());
        assertEquals(0,result.getJustRightVotes());
        assertEquals(0,result.getTooSlowVotes());
    }

    @Test
    public void getAggregatedVotes_withNonExistentQuestionBoard_throwsNotFoundException() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-04-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);
        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> paceVoteService.getAggregatedVotes(UUID.randomUUID())
        );
        // Assert
        assertEquals("Question board does not exist", exception.getMessage());
    }


}
