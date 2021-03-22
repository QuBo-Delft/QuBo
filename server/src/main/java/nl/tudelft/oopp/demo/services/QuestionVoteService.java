package nl.tudelft.oopp.demo.services;

import java.util.UUID;

import nl.tudelft.oopp.demo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.QuestionVote;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.QuestionVoteRepository;
import nl.tudelft.oopp.demo.services.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class QuestionVoteService {
    private final QuestionVoteRepository questionVoteRepository;
    private final QuestionRepository questionRepository;

    /**
     * Creates an instance of the QuestionVoteService.
     *
     * @param questionVoteRepository The QuestionVoteRepository.
     * @param questionRepository     The QuestionRepository.
     */
    public QuestionVoteService(
        QuestionVoteRepository questionVoteRepository,
        QuestionRepository questionRepository) {
        this.questionVoteRepository = questionVoteRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * Method for registering QuestionVotes by
     * adding a new QuestionVote to the database.
     *
     * @param questionId The question ID.
     * @return The QuestionVote object that was just registered.
     * @throws NotFoundException when the Question ID provided does not exist.
     */
    public QuestionVote registerVote(UUID questionId) {
        Question question = questionRepository.getQuestionById(questionId);
        // Check if question with this ID exists
        if (question == null) {
            throw new NotFoundException("Question does not exist");
        }

        QuestionVote vote = new QuestionVote();
        vote.setQuestion(question);
        questionVoteRepository.save(vote);
        return vote;
    }

    /**
     * Get question vote by ID.
     *
     * @param voteId The vote ID.
     * @return QuestionVote with provided ID.
     */
    public QuestionVote getQuestionVoteById(UUID voteId) {
        return questionVoteRepository.getQuestionVoteById(voteId);
    }

    /**
     * Delete vote.
     *
     * @param questionVote The QuestionVote to be deleted.
     */
    public void deleteVote(QuestionVote questionVote) {
        questionVoteRepository.deleteQuestionVoteById(questionVote.getId());
    }


}
