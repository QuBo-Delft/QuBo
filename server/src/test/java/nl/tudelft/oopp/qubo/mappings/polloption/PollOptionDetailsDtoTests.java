package nl.tudelft.oopp.qubo.mappings.polloption;

import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionDetailsDto;
import nl.tudelft.oopp.qubo.entities.PollOption;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class PollOptionDetailsDtoTests {
    private ModelMapper mapper;

    /**
     * Sets up the modelmapper used for tests. Uses the custom mapping for Polls to PollDetailsDtos.
     */
    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromPollOptionToPollOptionDetailsDto() {
        // Arrange
        PollOption option = new PollOption();
        option.setId(UUID.randomUUID());
        option.setText("Option A");
        PollOption option2 = new PollOption();
        option2.setId(UUID.randomUUID());
        option2.setText("Option B");

        // Act
        PollOptionDetailsDto pollOptionDto = mapper.map(option, PollOptionDetailsDto.class);

        // Assert
        assertEquals(option.getId(), pollOptionDto.getOptionId());
        assertEquals(option.getText(), pollOptionDto.getOptionText());
    }

}
