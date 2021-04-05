package nl.tudelft.oopp.qubo.mappings.pacevote;

import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteDetailsDto;
import nl.tudelft.oopp.qubo.entities.PaceType;
import nl.tudelft.oopp.qubo.entities.PaceVote;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class PaceVoteDetailsDtoTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapPaceVoteToPaceVoteDetailsDto() {
        // Arrange
        PaceVote vote = new PaceVote();
        vote.setId(UUID.randomUUID());
        vote.setPaceType(PaceType.JUST_RIGHT);

        // Act
        PaceVoteDetailsDto dto = mapper.map(vote, PaceVoteDetailsDto.class);

        // Assert
        assertEquals(dto.getId(), vote.getId());
        assertEquals(dto.getPaceType().toString(), vote.getPaceType().toString());
    }
}
