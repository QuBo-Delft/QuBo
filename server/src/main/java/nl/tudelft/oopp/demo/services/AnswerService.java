package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.AnswerRepository;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
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
     * Answer question of questionID with provided answer.
     * Only works if question exists and moderatorCode
     * is correct for the board this question exists in.
     *
     * @param qb                The provided QuestionBoard.
     * @param moderatorCode     The provided moderator code.
     * @param questionToAnswer  The provided Question.
     * @param answerModel       The answer model.
     * @return The answer created in the database.
     */
    public Answer answerQuestion(
            QuestionBoard qb, UUID moderatorCode,
            Question questionToAnswer, AnswerCreationBindingModel answerModel) {
        QuestionBoard boardContainingQuestion = questionToAnswer.getQuestionBoard();
        // Checks whether moderatorCode is valid for the board this question was asked in.
        if (!boardContainingQuestion.equals(qb)) {
            return null;
        }
        // Make answer and save to DB
        Answer answer = modelMapper.map(answerModel, Answer.class);
        answer.setQuestion(questionToAnswer);
        answer.setTimestamp(Timestamp.from(Instant.now()));
        answerRepository.save(answer);
        return answer;
    }
}
