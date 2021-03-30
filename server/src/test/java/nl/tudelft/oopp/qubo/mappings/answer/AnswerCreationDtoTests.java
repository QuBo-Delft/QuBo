package nl.tudelft.oopp.qubo.mappings.answer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerCreationDto;
import nl.tudelft.oopp.qubo.entities.Answer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class AnswerCreationDtoTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromAnswerToAnswerCreationDto() {
        // Arrange
        Answer answer = new Answer();
        answer.setId(UUID.fromString("93c8f1d7-7265-4596-9320-ae34895ff64a"));

        // Act
        AnswerCreationDto dto = mapper.map(answer, AnswerCreationDto.class);

        // Assert
        assertEquals(answer.getId(), dto.getId());
    }
}
