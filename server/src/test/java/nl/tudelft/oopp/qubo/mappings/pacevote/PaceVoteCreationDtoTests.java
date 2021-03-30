package nl.tudelft.oopp.qubo.mappings.pacevote;

import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationDto;
import nl.tudelft.oopp.qubo.entities.PaceType;
import nl.tudelft.oopp.qubo.entities.PaceVote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaceVoteCreationDtoTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapPaceVoteToPaceVoteCreationDto() {
        // Arrange
        PaceVote paceVote = new PaceVote();
        paceVote.setId(UUID.randomUUID());
        paceVote.setPaceType(PaceType.JUST_RIGHT);
        // Act
        PaceVoteCreationDto dto = mapper.map(paceVote, PaceVoteCreationDto.class);
        // Assert
        assertEquals(dto.getId(), paceVote.getId());
    }
}
