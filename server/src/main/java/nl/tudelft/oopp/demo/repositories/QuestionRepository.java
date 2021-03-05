package nl.tudelft.oopp.demo.repositories;

import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("QuestionRepository")
public interface QuestionRepository extends JpaRepository<Question, UUID> {
}
