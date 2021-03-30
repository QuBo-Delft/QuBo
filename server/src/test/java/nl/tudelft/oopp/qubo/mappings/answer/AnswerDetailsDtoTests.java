package nl.tudelft.oopp.qubo.mappings.answer;

import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.qubo.entities.Answer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnswerDetailsDtoTests {
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
        answer.setTimestamp(Timestamp.from(Instant.now()));
        answer.setText("Test");

        // Act
        AnswerDetailsDto dto = mapper.map(answer, AnswerDetailsDto.class);

        // Assert
        assertEquals(answer.getId(), dto.getId());
        assertEquals(answer.getTimestamp(), dto.getTimestamp());
        assertEquals(answer.getText(), dto.getText());
    }
}
