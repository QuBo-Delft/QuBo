package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.dtos.poll.PollCreationBindingModel;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.PollRepository;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.demo.services.exceptions.ConflictException;
import nl.tudelft.oopp.demo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.demo.services.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
public class PollService {
    private final QuestionBoardRepository questionBoardRepository;

    private final PollRepository pollRepository;

    private final ModelMapper modelMapper;

    /**
     * Creates an instance of PollService.
     *
     * @param questionBoardRepository A QuestionBoardRepository.
     * @param pollRepository          A PollRepository.
     * @param modelMapper             The ModelMapper.
     */
    public PollService(
            QuestionBoardRepository questionBoardRepository, PollRepository pollRepository,
            ModelMapper modelMapper) {
        this.questionBoardRepository = questionBoardRepository;
        this.pollRepository = pollRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a Poll assigned to the question board and assigns it status open.
     *
     * @param model   The details of the new poll.
     * @param boardId The ID of the question board that the poll should be assigned to.
     * @return The newly-created poll.
     * @throws NotFoundException  if the board doesn't exist.
     * @throws ForbiddenException if the startTime of the board is after the current time or
     *                            the board is closed.
     */
    public Poll createPoll(PollCreationBindingModel model, UUID boardId) {
        QuestionBoard board = questionBoardRepository.getById(boardId);

        //If the question board did not exist, throw a NotFoundException
        if (board == null) {
            throw new NotFoundException("Question board does not exist");
        }
        //If the question board already had a registered poll, throw a ConflictException.
        if (board.getPoll() != null) {
            throw new ConflictException("There is already a poll registered for this question board");
        }

        Instant now = Instant.now();

        //If the question board has not yet been opened or has already been closed, throw a ForbiddenException.
        if (now.isBefore(board.getStartTime().toInstant())
                || board.isClosed()) {
            throw new ForbiddenException("Question board is not active");
        }

        //Convert the binding model to a Poll and store this in the database.
        Poll poll = modelMapper.map(model, Poll.class);
        poll.setQuestionBoard(board);
        poll.setOpen(true);
        pollRepository.save(poll);

        return poll;
    }
}