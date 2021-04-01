package nl.tudelft.oopp.qubo.services;

import java.sql.Timestamp;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.Answer;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.repositories.AnswerRepository;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * The Answer service.
 */
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    private final ModelMapper modelMapper;

    private final CurrentTimeProvider currentTimeProvider;

    /**
     * Creates new instance of AnswerService.
     *
     * @param answerRepository    The answer repository.
     * @param modelMapper         The model mapper.
     * @param currentTimeProvider The CurrentTimeProvider.
     */
    public AnswerService(AnswerRepository answerRepository, ModelMapper modelMapper,
                         CurrentTimeProvider currentTimeProvider) {
        this.answerRepository = answerRepository;
        this.modelMapper = modelMapper;
        this.currentTimeProvider = currentTimeProvider;
    }

    /**
     * Answer provided question with answer created from provided answerModel.
     * Saves answer to the database.
     *
     * @param answerModel The answer model.
     * @param question    The question to answer to.
     * @return The answer created in the database.
     */
    public Answer addAnswerToQuestion(AnswerCreationBindingModel answerModel, Question question) {
        Answer answer = modelMapper.map(answerModel, Answer.class);
        answer.setQuestion(question);
        answer.setTimestamp(Timestamp.from(currentTimeProvider.getCurrentTime()));
        answerRepository.save(answer);
        return answer;
    }
}
