package nl.tudelft.oopp.demo.mapper;

import java.sql.Timestamp;
import java.util.UUID;
import nl.tudelft.oopp.demo.dtos.QuestionBoardDetailsDto;
import nl.tudelft.oopp.demo.dtos.bindingmodels.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
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
        //Arrange
        QuestionBoardCreationBindingModel model = new QuestionBoardCreationBindingModel();
        model.setTitle("Test QuestionBoard");
        model.setStartTime(Timestamp.valueOf("2021-03-01 00:02:00"));
        model.setEndTime(Timestamp.valueOf("2021-03-01 10:00:00"));

        // Act
        QuestionBoard qb = mapper.map(model, QuestionBoard.class);

        // Assert
        assertEquals(model.getTitle(), qb.getTitle());
        assertEquals(model.getStartTime(), qb.getStartTime());
        assertEquals(model.getEndTime(), qb.getEndTime());
    }
}
