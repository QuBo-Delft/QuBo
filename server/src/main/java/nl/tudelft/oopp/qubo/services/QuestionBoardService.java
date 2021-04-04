package nl.tudelft.oopp.qubo.services;

import java.util.Set;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ConflictException;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


/**
 * The Question board service.
 */
@Service
public class QuestionBoardService {
    private final QuestionBoardRepository questionBoardRepository;

    private final QuestionRepository questionRepository;

    private final ModelMapper modelMapper;

    /**
     * Creates an instance of a QuestionBoardService.
     *
     * @param questionBoardRepository A QuestionBoardRepository.
     * @param questionRepository      A QuestionRepository.
     * @param modelMapper             The ModelMapper.
     */
    public QuestionBoardService(
        QuestionBoardRepository questionBoardRepository, QuestionRepository questionRepository,
        ModelMapper modelMapper) {
        this.questionBoardRepository = questionBoardRepository;
        this.questionRepository = questionRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Save board to database.
     * Converts the binding model to a QuestionBoard entity,
     * generates a random moderator code, and saves
     * it into the database.
     *
     * @param boardModel The board model.
     * @return The QuestionBoard that was created.
     */
    public QuestionBoard saveBoard(QuestionBoardCreationBindingModel boardModel) {
        QuestionBoard board = modelMapper.map(boardModel, QuestionBoard.class);
        board.setModeratorCode(UUID.randomUUID());
        questionBoardRepository.save(board);
        return board;
    }

    /**
     * Looks for QuestionBoard in database.
     *
     * @param boardId The board id provided by the URL.
     * @return The requested QuestionBoard, or if not existent null.
     */
    public QuestionBoard getBoardById(UUID boardId) {
        return questionBoardRepository.getById(boardId);
    }

    /**
     * Looks for QuestionBoard with the specified moderator code in the database.
     *
     * @param moderatorCode The moderator code.
     * @return The requested QuestionBoard, or null if nonexistent.
     */
    public QuestionBoard getBoardByModeratorCode(UUID moderatorCode) {
        return questionBoardRepository.getByModeratorCode(moderatorCode);
    }

    /**
     * Retrieve questions set from database.
     * First retrieves QuestionBoard object and then
     * retrieves Question objects mapped to said QuestionBoard.
     *
     * @param boardId The board id.
     * @return The set of questions of this specific board or null if the board is not found.
     */
    public Set<Question> getQuestionsByBoardId(UUID boardId) {
        QuestionBoard qb = questionBoardRepository.getById(boardId);
        if (qb == null) {
            return null;
        }
        // Find questions belonging to this board
        return questionRepository.getQuestionByQuestionBoard(qb);
    }

    /**
     * Set the QuestionBoard's closed attribute to true.
     *
     * @param boardId The board id.
     * @return The updated QuestionBoard
     * @throws NotFoundException if the board doesn't exist.
     * @throws ConflictException if the board is already closed.
     */
    public QuestionBoard closeBoard(UUID boardId) {
        QuestionBoard qb = questionBoardRepository.getById(boardId);

        if (qb == null) {
            throw new NotFoundException("Question board does not exist");
        }

        if (qb.isClosed()) {
            throw new ConflictException("This QuestionBoard is already closed");
        }

        qb.setClosed(true);

        questionBoardRepository.save(qb);
        return qb;
    }
}
