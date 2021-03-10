package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.bindingmodels.QuestionBoardBindingModel;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionBoardService {

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    /**
     * Reflect binding model of question board and save it to the DB.
     *
     * @param boardModel    The board model.
     * @return The QuestionBoard that was created.
     */
    public QuestionBoard saveBoard(QuestionBoardBindingModel boardModel) {
        QuestionBoard board = boardModel.reflect();
        questionBoardRepository.save(board);
        return board;
    }
}
