package nl.tudelft.oopp.demo.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import nl.tudelft.oopp.demo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.pacevote.PaceVoteCreationDto;
import nl.tudelft.oopp.demo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.question.QuestionCreationDto;
import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.demo.entities.PaceVote;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.services.PaceVoteService;
import nl.tudelft.oopp.demo.services.QuestionBoardService;
import nl.tudelft.oopp.demo.services.QuestionService;

import org.modelmapper.ModelMapper;

import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * The type Question board controller
 * used for requests starting with /api/board.
 */
@Controller
@RequestMapping("/api/board")
public class QuestionBoardController {

    private final QuestionBoardService service;
    private final QuestionService questionService;
    private final PaceVoteService paceVoteService;

    private final ModelMapper modelMapper;

    /**
     * Creates an instance of a QuestionBoardController.
     *
     * @param service         The QuestionBoardService.
     * @param questionService The QuestionService.
     * @param paceVoteService The PaceVoteService
     * @param modelMapper     The ModelMapper.
     */
    public QuestionBoardController(QuestionBoardService service,
                                   QuestionService questionService,
                                   PaceVoteService paceVoteService,
                                   ModelMapper modelMapper) {
        this.service = service;
        this.questionService = questionService;
        this.paceVoteService = paceVoteService;
        this.modelMapper = modelMapper;
    }

    /**
     * POST endpoint to create questionBoard on backend.
     *
     * @param qb The binding model passed by the client containing information to be used
     *           in creating a new QuestionBoard on the backend.
     * @return the question board
     */
    @RequestMapping(method = POST, consumes = "application/json")
    @ResponseBody
    public QuestionBoardCreationDto createBoard(
        @Valid @RequestBody QuestionBoardCreationBindingModel qb) {
        QuestionBoard board = service.saveBoard(qb);
        QuestionBoardCreationDto dto = modelMapper.map(board, QuestionBoardCreationDto.class);
        return dto;
    }

    /**
     * GET endpoint that provides client with QuestionBoard based on requested UUID.
     * Throw 400 upon wrong UUID formatting.
     * Throw 404 upon requesting non-existent boardid.
     *
     * @param boardId ID property of a board.
     * @return The question board with this specific UUID.
     */
    @RequestMapping(value = "/{boardid}", method = GET)
    @ResponseBody
    public QuestionBoardDetailsDto retrieveQuestionBoardDetails(
        @PathVariable("boardid") UUID boardId) {
        QuestionBoard qb = service.getBoardById(boardId);
        if (qb == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        QuestionBoardDetailsDto dto = modelMapper.map(qb, QuestionBoardDetailsDto.class);
        return dto;
    }

    /**
     * GET endpoint that provides the client with the QuestionBoard associated with the specified
     *      moderator code.
     * Throw 400 upon wrong UUID formatting.
     * Throw 404 upon requesting a non-existent moderator code.
     *
     * @param moderatorCode Moderator code of a board.
     * @return The question board with the specified moderator code.
     */
    @RequestMapping(value = "/moderator", method = GET)
    @ResponseBody
    public QuestionBoardDetailsDto retrieveQuestionBoardByModeratorCode(
        @RequestParam("code") UUID moderatorCode) {
        QuestionBoard qb = service.getBoardByModeratorCode(moderatorCode);
        if (qb == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        QuestionBoardDetailsDto dto = modelMapper.map(qb, QuestionBoardDetailsDto.class);
        return dto;
    }

    /**
     * GET endpoint to retrieve the list of questions of this QuestionBoard.
     * Throw 400 upon wrong UUID formatting.
     * Throw 404 upon requesting non-existent boardid.
     *
     * @param boardId ID property of a board.
     * @return The list of questions of this board.
     */
    @RequestMapping(value = "/{boardid}/questions", method = GET)
    @ResponseBody
    public Set<QuestionDetailsDto> retrieveQuestionListByBoardId(
        @PathVariable("boardid") UUID boardId) {
        // 400 is thrown upon bad formatting automatically
        Set<Question> ql = service.getQuestionsByBoardId(boardId);
        // Throw 404 when board does not exist
        if (ql == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }

        Set<QuestionDetailsDto> dtoSet = modelMapper.map(ql,
            new TypeToken<Set<QuestionDetailsDto>>() {
            }.getType());
        return dtoSet;
    }

    /**
     * POST endpoint to ask a question.
     * Throw 400 upon wrong UUID formatting.
     * Throw 404 upon requesting non-existent boardid.
     *
     * @param boardId ID property of a board.
     * @param model   The binding model passed by the client containing the question text.
     * @return The newly-created question.
     */
    @RequestMapping(value = "/{boardid}/question", method = POST, consumes = "application/json")
    @ResponseBody
    public QuestionCreationDto createQuestion(
        @PathVariable("boardid") UUID boardId,
        @Valid @RequestBody QuestionCreationBindingModel model) {
        Question question = questionService.createQuestion(model, boardId);
        QuestionCreationDto dto = modelMapper.map(question, QuestionCreationDto.class);
        return dto;
    }


    /**
     * POST endpoint for registering PaceVotes.
     *
     * @param boardId       The board ID.
     * @param paceVoteModel The PaceVote model.
     * @return The paceVote DTO.
     */
    @RequestMapping(value = "/{boardid}/pace", method = POST, consumes = "application/json")
    @ResponseBody
    public PaceVoteCreationDto registerPaceVote(
        @PathVariable("boardid") UUID boardId,
        @Valid @RequestBody PaceVoteCreationBindingModel paceVoteModel) {
        PaceVote paceVote = paceVoteService.registerVote(paceVoteModel, boardId);
        return modelMapper.map(paceVote, PaceVoteCreationDto.class);
    }
}

