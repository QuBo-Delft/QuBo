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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext
public class PaceVoteServiceTests {

    @Autowired
    private PaceVoteRepository paceVoteRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private PaceVoteService paceVoteService;

    @Test
    public void paceVoteRegistration_withValidData_worksCorrectly() {
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
    public void paceVoteRegistration_withNonExistentBoard_throwsNotFoundException() {
        // Arrange
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
    public void paceVoteRegistration_withNonActiveQuestionBoard_trowsForbiddenException() {
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
        assertEquals(exception.getMessage(), "Question board is not active");
    }

    @Test
    public void getPaceVoteById_withExistentPaceVote_worksCorrectly() {
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
        assertEquals(result.getQuestionBoard().getId(), qb.getId());
        assertEquals(result.getPaceType().toString(), model.getPaceType().toString());
        assertEquals(result.getPaceType().toString(), vote.getPaceType().toString());

    }

    @Test
    public void getPaceVoteById_withNonExistentPaceVote_returnsNull() {
        // Arrange
        // Act
        PaceVote result = paceVoteService.getById(UUID.randomUUID());
        // Assert
        assertNull(result);
    }

    @Test
    public void deletePaceVoteById_withExistentPaceVote_removesPaceVoteFromRepository() {
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
        PaceVote toBeDeleted = paceVoteRepository.getById(vote.getId());
        assertNotNull(toBeDeleted);
        paceVoteService.deleteVote(toBeDeleted);

        // Assert
        assertNull(paceVoteRepository.getById(toBeDeleted.getId()));
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

        // Act
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
        PaceDetailsDto result = paceVoteService.getAggregatedVotes(qb.getId());

        // Assert
        assertEquals(result.getTooFastVotes(), 4);
        assertEquals(result.getJustRightVotes(), 6);
        assertEquals(result.getTooSlowVotes(), 3);
    }

    @Test
    public void getAggregatedVotes_withEmptyDatabase_returnsCorrectAmounts() {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-04-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        PaceDetailsDto result = paceVoteService.getAggregatedVotes(qb.getId());

        // Assert
        assertEquals(result.getTooFastVotes(), 0);
        assertEquals(result.getJustRightVotes(), 0);
        assertEquals(result.getTooSlowVotes(), 0);
    }

    @Test
    public void getAggregatedVotes_withNonExistentQuestionBoard_throwsNotFoundException() {
        // Arrange
        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> paceVoteService.getAggregatedVotes(UUID.randomUUID())
        );
        // Assert
        assertEquals("Question board does not exist", exception.getMessage());
    }


}
