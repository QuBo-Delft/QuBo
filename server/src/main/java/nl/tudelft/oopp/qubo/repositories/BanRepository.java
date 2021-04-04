package nl.tudelft.oopp.qubo.repositories;

import nl.tudelft.oopp.qubo.entities.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository("BanRepository")
public interface BanRepository extends JpaRepository<Ban, UUID> {
    Ban getById(UUID banId);

    @Transactional
    void deleteBanById(UUID banId);
}
