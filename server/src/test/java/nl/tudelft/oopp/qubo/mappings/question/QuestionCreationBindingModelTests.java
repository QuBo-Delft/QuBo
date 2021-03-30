package nl.tudelft.oopp.qubo.mappings.question;

import nl.tudelft.oopp.qubo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.Question;
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
        model.setAuthorName("Someone");

        // Act
        Question question = mapper.map(model, Question.class);

        // Assert
        assertEquals(model.getText(), question.getText());
        assertEquals(model.getAuthorName(), question.getAuthorName());
    }
}
