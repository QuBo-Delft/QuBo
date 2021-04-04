package nl.tudelft.oopp.qubo.config.custommappings;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import nl.tudelft.oopp.qubo.dtos.poll.PollCreationBindingModel;
import nl.tudelft.oopp.qubo.entities.Poll;
import nl.tudelft.oopp.qubo.entities.PollOption;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

/**
 * The Poll creation binding model to poll converter.
 */
@Component
public class PollCreationBindingModelToPollConverter {

    private final ModelMapper mapper;

    /**
     * Instantiates a new Poll creation binding model to poll converter.
     *
     * @param modelMapper The model mapper.
     */
    public PollCreationBindingModelToPollConverter(ModelMapper modelMapper) {
        this.mapper = modelMapper;
    }

    /**
     * Create custom mapping for converting from a String to a PollOption.
     * Set the "text" attribute to the String content.
     */
    @PostConstruct
    public void init() {
        Converter<Set<String>, Set<PollOption>> converter = new AbstractConverter<>() {
            @Override
            protected Set<PollOption> convert(Set<String> source) {
                Set<PollOption> result = new HashSet<>();
                source.forEach(string -> {
                    PollOption option = new PollOption();
                    option.setText(string);
                    result.add(option);
                });
                return result;
            }
        };
        mapper.addMappings(new PropertyMap<PollCreationBindingModel, Poll>() {
            @Override
            protected void configure() {
                using(converter).map(source.getPollOptions(), destination.getPollOptions());
            }
        });
    }
}
