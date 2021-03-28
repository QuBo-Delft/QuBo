package nl.tudelft.oopp.demo.mappings.polloption;

import nl.tudelft.oopp.demo.dtos.polloption.PollOptionCreationDto;
import nl.tudelft.oopp.demo.entities.PollOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PollOptionCreationDtoTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromPollOptionToPollOptionCreationDto() {
        // Arrange
        PollOption pollOption = new PollOption();
        pollOption.setId(UUID.randomUUID());
        pollOption.setText("Test option");

        // Act
        PollOptionCreationDto pollOptionModel = mapper
                .map(pollOption, PollOptionCreationDto.class);

        // Assert
        assertEquals(pollOptionModel.getId(), pollOption.getId());
    }
}
