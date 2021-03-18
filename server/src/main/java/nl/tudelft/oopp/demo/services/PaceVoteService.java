package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.demo.entities.PaceVote;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.PaceVoteRepository;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.demo.services.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

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
     * @param boardId       The board ID.
     * @param paceVoteModel The pace vote model.
     * @return The PaceVote object that was just registered.
     * @throws NotFoundException when QuestionBoard provided does not exist.
     */
    public PaceVote registerVote(UUID boardId, PaceVoteCreationBindingModel paceVoteModel) {
        QuestionBoard board = questionBoardRepository.getById(boardId);
        if (board == null) {
            throw new NotFoundException("Question board does not exist");
        }
        PaceVote vote = modelMapper.map(paceVoteModel, PaceVote.class);
        vote.setQuestionBoard(board);
        paceVoteRepository.save(vote);
        return vote;
    }

}
