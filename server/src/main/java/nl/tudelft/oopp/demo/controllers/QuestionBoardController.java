package nl.tudelft.oopp.demo.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Set;
import java.util.UUID;

import nl.tudelft.oopp.demo.dtos.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.dtos.bindingmodels.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.services.QuestionBoardService;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * The type Question board controller
 * used for requests starting with /api/board.
 */
@Controller
@RequestMapping("/api/board")
public class QuestionBoardController {

    @Autowired
    QuestionBoardService service;

    @Autowired
    ModelMapper modelMapper;

    /**
     * POST endpoint to create questionBoard on backend.
     *
     * @param qb    The binding model passed by the client containing information to be used
     *           in creating a new QuestionBoard on the backend.
     * @return the question board
     */
    @RequestMapping(method = POST, consumes = "application/json")
    @ResponseBody
    public QuestionBoardCreationDto createBoard(@RequestBody QuestionBoardCreationBindingModel qb) {
        QuestionBoard board = service.saveBoard(qb);
        QuestionBoardCreationDto dto = modelMapper.map(board, QuestionBoardCreationDto.class);
        return dto;
    }

    /**
     * GET endpoint that provides client with QuestionBoard based on requested UUID.
     * Throw 400 upon wrong UUID formatting.
     * Throw 404 upon requesting non-existent boardid.
     *
     * @param boardId   ID property of a board.
     * @return The question board with this specific UUID.
     */
    @RequestMapping(value = "/{boardid}", method = GET)
    @ResponseBody
    public QuestionBoard retrieveQuestionBoardDetails(@PathVariable("boardid") UUID boardId) {
        QuestionBoard qb = service.getBoardById(boardId);
        if (qb == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        return qb;
    }

    /**
     * GET endpoint to retrieve the list of questions of this QuestionBoard.
     * Throw 400 upon wrong UUID formatting.
     * Throw 404 upon requesting non-existent boardid.
     *
     * @param boardId   ID property of a board.
     * @return The list of questions of this board.
     */
    @RequestMapping(value = "/{boardid}/questions", method = GET)
    @ResponseBody
    public Set<Question> retrieveQuestionListByBoardId(@PathVariable("boardid") UUID boardId) {
        // 400 is thrown upon bad formatting automatically
        Set<Question> ql = service.getQuestionsByBoardId(boardId);
        // Throw 404 when board does not exist
        if (ql == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        return ql;
    }
}

