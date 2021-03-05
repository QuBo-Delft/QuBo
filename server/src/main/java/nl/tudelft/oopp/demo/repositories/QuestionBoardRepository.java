package nl.tudelft.oopp.demo.repositories;

import java.util.UUID;

import nl.tudelft.oopp.demo.entities.QuestionBoard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("QuestionBoardRepository")
public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, UUID> {
}
