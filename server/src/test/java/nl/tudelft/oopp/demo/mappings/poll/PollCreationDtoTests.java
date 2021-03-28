package nl.tudelft.oopp.demo.mappings.poll;

import nl.tudelft.oopp.demo.dtos.poll.PollCreationDto;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.PollOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PollCreationDtoTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromPollToPollCreationDto() {
        // Arrange
        Poll poll = new Poll();
        poll.setId(UUID.fromString("8e80cddb-72ec-44c2-a702-a4d9b54a6961"));
        poll.setText("Test poll");
        poll.setOpen(true);

        HashSet<PollOption> pollOptionSet = new HashSet<>();
        pollOptionSet.add(new PollOption());
        pollOptionSet.add(new PollOption());

        poll.setPollOptions(pollOptionSet);

        // Act
        PollCreationDto pollDto = mapper.map(poll, PollCreationDto.class);

        // Assert
        assertEquals(poll.getId(), pollDto.getPollId());
    }
}
