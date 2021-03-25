package nl.tudelft.oopp.demo.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import nl.tudelft.oopp.demo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.question.QuestionEditingBindingModel;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.services.exceptions.ConflictException;
import nl.tudelft.oopp.demo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.demo.services.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class QuestionService {
    private final QuestionBoardRepository questionBoardRepository;

    private final QuestionRepository questionRepository;

    private final ModelMapper modelMapper;

    /**
     * Creates an instance of a QuestionService.
     *
     * @param questionBoardRepository A QuestionBoardRepository.
     * @param questionRepository      A QuestionRepository.
     * @param modelMapper             The ModelMapper.
     */
    public QuestionService(
        QuestionBoardRepository questionBoardRepository, QuestionRepository questionRepository,
        ModelMapper modelMapper) {
        this.questionBoardRepository = questionBoardRepository;
        this.questionRepository = questionRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a Question assigned to the board,
     * sets the timestamp to the current time,
     * generates a random secret code,
     * and saves the question in the database.
     *
     * @param model   The details of the new question.
     * @param boardId The id of the board which the question should be assigned to.
     * @return The newly-created question.
     * @throws NotFoundException  if the board doesn't exist.
     * @throws ForbiddenException if the startTime of the board is after the current time or
     *                            the board is closed.
     */
    public Question createQuestion(QuestionCreationBindingModel model, UUID boardId) {
        QuestionBoard board = questionBoardRepository.getById(boardId);
        if (board == null) {
            throw new NotFoundException("Question board does not exist");
        }

        Instant now = Instant.now();

        if (now.isBefore(board.getStartTime().toInstant())
            || board.isClosed()) {
            throw new ForbiddenException("Question board is not active");
        }

        Question question = modelMapper.map(model, Question.class);
        question.setQuestionBoard(board);
        question.setSecretCode(UUID.randomUUID());
        question.setTimestamp(Timestamp.from(Instant.now()));

        questionRepository.save(question);

        return question;
    }

    /**
     * Gets question by id.
     *
     * @param questionId The question id.
     * @return The question by id.
     */
    public Question getQuestionById(UUID questionId) {
        return questionRepository.getQuestionById(questionId);
    }

    /**
     * Edits the text of a question.
     *
     * @param questionId The id of the question to be deleted.
     * @param model      A model containing the new text of the question.
     * @return The updated Question.
     * @throws NotFoundException if the board doesn't exist.
     */
    public Question editQuestion(UUID questionId, QuestionEditingBindingModel model) {
        Question question = questionRepository.getQuestionById(questionId);
        if (question == null) {
            throw new NotFoundException("Question does not exist");
        }

        question.setText(model.getText());

        questionRepository.save(question);

        return question;
    }

    /**
     * Deletes a question from the database.
     *
     * @param id The id of the question to be deleted.
     */
    public void deleteQuestionById(UUID id) {
        this.questionRepository.deleteQuestionById(id);
    }

    /**
     * Marks a question as answered.
     *
     * @param questionId The ID of the question to be marked as answered.
     * @return The question that was just marked as answered.
     * @throws ConflictException if the question was already marked as answered.
     */
    public Question markAsAnswered(UUID questionId) {
        Question question = questionRepository.getQuestionById(questionId);
        if (question.getAnswered() != null) {
            throw new ConflictException("Question was already marked as answered");
        }
        question.setAnswered(Timestamp.from(Instant.now()));
        questionRepository.save(question);
        return question;
    }

    /**
     * Checks if a user has permission to modify a question by verifying whether a code is
     * either the secret code of a question, or the moderator code of its board.
     *
     * @param question The question.
     * @return Whether the user can modify the question if they provide the given code.
     */
    public boolean canModifyQuestion(Question question, UUID code) {
        return code.equals(question.getSecretCode())
            || code.equals(question.getQuestionBoard().getModeratorCode());
    }
}
