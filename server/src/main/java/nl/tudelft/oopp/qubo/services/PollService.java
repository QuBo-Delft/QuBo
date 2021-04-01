package nl.tudelft.oopp.qubo.services;

import java.time.Instant;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.poll.PollCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.PollRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ConflictException;
import nl.tudelft.oopp.qubo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class PollService {
    private final QuestionBoardRepository questionBoardRepository;

    private final PollRepository pollRepository;

    private final ModelMapper modelMapper;

    private final CurrentTimeProvider currentTimeProvider;

    /**
     * Creates an instance of PollService.
     *
     * @param questionBoardRepository A QuestionBoardRepository.
     * @param pollRepository          A PollRepository.
     * @param modelMapper             The ModelMapper.
     * @param currentTimeProvider     The CurrentTimeProvider.
     */
    public PollService(
        QuestionBoardRepository questionBoardRepository, PollRepository pollRepository,
        ModelMapper modelMapper,
        CurrentTimeProvider currentTimeProvider) {
        this.questionBoardRepository = questionBoardRepository;
        this.pollRepository = pollRepository;
        this.modelMapper = modelMapper;
        this.currentTimeProvider = currentTimeProvider;
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

        Instant now = currentTimeProvider.getCurrentTime();

        //If the question board has not yet been opened or has already been closed, throw a ForbiddenException.
        if (now.isBefore(board.getStartTime().toInstant())
            || board.isClosed()) {
            throw new ForbiddenException("Question board is not active");
        }

        //Convert the binding model to a Poll and store this in the database.
        Poll poll = modelMapper.map(model, Poll.class);
        poll.setQuestionBoard(board);
        poll.setOpen(true);

        for (PollOption option : poll.getPollOptions()) {
            option.setPoll(poll);
        }

        pollRepository.save(poll);

        return poll;
    }

    /**
     * Retrieves a Poll through its ID.
     *
     * @param pollId    The ID of the the poll that should be retrieved.
     * @return The Poll associated with the ID.
     */
    public Poll getPollById(UUID pollId) {
        return pollRepository.getById(pollId);
    }

    /**
     * Deletes a poll from the database.
     *
     * @param pollId    The ID of the poll that is to be deleted.
     */
    public void deletePoll(UUID pollId) {
        pollRepository.deletePollById(pollId);
    }
}
