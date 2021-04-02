package nl.tudelft.oopp.qubo.mappings.polloption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import nl.tudelft.oopp.qubo.config.custommappings.PollOptionToPollOptionResultDtoConverter;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionResultDto;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import nl.tudelft.oopp.qubo.entities.PollVote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PollOptionResultDtoTests {
    private ModelMapper mapper;

    /**
     * Initialise mapper and add custom mapping configuration.
     */
    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();

        var pollOptionToPollOptionResultDtoConverter = new PollOptionToPollOptionResultDtoConverter(mapper);
        pollOptionToPollOptionResultDtoConverter.init();
    }

    @Test
    public void mapFromPollOptionToPollOptionResultDto() {
        // Arrange
        PollOption p = new PollOption();
        p.setId(UUID.randomUUID());
        p.setText("Option Text");

        PollVote vote = new PollVote();
        vote.setId(UUID.randomUUID());
        vote.setPollOption(p);

        PollVote vote2 = new PollVote();
        vote2.setId(UUID.randomUUID());
        vote2.setPollOption(p);

        Set<PollVote> votes = new HashSet<>();
        votes.add(vote);
        votes.add(vote2);

        p.setVotes(votes);

        Poll poll = new Poll();
        Set<PollOption> options = new HashSet<>();
        options.add(p);
        poll.setPollOptions(options);

        // Act
        PollOptionResultDto dto = mapper.map(p, PollOptionResultDto.class);

        // Assert
        assertEquals(p.getId(), dto.getId());
        assertEquals(p.getText(), dto.getText());
        assertEquals(dto.getVotes(), votes.size());
    }
}
