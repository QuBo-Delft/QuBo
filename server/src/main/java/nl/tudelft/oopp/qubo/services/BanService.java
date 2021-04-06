package nl.tudelft.oopp.qubo.services;

import java.util.UUID;
import nl.tudelft.oopp.qubo.entities.Ban;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.repositories.BanRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.services.exceptions.ConflictException;
import nl.tudelft.oopp.qubo.services.exceptions.ForbiddenException;
import org.springframework.stereotype.Service;

@Service
public class BanService {
    private final QuestionRepository questionRepository;
    private final BanRepository banRepository;

    /**
     * Creates an instance of a BanService.
     *
     * @param questionRepository A QuestionRepository.
     * @param banRepository      A BanRepository.
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
     * @param questionId The ID of the question where the ban request originated.
     * @throws ConflictException if the IP of the question is null.
     * @throws ConflictException if the IP has already been banned from the question board
     *                           the question was in.
     */
    public void banIp(UUID questionId) {
        Question question = questionRepository.getQuestionById(questionId);
        String ip = question.getIp();

        if (ip == null) {
            throw new ConflictException("The IP of the question is null.");
        }

        if (ipAlreadyBannedInBoard(question.getQuestionBoard(), ip)) {
            throw new ConflictException("This IP has already been banned from the requested question board.");
        }

        Ban ban = new Ban(question.getQuestionBoard(), ip);
        banRepository.save(ban);
    }

    /**
     * This method checks whether the IP to be banned already exists in the set of banned IPs of the
     * requested question board.
     *
     * @param questionBoard Question board containing the question where the ban request came from.
     * @param ip            IP of the question to be banned.
     * @return True iff the IP to be banned already exists in the list of banned IPs
     *         for the question board, otherwise returns false.
     */
    public boolean ipAlreadyBannedInBoard(QuestionBoard questionBoard, String ip) {
        Ban ban = banRepository.getBanByQuestionBoardAndIp(questionBoard, ip);

        return ban != null;
    }

    /**
     * This method checks whether the provided IP is banned from the specified QuestionBoard
     * and throws an exception if it is.
     *
     * @param questionBoard The question board.
     * @param ip            IP to be checked.
     * @throws ForbiddenException if the IP is banned from the question board
     */
    public void performBanCheck(QuestionBoard questionBoard, String ip) {
        if (ipAlreadyBannedInBoard(questionBoard, ip)) {
            throw new ForbiddenException("You are banned from this question board");
        }
    }
}
