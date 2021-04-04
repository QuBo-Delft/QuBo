package nl.tudelft.oopp.qubo.controllers;

import java.util.UUID;
import javax.validation.Valid;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerCreationDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionEditingBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.qubo.entities.Answer;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.entities.QuestionVote;
import nl.tudelft.oopp.qubo.services.AnswerService;
import nl.tudelft.oopp.qubo.services.BanService;
import nl.tudelft.oopp.qubo.services.QuestionService;
import nl.tudelft.oopp.qubo.services.QuestionVoteService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;


/**
 * The Question controller
 * used for requests starting with /api/question.
 */
@Controller
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionVoteService questionVoteService;
    private final AnswerService answerService;
    private final BanService banService;

    private final ModelMapper modelMapper;

    /**
     * Creates an instance of a Question controller.
     *
     * @param questionService     The question service.
     * @param questionVoteService The Question vote service.
     * @param answerService       The answer service.
     * @param modelMapper         The model mapper.
     */
    public QuestionController(
        QuestionService questionService,
        QuestionVoteService questionVoteService,
        AnswerService answerService,
        BanService banService,
        ModelMapper modelMapper) {
        this.questionService = questionService;
        this.questionVoteService = questionVoteService;
        this.answerService = answerService;
        this.banService = banService;
        this.modelMapper = modelMapper;
    }


    /**
     * POST endpoint for answering questions.
     * Throw 404 if there is no board with the specified moderatorCode or question
     * with the specified question ID.
     * Throw 403 if the board associated with the moderatorCode does not hold the question.
     *
     * @param answerModel   The answer model.
     * @param questionId    The question id.
     * @param moderatorCode The moderator code.
     * @return The answer creation dto.
     */
    @RequestMapping(value = "{questionid}/answer", method = POST, consumes = "application/json")
    @ResponseBody
    public AnswerCreationDto addAnswerToQuestion(
        @Valid @RequestBody AnswerCreationBindingModel answerModel,
        @PathVariable("questionid") UUID questionId,
        @RequestParam("code") UUID moderatorCode) {
        // Retrieve question and board moderator rights hold for
        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            // Requested question does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question does not exist");
        }
        QuestionBoard board = question.getQuestionBoard();
        // Check whether moderator code is correct for the board the question was asked in
        if (!board.getModeratorCode().equals(moderatorCode)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect moderatorCode for Question");
        }
        // Answer the question
        Answer answer = answerService.addAnswerToQuestion(answerModel, question);
        AnswerCreationDto dto = modelMapper.map(answer, AnswerCreationDto.class);
        return dto;
    }


    /**
     * DELETE endpoint for deleting questions.
     * Throw 404 if there is no question with this questionId.
     * Throw 403 if the provided code is neither the secret code of the given
     * question nor the moderator code of its board.
     *
     * @param questionId The question id.
     * @param code       The secret code of the question or the moderator code of its board.
     * @return The details of the deleted question.
     */
    @RequestMapping(value = "{questionid}", method = DELETE)
    @ResponseBody
    public QuestionDetailsDto deleteQuestion(
        @PathVariable("questionid") UUID questionId,
        @RequestParam("code") UUID code) {
        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            // Requested question does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question does not exist");
        }
        if (!questionService.canModifyQuestion(question, code)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The provided code is neither the "
                + "secret code of this question nor the moderator code of its board.");
        }

        QuestionDetailsDto dto = modelMapper.map(question, QuestionDetailsDto.class);
        questionService.deleteQuestionById(question.getId());
        return dto;
    }


    /**
     * PUT endpoint for editing questions.
     * Throw 404 if there is no question with this questionId.
     * Throw 403 if the provided code is neither the secret code of the given
     * question nor the moderator code of its board.
     *
     * @param model      The question editing model.
     * @param questionId The question id.
     * @param code       The secret code of the question or the moderator code of its board.
     * @return The details of the edited question.
     */
    @RequestMapping(value = "{questionid}", method = PUT)
    @ResponseBody
    public QuestionDetailsDto editQuestion(
        @Valid @RequestBody QuestionEditingBindingModel model,
        @PathVariable("questionid") UUID questionId,
        @RequestParam("code") UUID code) {
        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            // Requested question does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question does not exist");
        }
        if (!questionService.canModifyQuestion(question, code)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The provided code is neither the "
                + "secret code of this question nor the moderator code of its board.");
        }

        Question edited = questionService.editQuestion(questionId, model);

        QuestionDetailsDto dto = modelMapper.map(edited, QuestionDetailsDto.class);
        return dto;
    }

    /**
     * POST endpoint for registering QuestionVotes.
     *
     * @param questionId The question ID.
     * @return The QuestionVote DTO.
     */
    @RequestMapping(value = "/{questionid}/vote", method = POST)
    @ResponseBody
    public QuestionVoteCreationDto registerQuestionVote(
        @PathVariable("questionid") UUID questionId) {
        QuestionVote vote = questionVoteService.registerVote(questionId);
        QuestionVoteCreationDto dto = modelMapper.map(vote, QuestionVoteCreationDto.class);
        return dto;
    }

    /**
     * Delete the QuestionVote with the specified vote ID.
     *
     * @param questionId The question ID.
     * @param voteId     The vote ID.
     * @return The QuestionVoteDetailsDto based on the deleted QuestionVote.
     * @throws ResponseStatusException 404 if vote does not exist.
     * @throws ResponseStatusException 404 if the vote's question ID
     *                                 doesn't match the provided question ID.
     */
    @RequestMapping(value = "/{questionid}/vote/{voteid}", method = DELETE)
    @ResponseBody
    public QuestionVoteDetailsDto deleteQuestionVote(
        @PathVariable("questionid") UUID questionId,
        @PathVariable("voteid") UUID voteId) {
        // Verify the request
        QuestionVote vote = questionVoteService.getQuestionVoteById(voteId);
        // Check if vote exists
        if (vote == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find QuestionVote");
        }
        // Check if questionId corresponds to this vote's questionId
        if (!questionId.equals(vote.getQuestion().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This Question was not voted on"
                + " with this QuestionVote");
        }
        QuestionVoteDetailsDto dto = modelMapper.map(vote, QuestionVoteDetailsDto.class);
        questionVoteService.deleteVote(vote);
        return dto;
    }


    /**
     * PATCH endpoint for marking Questions as answered.
     *
     * @param questionId    The ID of the question that is to be marked as answered.
     * @param moderatorCode The moderator code of the board this question is in.
     * @return The QuestionDetailsDto after marking the question as answered.
     * @throws ResponseStatusException 404 if the question was not found in database.
     * @throws ResponseStatusException 403 if the provided moderatorCode is not authorized
     *                                 to mark this question as answered.
     */
    @RequestMapping(value = "{questionid}/answer", method = PATCH)
    @ResponseBody
    public QuestionDetailsDto markQuestionAsAnswered(
        @PathVariable("questionid") UUID questionId,
        @RequestParam("code") UUID moderatorCode) {
        Question question = questionService.getQuestionById(questionId);
        // Check if the question exists
        if (question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question does not exist");

        }
        // Check if the moderatorCode is valid for the question board the question is in
        if (!moderatorCode.equals(question.getQuestionBoard().getModeratorCode())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The provided moderatorCode is not valid "
                + "for this Question");
        }
        Question markedQuestion = questionService.markAsAnswered(questionId);
        QuestionDetailsDto dto = modelMapper.map(markedQuestion, QuestionDetailsDto.class);
        return dto;
    }

    /**
     * POST endpoint for banning user IPs.
     *
     * @param questionId    The ID of the question where the ban request originated.
     * @param moderatorCode The moderator code of the board this question is in.
     * @throws ResponseStatusException 404 if the question was not found in the database.
     * @throws ResponseStatusException 403 if the provided moderatorCode is not authorized
     *                                 to ban this user IP from the question board.
     */
    @RequestMapping(value = "{questionid}/ban", method = POST)
    @ResponseBody
    public void banUserByIp(
        @PathVariable("questionid") UUID questionId,
        @RequestParam("code") UUID moderatorCode) {
        Question question = questionService.getQuestionById(questionId);
        // Check if the question exists
        if (question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question does not exist");

        }
        // Check if the moderatorCode is valid for the question board the question is in
        if (!moderatorCode.equals(question.getQuestionBoard().getModeratorCode())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The provided moderatorCode is not valid "
                + "for this Question");
        }

        banService.banIp(questionId);
    }
}
