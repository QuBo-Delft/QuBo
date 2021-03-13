package nl.tudelft.oopp.demo.services;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import nl.tudelft.oopp.demo.dtos.bindingmodels.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QuestionBoardService {
    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Save board to database.
     * Converts the binding model to a QuestionBoard entity,
     * generates a random moderator code, and saves
     * it into the database.
     *
     * @param boardModel    The board model.
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
     * @param boardId   The board id provided by the URL.
     * @return The requested QuestionBoard, or if not existent null.
     */
    public QuestionBoard getBoardById(UUID boardId) {
        return questionBoardRepository.getById(boardId);
    }

    /**
     * Retrieve questions set from database.
     * First retrieves QuestionBoard object and then
     * retrieves Question objects mapped to said QuestionBoard.
     *
     * @param boardId   The board id.
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
}
