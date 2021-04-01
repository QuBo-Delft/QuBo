package nl.tudelft.oopp.qubo.controllers;

import java.util.UUID;
import javax.validation.Valid;
import nl.tudelft.oopp.qubo.dtos.poll.PollCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.poll.PollCreationDto;
import nl.tudelft.oopp.qubo.dtos.poll.PollDetailsDto;
import nl.tudelft.oopp.qubo.dtos.pollvote.PollVoteCreationDto;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import nl.tudelft.oopp.qubo.entities.PollVote;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.services.PollService;
import nl.tudelft.oopp.qubo.services.PollVoteService;
import nl.tudelft.oopp.qubo.services.QuestionBoardService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * The type Poll controller
 * used for requests starting with /api/board.
 */
@Controller
@RequestMapping("/api/board")
public class PollController {

    private final QuestionBoardService questionBoardService;
    private final PollService pollService;
    private final PollVoteService pollVoteService;

    private final ModelMapper modelMapper;

    /**
     * Creates an instance of a PollController.
     *
     * @param questionBoardService The QuestionBoardService.
     * @param pollService          The PollService.
     * @param pollVoteService      The PollVoteService.
     * @param modelMapper          The ModelMapper.
     */
    public PollController(QuestionBoardService questionBoardService,
                          PollService pollService,
                          PollVoteService pollVoteService,
                          ModelMapper modelMapper) {
        this.questionBoardService = questionBoardService;
        this.pollService = pollService;
        this.pollVoteService = pollVoteService;
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

    /**
     * DELETE endpoint for deleting Polls.
     * Throw 404 upon requesting a non-existent poll.
     * Throw 403 when the provided moderator code does not match that of the question board.
     *
     * @param boardId The ID of the question board whose poll should be deleted.
     * @param code    The moderator code of the question board.
     * @return The PollDetailsDto containing details about the deleted poll.
     */
    @RequestMapping(value = "/{boardid}/poll", method = DELETE)
    @ResponseBody
    public PollDetailsDto deletePoll(
        @PathVariable("boardid") UUID boardId,
        @RequestParam("code") UUID code) {
        QuestionBoard board = questionBoardService.getBoardById(boardId);
        // Check if the question board exists
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This question board does not exist.");
        }

        // Check if the provided moderator code matches that of the question board whose poll should be deleted
        UUID moderator = board.getModeratorCode();
        if (!code.equals(moderator)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The provided moderator code does not"
                + "match that of the question board.");
        }

        Poll poll = board.getPoll();
        // Check if there is a poll associated with the question board
        if (poll == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is currently no poll in this"
                + "question board.");
        }

        PollDetailsDto pollDto = modelMapper.map(poll, PollDetailsDto.class);
        // Delete the poll
        pollService.deletePoll(poll.getId());

        return pollDto;
    }

    /**
     * POST endpoint for voting on a poll.
     * Throw 400 upon wrong UUID formatting.
     * Throw 403 if the poll associated with the option is closed.
     * Throw 404 if the option id does not exist, or the specified board id is not the id of its board.
     *
     * @param boardId  The board ID.
     * @param optionId The option ID.
     * @return The PollVoteCreation DTO.
     */
    @RequestMapping(value = "/{boardid}/poll/{optionid}/vote", method = POST)
    @ResponseBody
    public PollVoteCreationDto registerPollVote(
        @PathVariable("boardid") UUID boardId,
        @PathVariable("optionid") UUID optionId) {
        PollOption option = pollService.getPollOptionById(optionId);

        if (option == null || !option.getPoll().getQuestionBoard().getId().equals(boardId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find PollOption");
        }

        PollVote vote = pollVoteService.registerVote(optionId);
        PollVoteCreationDto dto = modelMapper.map(vote, PollVoteCreationDto.class);
        return dto;
    }
}
