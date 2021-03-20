package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Question;

import nl.tudelft.oopp.demo.entities.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("QuestionRepository")
public interface QuestionRepository extends JpaRepository<Question, QuestionBoard> {
    Set<Question> getQuestionByQuestionBoard(QuestionBoard boardId);

    Question getQuestionById(UUID questionId);
}
