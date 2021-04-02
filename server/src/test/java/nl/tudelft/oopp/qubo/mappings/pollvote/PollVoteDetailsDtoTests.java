package nl.tudelft.oopp.qubo.mappings.pollvote;

import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.pollvote.PollVoteDetailsDto;
import nl.tudelft.oopp.qubo.entities.PollVote;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class PollVoteDetailsDtoTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapPollVoteToPollVoteCreationDto() {
        // Arrange
        PollVote pollVote = new PollVote();
        pollVote.setId(UUID.fromString("144d4d04-2e12-4c69-8845-1225582e2b4d"));

        // Act
        PollVoteDetailsDto dto = mapper.map(pollVote, PollVoteDetailsDto.class);

        // Assert
        assertEquals(dto.getId(), pollVote.getId());
    }
}
