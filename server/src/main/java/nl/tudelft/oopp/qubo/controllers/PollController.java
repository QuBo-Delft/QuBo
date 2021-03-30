package nl.tudelft.oopp.qubo.controllers;

import nl.tudelft.oopp.qubo.dtos.poll.PollDetailsDto;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.services.PollService;
import nl.tudelft.oopp.qubo.services.QuestionBoardService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * The type Poll controller
 * used for requests starting with /api/board/.
 */
@Controller
@RequestMapping("/api/board/")
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
     * GET endpoint that provides the client with a Poll associated with the question board whose ID was
     * provided in the request.
     * Throw 400 upon wrong UUID formatting.
     * Throw 404 upon requesting a non-existent question board.
     *
     * @param boardId ID property of a board.
     * @return The question board with this specific UUID.
     */
    @RequestMapping(value = "{boardid}/poll", method = GET)
    @ResponseBody
    public PollDetailsDto retrievePollDetails(
            @PathVariable("boardid") UUID boardId) {
        QuestionBoard qb = questionBoardService.getBoardById(boardId);

        //If the question board does not exist, throw a 404
        if (qb == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }

        //Obtain the poll associated with the question board and convert this to a PollDetailsDto
        Poll poll = pollService.getPollByBoard(qb);
        PollDetailsDto pollDto = modelMapper.map(poll, PollDetailsDto.class);

        return pollDto;
    }
}