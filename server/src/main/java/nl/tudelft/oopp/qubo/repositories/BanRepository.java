package nl.tudelft.oopp.qubo.repositories;

import java.util.Set;
import java.util.UUID;
import javax.transaction.Transactional;
import nl.tudelft.oopp.qubo.entities.Ban;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Ban repository.
 */
@Repository("BanRepository")
public interface BanRepository extends JpaRepository<Ban, UUID> {
    /**
     * Get ban by id.
     *
     * @param banId The ban id.
     * @return The ban if it exists.
     */
    Ban getById(UUID banId);

    /**
     * Get ban by question board and ip.
     *
     * @param questionBoard Question board.
     * @param ip            The IP address.
     * @return The ban if it exists.
     */
    Ban getBanByQuestionBoardAndIp(QuestionBoard questionBoard, String ip);

    /**
     * Get all bans by question board.
     *
     * @param questionBoard Question board.
     * @return The set of bans.
     */
    Set<Ban> getBanByQuestionBoard(QuestionBoard questionBoard);

    /**
     * Delete ban by id.
     *
     * @param banId The ban id.
     */
    @Transactional
    void deleteBanById(UUID banId);
}
