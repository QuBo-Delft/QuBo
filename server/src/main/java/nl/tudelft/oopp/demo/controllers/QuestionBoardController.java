package nl.tudelft.oopp.demo.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Set;
import java.util.UUID;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class QuestionBoardController {

    @Autowired
    private QuestionBoardRepository repository;

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * GET endpoint to retrieve list of questions in that board.
     *
     * @param boardId id property of a board.
     * @return list of questions in this board.
     */
    @RequestMapping(value = "/api/board/{boardid}/questions", method = GET)
    @ResponseBody
    public Set<Question> retrieveQuestionListByBoardId(@PathVariable("boardid") UUID boardId) {
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
