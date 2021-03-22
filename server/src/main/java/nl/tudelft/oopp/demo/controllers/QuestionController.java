package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.answer.AnswerCreationDto;
import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.demo.dtos.questionvote.QuestionVoteCreationDto;
import nl.tudelft.oopp.demo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.entities.QuestionVote;
import nl.tudelft.oopp.demo.services.AnswerService;
import nl.tudelft.oopp.demo.services.QuestionService;
import nl.tudelft.oopp.demo.services.QuestionVoteService;
import nl.tudelft.oopp.demo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.demo.services.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
        ModelMapper modelMapper) {
        this.questionService = questionService;
        this.questionVoteService = questionVoteService;
        this.answerService = answerService;
        this.modelMapper = modelMapper;
    }


    /**
     * POST endpoint for answering questions.
     *
     * @param answerModel   The answer model.
     * @param questionId    The question id.
     * @param moderatorCode The moderator code.
     * @return The answer details dto.
     * @throws NotFoundException  if there is no board with moderatorCode or question with questionId.
     * @throws ForbiddenException if the board associated with the moderatorCode does not hold the question.
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
            throw new NotFoundException("Question does not exist");
        }
        QuestionBoard board = question.getQuestionBoard();
        // Check whether moderator code is correct for the board the question was asked in
        if (!board.getModeratorCode().equals(moderatorCode)) {
            throw new ForbiddenException("Incorrect moderatorCode for Question");
        }
        // Answer the question
        Answer answer = answerService.addAnswerToQuestion(answerModel, question);
        AnswerCreationDto dto = modelMapper.map(answer, AnswerCreationDto.class);
        return dto;
    }


    /**
     * DELETE endpoint for deleting questions.
     *
     * @param questionId The question id.
     * @param code       The secret code of the question or the moderator code of its board.
     * @return The details of the deleted question.
     * @throws NotFoundException  if there is no question with this questionId.
     * @throws ForbiddenException if the provided code is neither the secret code of the given
     *                            question nor the moderator code of its board.
     */
    @RequestMapping(value = "{questionid}", method = DELETE)
    @ResponseBody
    public QuestionDetailsDto deleteQuestion(
        @PathVariable("questionid") UUID questionId,
        @RequestParam("code") UUID code) {
        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            // Requested question does not exist
            throw new NotFoundException("Question does not exist");
        }
        if (!questionService.canModifyQuestion(question, code)) {
            throw new ForbiddenException("The provided code is neither the secret code of this "
                + "question nor the moderator code of its board.");
        }

        QuestionDetailsDto dto = modelMapper.map(question, QuestionDetailsDto.class);
        questionService.deleteQuestionById(question.getId());
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
     * Delete question vote question vote details dto.
     * throws 404 when vote does not exists.
     * throws 404 when vote's question doesn't match the provided question.
     *
     * @param questionId The question ID.
     * @param voteId     The vote ID.
     * @return The QuestionVoteDetailsDto based on the deleted QuestionVote.
     */
    @RequestMapping(value = "/{questionid}/vote/{voteid}", method = DELETE)
    @ResponseBody
    public QuestionVoteDetailsDto deleteQuestionVote(
        @PathVariable("questionid") UUID questionId,
        @PathVariable("voteid") UUID voteId) {
        // verify request
        QuestionVote vote = questionVoteService.getQuestionVoteById(voteId);
        // Check if vote exists
        if (vote == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find QuestionVote");
        }
        // Check if questionId corresponds with this vote's questionId
        if (!questionId.equals(vote.getQuestion().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This Question was not voted on"
                + " with this QuesitonVote");
        }
        QuestionVoteDetailsDto dto = modelMapper.map(vote, QuestionVoteDetailsDto.class);
        questionVoteService.deleteVote(vote);
        return dto;
    }

}
