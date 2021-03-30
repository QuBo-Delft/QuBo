package nl.tudelft.oopp.qubo.config.custommappings;

import nl.tudelft.oopp.qubo.dtos.poll.PollDetailsDto;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionDetailsDto;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.annotation.PostConstruct;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class PollToPollDetailsDtoConverter {

    private final ModelMapper mapper;

    public PollToPollDetailsDtoConverter(ModelMapper modelMapper) {
        this.mapper = modelMapper;
    }

    /**
     * Create custom mapping for converting from a String to a PollOption.
     * Set the "text" attribute to the String content.
     */
    @PostConstruct
    public void init() {
        Converter<Set<PollOption>, Set<PollOptionDetailsDto>> converter = new AbstractConverter<>() {
            @Override
            protected Set<PollOptionDetailsDto> convert(Set<PollOption> source) {
                Set<PollOptionDetailsDto> result = source.stream()
                        .map(pollOption -> mapper.map(pollOption, PollOptionDetailsDto.class))
                        .collect(toSet());
                return result;
            }
        };
        mapper.addMappings(new PropertyMap<Poll, PollDetailsDto>() {
            @Override
            protected void configure() {
                using(converter).map(source.getPollOptions(), destination.getOptions());
            }
        });
    }
}
