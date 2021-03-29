package nl.tudelft.oopp.demo.mappings.polloption;

import nl.tudelft.oopp.demo.dtos.polloption.PollOptionDetailsDto;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.PollOption;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PollOptionDetailsDtoTests {
    private ModelMapper mapper;

    /**
     * Sets up the modelmapper used for tests. Uses the custom mapping for Polls to PollDetailsDtos.
     */
    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromPollOptionToPollOptionDetailsDto() {
        // Arrange
        QuestionBoard questionBoard = new QuestionBoard();

        Poll poll = new Poll();
        poll.setId(UUID.randomUUID());
        poll.setText("Test poll");
        poll.setOpen(true);
        poll.setQuestionBoard(questionBoard);

        PollOption option = new PollOption();
        option.setId(UUID.randomUUID());
        option.setText("Option A");
        option.setPoll(poll);
        PollOption option2 = new PollOption();
        option2.setId(UUID.randomUUID());
        option2.setText("Option B");
        option2.setPoll(poll);

        Set<PollOption> optionSet = new HashSet<>();
        optionSet.add(option);
        optionSet.add(option2);
        poll.setPollOptions(optionSet);

        // Act
        PollOptionDetailsDto pollOptionDto = mapper.map(option, PollOptionDetailsDto.class);

        // Assert
        assertEquals(option.getId(), pollOptionDto.getOptionId());
        assertEquals(option.getText(), pollOptionDto.getOptionText());
    }

}
