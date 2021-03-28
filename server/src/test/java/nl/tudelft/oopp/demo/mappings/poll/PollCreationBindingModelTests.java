package nl.tudelft.oopp.demo.mappings.poll;

import nl.tudelft.oopp.demo.config.custommappings.PollCreationBindingModelToPollConverter;
import nl.tudelft.oopp.demo.config.custommappings.QuestionToQuestionDetailsDtoConverter;
import nl.tudelft.oopp.demo.dtos.poll.PollCreationBindingModel;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.PollOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PollCreationBindingModelTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();

        var pollCreationBindingModelToPollConverter = new PollCreationBindingModelToPollConverter(mapper);
        pollCreationBindingModelToPollConverter.init();
    }

    @Test
    public void mapFromPollBindingModelToPoll() {
        // Arrange
        PollCreationBindingModel model = new PollCreationBindingModel();
        model.setText("Test question");

        String option1 = "A";
        String option2 = "B";
        HashSet<String> optionSet = new HashSet<>();
        optionSet.add(option1);
        optionSet.add(option2);
        model.setPollOptions(optionSet);

        // Act
        Poll poll = mapper.map(model, Poll.class);

        // Assert
        PollOption[] options = poll.getPollOptions().toArray(new PollOption[0]);

        assertEquals(model.getText(), poll.getText());
        assertEquals(optionSet.size(), options.length);
        assertNotEquals(options[0].getText(), options[1].getText());
        assertTrue(options[0].getText().equals(option1)
                || options[0].getText().equals(option2));
        assertTrue(options[1].getText().equals(option1)
                || options[1].getText().equals(option2));
    }
}
