package nl.tudelft.oopp.demo.config.custommappings;

import nl.tudelft.oopp.demo.dtos.poll.PollCreationBindingModel;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.PollOption;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class PollCreationBindingModelToPollConverter {

    private final ModelMapper mapper;

    public PollCreationBindingModelToPollConverter(ModelMapper modelMapper) {
        this.mapper = modelMapper;
    }

    /**
     * Create custom mapping for converting from a String to a PollOption.
     * Set the "text" attribute to the String content.
     */
    @PostConstruct
    public void init() {
        Converter<Set<String>, Set<PollOption>> converter =
                new AbstractConverter<Set<String>, Set<PollOption>>() {
                Set<PollOption> result = new HashSet<>();
                @Override
                protected Set<PollOption> convert(Set<String> source) {
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
