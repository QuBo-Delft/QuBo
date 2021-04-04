package nl.tudelft.oopp.qubo.repositories;

import java.util.UUID;
import javax.transaction.Transactional;
import nl.tudelft.oopp.qubo.entities.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Ban repository.
 */
@Repository("BanRepository")
public interface BanRepository extends JpaRepository<Ban, UUID> {
    /**
     * Gets by id.
     *
     * @param banId the ban id
     * @return The by id.
     */
    Ban getById(UUID banId);

    /**
     * Delete ban by id.
     *
     * @param banId The ban id.
     */
    @Transactional
    void deleteBanById(UUID banId);
}
