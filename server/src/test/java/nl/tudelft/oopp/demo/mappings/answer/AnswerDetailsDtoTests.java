package nl.tudelft.oopp.demo.mappings.answer;

import java.sql.Timestamp;
import java.util.UUID;
import nl.tudelft.oopp.demo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.demo.entities.Answer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class AnswerDetailsDtoTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromAnswerToAnswerDetailsDto() {
        //Arrange
        Answer answer = new Answer();
        answer.setId(UUID.fromString("93c8f1d7-7265-4596-9320-ae34895ff64a"));
        answer.setText("Answer Text");
        answer.setTimestamp(Timestamp.valueOf("2021-03-01 10:05:00"));

        // Act
        AnswerDetailsDto dto = mapper.map(answer, AnswerDetailsDto.class);

        // Assert
        assertEquals(answer.getId(), dto.getId());
        assertEquals(answer.getText(), dto.getText());
        assertEquals(answer.getTimestamp(), dto.getTimestamp());
    }
}
