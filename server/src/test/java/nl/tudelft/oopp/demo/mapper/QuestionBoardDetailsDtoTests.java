package nl.tudelft.oopp.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.UUID;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class QuestionBoardDetailsDtoTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromQuestionBoardToQuestionBoardDetailsDto() {
        //Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setId(UUID.fromString("8e80cddb-72ec-44c2-a702-a4d9b54a6961"));
        qb.setModeratorCode(UUID.fromString("a228b64c-feea-4aef-9ddc-9c3484562188"));
        qb.setTitle("Test QuestionBoard");
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:02:00"));
        qb.setEndTime(Timestamp.valueOf("2021-03-01 10:00:00"));

        // Act
        QuestionBoardDetailsDto dto = mapper.map(qb, QuestionBoardDetailsDto.class);

        // Assert
        assertEquals(qb.getId(), dto.getId());
        assertEquals(qb.getTitle(), dto.getTitle());
        assertEquals(qb.getStartTime(), dto.getStartTime());
        assertEquals(qb.getEndTime(), dto.getEndTime());
    }
}
