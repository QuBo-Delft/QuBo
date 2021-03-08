package nl.tudelft.oopp.demo.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Set;
import java.util.UUID;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.services.QuestionBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuestionBoardController {

    @Autowired
    QuestionBoardService questionBoardService;

    /**
     * GET endpoint to retrieve the list of questions of this QuestionBoard.
     *
     * @param boardId   ID property of a board.
     * @return The list of questions of this board.
     */
    @RequestMapping(value = "/api/board/{boardid}/questions", method = GET)
    @ResponseBody
    public Set<Question> retrieveQuestionListByBoardId(@PathVariable("boardid") UUID boardId) {
        return questionBoardService.getQuestionsByBoardId(boardId);
    }
}
