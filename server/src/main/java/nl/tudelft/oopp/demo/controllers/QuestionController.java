package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.services.AnswerService;
import nl.tudelft.oopp.demo.services.QuestionBoardService;
import nl.tudelft.oopp.demo.services.QuestionService;
import nl.tudelft.oopp.demo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.demo.services.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    private final QuestionBoardService questionBoardService;
    private final AnswerService answerService;

    private final ModelMapper modelMapper;


    /**
     * Creates an instance of a Question controller.
     *
     * @param questionService      The question service.
     * @param questionBoardService The question board service.
     * @param answerService        The answer service.
     * @param modelMapper          The model mapper.
     */
    public QuestionController(
            QuestionService questionService, QuestionBoardService questionBoardService,
            AnswerService answerService, ModelMapper modelMapper) {
        this.questionService = questionService;
        this.questionBoardService = questionBoardService;
        this.answerService = answerService;
        this.modelMapper = modelMapper;
    }


    /**
     * POST endpoint for answering questions.
     *
     *
     * @param answerModel   The answer model.
     * @param questionId    The question id.
     * @param moderatorCode The moderator code.
     * @throws NotFoundException if there is no board with moderatorCode or question with questionId.
     * @throws ForbiddenException if moderatorCode doesn't hold with board question is in.
     * @return the answer details dto.
     */
    @RequestMapping(value = "{questionid}/answer", method = POST, consumes = "application/json")
    @ResponseBody
    public AnswerDetailsDto answerQuestion(
            @Valid @RequestBody AnswerCreationBindingModel answerModel,
            @PathVariable("questionid") UUID questionId,
            @RequestParam("code") UUID moderatorCode) {
        QuestionBoard qb = questionBoardService.getBoardByModeratorCode(moderatorCode);
        Question question = questionService.getQuestionById(questionId);
        // Check whether questionBoard and Question exist
        if (qb == null || question == null) {
            throw new NotFoundException();
        }
        Answer answer = answerService.answerQuestion(qb, moderatorCode, question, answerModel);
        // Check whether moderatorCode is valid in board of requested question.
        if (answer == null) {
            throw new ForbiddenException();
        }
        return modelMapper.map(answer, AnswerDetailsDto.class);
    }


}
