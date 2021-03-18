package nl.tudelft.oopp.demo.mappings.answer;

import nl.tudelft.oopp.demo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.demo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.entities.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnswerCreationBindingModelTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromAnswerToAnswerDetailsDto() {
        // Arrange
        AnswerCreationBindingModel model = new AnswerCreationBindingModel();
        model.setText("Test question");

        // Act
        Answer answer = mapper.map(model, Answer.class);

        // Assert
        assertEquals(model.getText(), answer.getText());
    }
}
