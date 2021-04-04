package nl.tudelft.oopp.qubo.mappings.questionboard;

import java.sql.Timestamp;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class QuestionBoardCreationBindingModelTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromQuestionBoardCreationBindingModelToQuestionBoard() {
        // Arrange
        QuestionBoardCreationBindingModel model = new QuestionBoardCreationBindingModel();
        model.setTitle("Test QuestionBoard");
        model.setStartTime(Timestamp.valueOf("2021-03-01 00:02:00"));

        // Act
        QuestionBoard qb = mapper.map(model, QuestionBoard.class);

        // Assert
        assertEquals(model.getTitle(), qb.getTitle());
        assertEquals(model.getStartTime(), qb.getStartTime());
    }
}
