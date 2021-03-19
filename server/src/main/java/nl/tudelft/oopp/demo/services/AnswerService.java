package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.AnswerRepository;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.demo.services.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

/**
 * The Answer service.
 */
@Service
public class AnswerService {
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private final ModelMapper modelMapper;

    /**
     * Creates new instance of AnswerService.
     *
     * @param questionBoardRepository The question board repository.
     * @param questionRepository      The question repository.
     * @param answerRepository        The answer repository.
     * @param modelMapper             The model mapper.
     */
    public AnswerService(
            QuestionBoardRepository questionBoardRepository, QuestionRepository questionRepository,
            AnswerRepository answerRepository, ModelMapper modelMapper) {
        this.questionBoardRepository = questionBoardRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Answer provided question with answer created from provided answerModel.
     * Saves answer to the database.
     *
     * @param answerModel   The answer model.
     * @param question      The question to answer to.
     * @return The answer created in the database.
     */
    public Answer addAnswerToQuestion(AnswerCreationBindingModel answerModel, Question question) {
        Answer answer = modelMapper.map(answerModel, Answer.class);
        answer.setQuestion(question);
        answer.setTimestamp(Timestamp.from(Instant.now()));
        answerRepository.save(answer);
        return answer;
    }
}
