package nl.tudelft.oopp.demo.mappings.question;

import nl.tudelft.oopp.demo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.demo.entities.Question;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class QuestionCreationBindingModelTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromQuestionToQuestionDetailsDto() {
        // Arrange
        QuestionCreationBindingModel model = new QuestionCreationBindingModel();
        model.setText("Test question");

        // Act
        Question question = mapper.map(model, Question.class);

        // Assert
        assertEquals(model.getText(), question.getText());
    }
}
