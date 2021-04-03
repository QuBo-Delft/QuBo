package nl.tudelft.oopp.qubo.services;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.poll.PollCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionResultDto;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.PollOptionRepository;
import nl.tudelft.oopp.qubo.repositories.PollRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ConflictException;
import nl.tudelft.oopp.qubo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class PollService {
    private final QuestionBoardRepository questionBoardRepository;

    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;

    private final ModelMapper modelMapper;

    private final CurrentTimeProvider currentTimeProvider;

    /**
     * Creates an instance of PollService.
     *
     * @param currentTimeProvider     The CurrentTimeProvider.
     * @param questionBoardRepository A QuestionBoardRepository.
     * @param pollRepository          A PollRepository.
     * @param pollOptionRepository    A PollOptionRepository.
     * @param modelMapper             The ModelMapper.
     */
    public PollService(
        QuestionBoardRepository questionBoardRepository,
        PollRepository pollRepository,
        PollOptionRepository pollOptionRepository,
        CurrentTimeProvider currentTimeProvider,
        ModelMapper modelMapper) {
        this.questionBoardRepository = questionBoardRepository;
        this.pollRepository = pollRepository;
        this.pollOptionRepository = pollOptionRepository;
        this.currentTimeProvider = currentTimeProvider;
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
     * Set the open attribute of the poll to false.
     *
     * @param pollId    The ID of the poll.
     * @return The updated poll.
     * @throws NotFoundException if the board does not exist.
     * @throws ConflictException if the poll was already closed.
     */
    public Poll closePoll(UUID pollId) {

        Poll poll = pollRepository.getById(pollId);

        // A 404 is thrown if the poll does not exist
        if (poll == null) {
            throw new NotFoundException("Poll does not exist");
        }

        // A 409 is thrown if the poll has already been closed
        if (!poll.isOpen()) {
            throw new ConflictException("The poll has already been closed");
        }

        // Set the poll to closed
        poll.setOpen(false);
        pollRepository.save(poll);

        return poll;
    }

    /**
     * Retrieves a Poll through its ID.
     *
     * @param pollId The ID of the the poll that should be retrieved.
     * @return The Poll associated with the ID.
     */
    public Poll getPollById(UUID pollId) {
        return pollRepository.getById(pollId);
    }

    /**
     * Retrieves a PollOption through its ID.
     *
     * @param pollOptionId The ID of the poll option that should be retrieved.
     * @return The PollOption associated with the ID.
     */
    public PollOption getPollOptionById(UUID pollOptionId) {
        return pollOptionRepository.getById(pollOptionId);
    }

    /**
     * Deletes a poll from the database.
     *
     * @param pollId The ID of the poll that is to be deleted.
     */
    public void deletePoll(UUID pollId) {
        pollRepository.deletePollById(pollId);
    }


    /**
     * Retrieve a set of PollOption objects corresponding to the question board's poll.
     * Throw 404 if the question board does not exist.
     * Throw 404 if there is no poll in this question board.
     * Throw 403 if the poll is open.
     *
     * @param boardId   The board ID.
     * @return A set of PollOption objects.
     */
    public Set<PollOptionResultDto> getPollResults(UUID boardId) {
        QuestionBoard board = questionBoardRepository.getById(boardId);

        // Check if the question board exists
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The question board does not exist.");
        }

        Poll poll = board.getPoll();
        // Check if the poll exists
        if (poll == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no poll in this question board.");
        }

        // Check if the poll is open
        if (poll.isOpen()) {
            throw new ForbiddenException("The poll is open.");
        }

        Set<PollOption> pollOptions = pollOptionRepository.getPollOptionsByPoll(poll);

        Set<PollOptionResultDto> pollOptionResults = modelMapper.map(pollOptions,
                new TypeToken<Set<PollOptionResultDto>>() {}.getType());

        return pollOptionResults;
    }
}
