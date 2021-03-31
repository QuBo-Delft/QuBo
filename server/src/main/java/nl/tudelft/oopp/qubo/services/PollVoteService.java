package nl.tudelft.oopp.qubo.services;

import java.util.UUID;
import nl.tudelft.oopp.qubo.entities.PollOption;
import nl.tudelft.oopp.qubo.entities.PollVote;
import nl.tudelft.oopp.qubo.repositories.PollOptionRepository;
import nl.tudelft.oopp.qubo.repositories.PollVoteRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ForbiddenException;
import nl.tudelft.oopp.qubo.services.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PollVoteService {
    private final PollVoteRepository pollVoteRepository;
    private final PollOptionRepository pollOptionRepository;

    public PollVoteService(PollVoteRepository pollVoteRepository,
                           PollOptionRepository pollOptionRepository) {
        this.pollVoteRepository = pollVoteRepository;
        this.pollOptionRepository = pollOptionRepository;
    }

    /**
     * Method for registering PollVotes.
     *
     * @param optionId The id of the QuestionBoard.
     * @return The PollVote object that was just registered.
     * @throws NotFoundException  if the option ID provided does not exist.
     * @throws ForbiddenException if the poll is closed.
     */
    public PollVote registerVote(UUID optionId) {
        PollOption option = pollOptionRepository.getById(optionId);
        if (option == null) {
            throw new NotFoundException("Poll option does not exist");
        }

        if (!option.getPoll().isOpen()) {
            throw new ForbiddenException("Poll is not active");
        }

        PollVote vote = new PollVote();
        vote.setPollOption(option);
        pollVoteRepository.save(vote);
        return vote;
    }
}
