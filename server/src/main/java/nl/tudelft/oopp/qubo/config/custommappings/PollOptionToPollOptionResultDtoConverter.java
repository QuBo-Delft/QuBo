package nl.tudelft.oopp.qubo.config.custommappings;

import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionResultDto;
import nl.tudelft.oopp.qubo.entities.PollOption;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * The Poll option to poll option result dto converter.
 */
@Component
public class PollOptionToPollOptionResultDtoConverter {
    private final ModelMapper mapper;

    /**
     * Initialise PollOptionToPollOptionResultDtoConverter.
     *
     * @param modelMapper The modelMapper object.
     */
    public PollOptionToPollOptionResultDtoConverter(ModelMapper modelMapper) {
        this.mapper = modelMapper;
    }

    /**
     * Create custom mapping for converting from PollOption to PollOptionResultDto.
     * Set the "votes" attribute to the amount of poll votes.
     */
    @PostConstruct
    public void init() {
        mapper.addMappings(new PropertyMap<PollOption, PollOptionResultDto>() {
            @Override
            protected void configure() {
                map().setVotes(source.getVotes().size());
            }
        });
    }
}
