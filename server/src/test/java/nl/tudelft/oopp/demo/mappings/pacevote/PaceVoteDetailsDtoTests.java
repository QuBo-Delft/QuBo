package nl.tudelft.oopp.demo.mappings.pacevote;

import nl.tudelft.oopp.demo.dtos.pacevote.PaceVoteCreationDto;
import nl.tudelft.oopp.demo.dtos.pacevote.PaceVoteDetailsDto;
import nl.tudelft.oopp.demo.entities.PaceType;
import nl.tudelft.oopp.demo.entities.PaceVote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
