package nl.tudelft.oopp.demo.config.custommappings;

import javax.annotation.PostConstruct;
import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.demo.entities.Question;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class QuestionToQuestionDetailsDtoConverter {

    private final ModelMapper mapper;

    public QuestionToQuestionDetailsDtoConverter(ModelMapper modelMapper) {
        this.mapper = modelMapper;
    }

    /**
     * Create custom mapping for converting from Question to QuestionDetailsDto.
     * Set the "upvotes" attribute to the number of QuestionVotes.
     */
    @PostConstruct
    public void init() {
        mapper.addMappings(new PropertyMap<Question, QuestionDetailsDto>() {
            @Override
            protected void configure() {
                map().setUpvotes(source.getVotes().size());
            }
        });
    }
}
