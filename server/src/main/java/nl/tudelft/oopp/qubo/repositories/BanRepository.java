package nl.tudelft.oopp.qubo.repositories;

import java.util.UUID;
import javax.transaction.Transactional;
import nl.tudelft.oopp.qubo.entities.Ban;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import javax.transaction.Transactional;
import java.util.UUID;

/**
 * The Ban repository.
 */
@Repository("BanRepository")
public interface BanRepository extends JpaRepository<Ban, UUID> {
    /**
     * Get ban by id.
     *
     * @param banId The ban id.
     * @return The by id.
     */
    Ban getById(UUID banId);

    Set<Ban> getBanByQuestionBoard(QuestionBoard questionBoard);

    /**
     * Delete ban by id.
     *
     * @param banId The ban id.
     */
    @Transactional
    void deleteBanById(UUID banId);
}
