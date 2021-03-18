package nl.tudelft.oopp.demo.mappings.answer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.UUID;
import nl.tudelft.oopp.demo.dtos.answer.AnswerCreationDto;
import nl.tudelft.oopp.demo.entities.Answer;
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
    public void mapFromAnswerToAnswerDetailsDto() {
        // Arrange
        Answer answer = new Answer();
        answer.setId(UUID.fromString("93c8f1d7-7265-4596-9320-ae34895ff64a"));

        // Act
        AnswerCreationDto dto = mapper.map(answer, AnswerCreationDto.class);

        // Assert
        assertEquals(answer.getId(), dto.getId());
    }
}
