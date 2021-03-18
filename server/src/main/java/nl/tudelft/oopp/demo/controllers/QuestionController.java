package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.answer.AnswerCreationDto;
import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.services.AnswerService;
import nl.tudelft.oopp.demo.services.QuestionBoardService;
import nl.tudelft.oopp.demo.services.QuestionService;
import nl.tudelft.oopp.demo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.demo.services.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
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
    private final AnswerService answerService;

    private final ModelMapper modelMapper;


    /**
     * Creates an instance of a Question controller.
     *
     * @param questionService      The question service.
     * @param answerService        The answer service.
     * @param modelMapper          The model mapper.
     */
    public QuestionController(
            QuestionService questionService, AnswerService answerService,
            ModelMapper modelMapper) {
        this.questionService = questionService;
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
     * @throws ForbiddenException if the board associated with the moderatorCode does not hold the question.
     * @return The answer details dto.
     */
    @RequestMapping(value = "{questionid}/answer", method = POST, consumes = "application/json")
    @ResponseBody
    public AnswerCreationDto addAnswerToQuestion(
            @Valid @RequestBody AnswerCreationBindingModel answerModel,
            @PathVariable("questionid") UUID questionId,
            @RequestParam("code") UUID moderatorCode) {
        Answer answer = answerService.addAnswerToQuestion(moderatorCode, questionId, answerModel);
        return modelMapper.map(answer, AnswerCreationDto.class);
    }


}
