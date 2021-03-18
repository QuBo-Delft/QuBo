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
     * Answer question of questionID with provided answer.
     * Only works if question exists and moderatorCode
     * is correct for the board this question exists in.
     *
     * @param moderatorCode     The provided QuestionBoard.
     * @param questionId        The provided moderator code.
     * @param answerModel       The answer model.
     * @return The answer created in the database.
     * @throws NotFoundException if the question or board don't exists in the DB.
     * @throws ForbiddenException if the moderator code's board
     *      doesn't align with the question's assigned board.
     */
    public Answer addAnswerToQuestion(
            UUID moderatorCode, UUID questionId,
            AnswerCreationBindingModel answerModel) {
        QuestionBoard board = questionBoardRepository.getByModeratorCode(moderatorCode);
        if (board == null) {
            // Requested board does not exist.
            throw new NotFoundException("Question board does not exist");
        }
        Question question = questionRepository.getQuestionById(questionId);
        if (question == null) {
            // Requested question does not exist.
            throw new NotFoundException("Question does not exist");
        }
        QuestionBoard boardContainingQuestion = question.getQuestionBoard();
        if (!boardContainingQuestion.equals(board)) {
            // Moderator code is not correct for the board the question was asked in.
            throw new ForbiddenException("Incorrect moderatorCode for Question");
        }

        // Create answer and save to DB
        Answer answer = modelMapper.map(answerModel, Answer.class);
        answer.setQuestion(question);
        answer.setTimestamp(Timestamp.from(Instant.now()));
        answerRepository.save(answer);
        return answer;
    }
}
