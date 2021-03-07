package nl.tudelft.oopp.demo.services;

import java.util.Set;
import java.util.UUID;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QuestionBoardService {
    @Autowired
    private QuestionBoardRepository repository;

    @Autowired
    private QuestionRepository questionRepository;


    /**
     * Retrieve questions set from database.
     *
     * @param boardId the board id.
     * @return the set of questions for this specific board.
     */
    public Set<Question> retrieveQuestions(UUID boardId) {
        // 400 is thrown upon bad formatting automatically
        QuestionBoard qb = repository.getById(boardId);
        if (qb == null) {
            // Throw 404 when board does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        // Find questions belonging to this board
        return questionRepository.getQuestionByQuestionBoard(qb);
    }

}
