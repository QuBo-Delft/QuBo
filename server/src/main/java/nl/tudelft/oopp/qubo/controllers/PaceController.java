package nl.tudelft.oopp.qubo.controllers;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nl.tudelft.oopp.qubo.dtos.pace.PaceDetailsDto;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationDto;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteDetailsDto;
import nl.tudelft.oopp.qubo.entities.PaceVote;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.services.BanService;
import nl.tudelft.oopp.qubo.services.PaceVoteService;
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
 * The PaceController
 * used for pace-related requests starting with /api/board.
 */
@Controller
@RequestMapping("/api/board")
public class PaceController {
    private final QuestionBoardService questionBoardService;
    private final PaceVoteService paceVoteService;
    private final BanService banService;
    private final ModelMapper modelMapper;

    /**
     * Creates an instance of a PaceController.
     *
     * @param questionBoardService The QuestionBoardService.
     * @param paceVoteService      The PaceVoteService
     * @param banService           The BanService.
     * @param modelMapper          The ModelMapper.
     */
    public PaceController(QuestionBoardService questionBoardService,
                          PaceVoteService paceVoteService, BanService banService,
                          ModelMapper modelMapper) {
        this.questionBoardService = questionBoardService;
        this.paceVoteService = paceVoteService;
        this.banService = banService;
        this.modelMapper = modelMapper;
    }

    /**
     * GET endpoint to retrieve the aggregated pace details.
     * Throw 400 upon wrong UUID formatting.
     * Throw 404 upon requesting non-existent boardid.
     *
     * @param boardId       ID property of a board.
     * @param moderatorCode The moderator code passed by the client.
     * @return The newly-created pace details object.
     */
    @RequestMapping(value = "/{boardid}/pace", method = GET)
    @ResponseBody
    public PaceDetailsDto retrievePaceDetails(
        @PathVariable("boardid") UUID boardId,
        @RequestParam("code") UUID moderatorCode) {
        // 400 is thrown upon bad formatting automatically
        QuestionBoard questionBoard = questionBoardService.getBoardById(boardId);
        // Throw 404 when the board does not exist
        if (questionBoard == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        //Throw 403 if the provided moderator code does not equal that of the question board
        if (!questionBoard.getModeratorCode().equals(moderatorCode)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid moderator code");
        }

        var dto = paceVoteService.getAggregatedVotes(boardId);
        return dto;
    }


    /**
     * POST endpoint for registering PaceVotes.
     *
     * @param boardId       The board ID.
     * @param paceVoteModel The PaceVote model.
     * @param request       The HTTPServletRequest to get the IP of the user.
     * @return The paceVote DTO.
     */
    @RequestMapping(value = "/{boardid}/pace", method = POST, consumes = "application/json")
    @ResponseBody
    public PaceVoteCreationDto registerPaceVote(
        @PathVariable("boardid") UUID boardId,
        @Valid @RequestBody PaceVoteCreationBindingModel paceVoteModel,
        HttpServletRequest request) {
        QuestionBoard qb = questionBoardService.getBoardById(boardId);
        if (qb == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        banService.performBanCheck(qb, request.getRemoteAddr());
        PaceVote paceVote = paceVoteService.registerVote(paceVoteModel, boardId);
        return modelMapper.map(paceVote, PaceVoteCreationDto.class);
    }

    /**
     * DELETE endpoint for deleting PaceVotes.
     * Throw 404 upon requesting non-existent pacevote.
     *
     * @param boardId    The ID of the question board this request was made in.
     * @param paceVoteId The ID of the PaceVote that is to be deleted.
     * @return The dto containing details about the deleted vote.
     */
    @RequestMapping(value = "/{boardid}/pace/{pacevoteid}", method = DELETE)
    @ResponseBody
    public PaceVoteDetailsDto deletePaceVote(
        @PathVariable("boardid") UUID boardId,
        @PathVariable("pacevoteid") UUID paceVoteId) {
        PaceVote vote = paceVoteService.getById(paceVoteId);
        // Check if PaceVote with this ID exists
        if (vote == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pace vote does not exist");
        }
        // Check whether provided boardId matches with the ID in the PaceVote
        UUID paceVoteBoardId = vote.getQuestionBoard().getId();
        if (!paceVoteBoardId.equals(boardId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pace vote was not found in "
                + "requested Question board");
        }
        PaceVoteDetailsDto dto = modelMapper.map(vote, PaceVoteDetailsDto.class);
        // Delete actual vote
        paceVoteService.deleteVote(vote);
        return dto;
    }
}
