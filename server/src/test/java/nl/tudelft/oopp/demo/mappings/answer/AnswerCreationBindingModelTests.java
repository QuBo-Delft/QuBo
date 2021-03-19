package nl.tudelft.oopp.demo.mappings.answer;

import nl.tudelft.oopp.demo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.demo.entities.Answer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnswerCreationBindingModelTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromAnswerCreationBindingModelToAnswer() {
        // Arrange
        AnswerCreationBindingModel model = new AnswerCreationBindingModel();
        model.setText("Test answer");

        // Act
        Answer answer = mapper.map(model, Answer.class);

        // Assert
        assertEquals(model.getText(), answer.getText());
    }
}
