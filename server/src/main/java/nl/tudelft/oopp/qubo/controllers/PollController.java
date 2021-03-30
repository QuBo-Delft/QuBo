package nl.tudelft.oopp.qubo.controllers;

import nl.tudelft.oopp.qubo.dtos.poll.PollCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.poll.PollCreationDto;
import nl.tudelft.oopp.qubo.dtos.poll.PollDetailsDto;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.services.PollService;
import nl.tudelft.oopp.qubo.services.QuestionBoardService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * The type Poll controller
 * used for requests starting with /api/board.
 */
@Controller
@RequestMapping("/api/board")
public class PollController {

    private final QuestionBoardService questionBoardService;
    private final PollService pollService;

    private final ModelMapper modelMapper;

    /**
     * Creates an instance of a PollController.
     *
     * @param questionBoardService  The QuestionBoardService.
     * @param pollService           The PollService.
     * @param modelMapper           The ModelMapper.
     */
    public PollController(QuestionBoardService questionBoardService,
                                   PollService pollService,
                                   ModelMapper modelMapper) {
        this.questionBoardService = questionBoardService;
        this.pollService = pollService;
        this.modelMapper = modelMapper;
    }

    /**
     * POST endpoint to create a Poll.
     *
     * @param pollModel The binding model passed by the client containing information to be used
     *                  in creating a new Poll.
     * @return the question board
     */
    @RequestMapping(value = "/{boardid}/poll", method = POST, consumes = "application/json")
    @ResponseBody
    public PollCreationDto createPoll(
            @PathVariable("boardid") UUID boardId,
            @RequestParam("code") UUID moderatorCode,
            @Valid @RequestBody PollCreationBindingModel pollModel) {
        QuestionBoard qb = questionBoardService.getBoardById(boardId);

        //Check if the question board exists
        if (qb == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        //Check if the moderator code of the question board is equal to the code that was provided
        //by the client.
        if (!qb.getModeratorCode().equals(moderatorCode)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid moderator code");
        }

        //Create a new poll
        Poll poll = pollService.createPoll(pollModel, boardId);

        //Convert the poll into a PollCreationDto that is returned.
        PollCreationDto dto = modelMapper.map(poll, PollCreationDto.class);
        return dto;
    }

    /**
     * GET endpoint that provides the client with a Poll associated with the question board whose ID was
     * provided in the request.
     * Throw 400 upon wrong UUID formatting.
     * Throw 404 upon requesting a non-existent question board.
     *
     * @param boardId ID property of a board.
     * @return The question board with this specific UUID.
     */
    @RequestMapping(value = "/{boardid}/poll", method = GET)
    @ResponseBody
    public PollDetailsDto retrievePollDetails(
            @PathVariable("boardid") UUID boardId) {
        QuestionBoard qb = questionBoardService.getBoardById(boardId);

        // If the question board does not exist, throw a 404
        if (qb == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }

        // Obtain the poll associated with the question board and convert this to a PollDetailsDto
        Poll poll = qb.getPoll();

        // If there is no poll associated with the question board, throw a 404
        if (poll == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no poll in this question board");
        }

        PollDetailsDto pollDto = modelMapper.map(poll, PollDetailsDto.class);

        return pollDto;
    }
}