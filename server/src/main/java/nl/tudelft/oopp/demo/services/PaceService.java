package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.entities.PaceVote;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.PaceVoteRepository;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class PaceService {
    private final PaceVoteRepository paceVoteRepository;
    private final QuestionBoardRepository questionBoardRepository;

    private final ModelMapper modelMapper;

    /**
     * Creates an instance of the PaceService.
     *
     * @param paceVoteRepository        The PaceVoteRepository
     * @param questionBoardRepository   The QuestionBoardRepository
     * @param modelMapper               The ModelMapper
     */
    public PaceService(PaceVoteRepository paceVoteRepository,
            QuestionBoardRepository questionBoardRepository, ModelMapper modelMapper) {
        this.paceVoteRepository = paceVoteRepository;
        this.questionBoardRepository = questionBoardRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Gets all pace votes of a question board.
     *
     * @param boardId   The board ID whose pace details should be retrieved.
     * @return The corresponding set of PaceVote objects or null.
     */
    public Set<PaceVote> getPaceVotes(UUID boardId) {
        QuestionBoard qb = questionBoardRepository.getById(boardId);
        if (qb == null) {
            return null;
        }
        // Find questions belonging to this board
        return paceVoteRepository.getPaceVotesByQuestionBoard(qb);
    }
}
