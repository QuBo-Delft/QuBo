package nl.tudelft.oopp.demo.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import nl.tudelft.oopp.demo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
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
     *                            the endTime is before the current time.
     */
    public Question createQuestion(QuestionCreationBindingModel model, UUID boardId) {
        QuestionBoard board = questionBoardRepository.getById(boardId);
        if (board == null) {
            throw new NotFoundException("Question board does not exist");
        }

        Instant now = Instant.now();

        if (now.isBefore(board.getStartTime().toInstant())
            || now.isAfter(board.getEndTime().toInstant())) {
            throw new ForbiddenException("Question board is not active");
        }

        Question question = modelMapper.map(model, Question.class);
        question.setQuestionBoard(board);
        question.setSecretCode(UUID.randomUUID());
        question.setTimestamp(Timestamp.from(Instant.now()));

        questionRepository.save(question);

        return question;
    }
}
