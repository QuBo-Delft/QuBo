package nl.tudelft.oopp.qubo.services;

import java.time.Instant;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.pace.PaceDetailsDto;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.PaceType;
import nl.tudelft.oopp.qubo.entities.PaceVote;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.PaceVoteRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * The Pace vote service.
 */
@Service
public class PaceVoteService {
    private final PaceVoteRepository paceVoteRepository;

    private final QuestionBoardRepository questionBoardRepository;

    private final ModelMapper modelMapper;

    private final CurrentTimeProvider currentTimeProvider;

    /**
     * Creates an instance of the PaceVoteService.
     *
     * @param paceVoteRepository      The PaceVoteRepository
     * @param questionBoardRepository The QuestionBoardRepository
     * @param modelMapper             The ModelMapper
     * @param currentTimeProvider     The CurrentTimeProvider.
     */
    public PaceVoteService(
        PaceVoteRepository paceVoteRepository, QuestionBoardRepository questionBoardRepository,
        ModelMapper modelMapper,
        CurrentTimeProvider currentTimeProvider) {
        this.paceVoteRepository = paceVoteRepository;
        this.questionBoardRepository = questionBoardRepository;
        this.modelMapper = modelMapper;
        this.currentTimeProvider = currentTimeProvider;
    }

    /**
     * Method for registering PaceVotes by
     * adding a new PaceVote to the database.
     *
     * @param paceVoteModel The pace vote model.
     * @param boardId       The board ID.
     * @return The PaceVote object that was just registered.
     * @throws NotFoundException  if the provided QuestionBoard does not exist.
     * @throws ForbiddenException if the QuestionBoard is not active.
     */
    public PaceVote registerVote(PaceVoteCreationBindingModel paceVoteModel, UUID boardId) {
        QuestionBoard board = questionBoardRepository.getById(boardId);
        // Check if QuestionBoard with this ID exists
        if (board == null) {
            throw new NotFoundException("Question board does not exist");
        }

        // Check if board is active
        Instant now = currentTimeProvider.getCurrentTime();
        if (now.isBefore(board.getStartTime().toInstant())
            || board.isClosed()) {
            throw new ForbiddenException("Question board is not active");
        }

        PaceVote vote = modelMapper.map(paceVoteModel, PaceVote.class);
        vote.setQuestionBoard(board);
        paceVoteRepository.save(vote);
        return vote;
    }

    /**
     * Gets PaceVote by id.
     *
     * @param paceVoteId The pace vote id..
     * @return corresponding PaceVote object or null.
     */
    public PaceVote getById(UUID paceVoteId) {
        return paceVoteRepository.getById(paceVoteId);
    }

    /**
     * Delete PaceVote from database.
     *
     * @param vote The vote that is to be deleted.
     */
    public void deleteVote(PaceVote vote) {
        // Delete paceVote from database
        this.paceVoteRepository.deletePaceVoteById(vote.getId());
    }

    /**
     * Gets the number of PaceVotes for a given board, grouped by type.
     *
     * @param boardId The board id.
     * @return A DTO containing the number of PaceVotes.
     */
    public PaceDetailsDto getAggregatedVotes(UUID boardId) {
        QuestionBoard board = questionBoardRepository.getById(boardId);
        // Check if QuestionBoard with this ID exists
        if (board == null) {
            throw new NotFoundException("Question board does not exist");
        }

        int justRight = paceVoteRepository.countByQuestionBoardAndPaceType(board, PaceType.JUST_RIGHT);
        int tooSlow = paceVoteRepository.countByQuestionBoardAndPaceType(board, PaceType.TOO_SLOW);
        int tooFast = paceVoteRepository.countByQuestionBoardAndPaceType(board, PaceType.TOO_FAST);

        PaceDetailsDto dto = new PaceDetailsDto(justRight, tooFast, tooSlow);

        return dto;
    }
}
