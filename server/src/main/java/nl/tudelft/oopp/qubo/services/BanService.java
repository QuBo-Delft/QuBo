package nl.tudelft.oopp.qubo.services;

import nl.tudelft.oopp.qubo.entities.Ban;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.repositories.BanRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ConflictException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BanService {
    private final QuestionRepository questionRepository;
    private final BanRepository banRepository;

    /**
     * Creates an instance of a BanService.
     *
     * @param questionRepository    A QuestionRepository.
     * @param banRepository         A BanRepository.
     */
    public BanService(QuestionRepository questionRepository,
                      BanRepository banRepository) {
        this.questionRepository = questionRepository;
        this.banRepository = banRepository;
    }

    /**
     * Checks if the IP has already been banned from the question board,
     * if not, add the question board and banned IP to the repository.
     *
     * @param questionId    The ID of the question where the ban request originated from.
     * @return              True iff the ban is successful.
     * @throws ConflictException if the IP of the question is null.
     * @throws ConflictException if the IP has already been banned from the question board
     *                           the question was in.
     */
    public boolean banIp(UUID questionId) {
        Question question = questionRepository.getQuestionById(questionId);
        String ip = question.getIp();

        if (ip == null) {
            throw new ConflictException("The IP of the question is null.");
        }

        Ban ban = new Ban(question.getQuestionBoard(), ip);
        if (banRepository.getBanByQuestionBoard(question.getQuestionBoard()).contains(ban)) {
            throw new ConflictException("This IP has already been banned from the requested question board.");
        }

        banRepository.save(ban);
        return true;
    }
}
