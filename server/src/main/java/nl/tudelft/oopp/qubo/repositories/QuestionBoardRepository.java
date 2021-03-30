package nl.tudelft.oopp.qubo.repositories;

import java.util.List;
import java.util.UUID;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("QuestionBoardRepository")
public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, UUID> {
    QuestionBoard getById(UUID id);

    QuestionBoard getByModeratorCode(UUID code);

    List<QuestionBoard> findAll();

}
