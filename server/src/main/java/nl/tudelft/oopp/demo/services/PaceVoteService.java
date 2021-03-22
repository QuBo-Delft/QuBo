package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.demo.entities.PaceVote;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.PaceVoteRepository;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.demo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.demo.services.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.time.Instant;
import java.util.UUID;

@Service
public class PaceVoteService {
    private final PaceVoteRepository paceVoteRepository;
    private final QuestionBoardRepository questionBoardRepository;

    private final ModelMapper modelMapper;

    /**
     * Creates an instance of the PaceVoteService.
     *
     * @param paceVoteRepository      The PaceVoteRepository
     * @param questionBoardRepository The QuestionBoardRepository
     * @param modelMapper             The ModelMapper
     */
    public PaceVoteService(
            PaceVoteRepository paceVoteRepository, QuestionBoardRepository questionBoardRepository,
            ModelMapper modelMapper) {
        this.paceVoteRepository = paceVoteRepository;
        this.questionBoardRepository = questionBoardRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Method for registering PaceVotes by
     * adding a new PaceVote to the database.
     *
     * @param paceVoteModel The pace vote model.
     * @param boardId       The board ID.
     * @return The PaceVote object that was just registered.
     * @throws NotFoundException if QuestionBoard provided does not exist.
     * @throws ForbiddenException if
     */
    public PaceVote registerVote(PaceVoteCreationBindingModel paceVoteModel, UUID boardId) {
        QuestionBoard board = questionBoardRepository.getById(boardId);
        // Check if QuestionBoard with this ID exists
        if (board == null) {
            throw new NotFoundException("Question board does not exist");
        }

        // Check if board is active
        Instant now = Instant.now();
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
     * @param paceVoteId the pace vote id.
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


}
