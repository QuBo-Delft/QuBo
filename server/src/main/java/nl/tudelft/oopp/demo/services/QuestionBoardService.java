package nl.tudelft.oopp.demo.services;

import java.util.Set;
import java.util.UUID;

import nl.tudelft.oopp.demo.bindingmodels.QuestionBoardBindingModel;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QuestionBoardService {
    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Save board to database.
     * Checks if
     * First reflects binding model of question board,
     * then save it to the database.
     *
     * @param boardModel    The board model.
     * @return The QuestionBoard that was created.
     */
    public QuestionBoard saveBoard(QuestionBoardBindingModel boardModel) {
        QuestionBoard board = boardModel.reflect();
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
