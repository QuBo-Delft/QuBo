package nl.tudelft.oopp.qubo.mappings.pacevote;

import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.PaceVote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaceVoteCreationBindingModelTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapPaceVoteCreationBindingModelToPaceVote() {
        // Arrange
        PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
        model.setPaceType(PaceType.JUST_RIGHT);
        // Act
        PaceVote vote = mapper.map(model, PaceVote.class);
        // Assert
        assertEquals(model.getPaceType().toString(), vote.getPaceType().toString());
    }


}
