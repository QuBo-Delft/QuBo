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
     * Retrieve questions set from database.
     *
     * @param boardId The board id.
     * @return The set of questions of this specific board.
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
     * Reflect binding model of question board and save it to the DB.
     *
     * @param boardModel The board model.
     * @return The QuestionBoard that was created.
     */
    public QuestionBoard saveBoard(QuestionBoardBindingModel boardModel) {
        QuestionBoard board = boardModel.reflect();
        questionBoardRepository.save(board);
        return board;
    }
}
