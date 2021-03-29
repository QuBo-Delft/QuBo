package nl.tudelft.oopp.demo.mappings.poll;

import nl.tudelft.oopp.demo.config.custommappings.PollToPollDetailsDtoConverter;
import nl.tudelft.oopp.demo.dtos.poll.PollDetailsDto;
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

import static org.junit.jupiter.api.Assertions.*;

public class PollDetailsDtoTests {
    private ModelMapper mapper;

    /**
     * Sets up the modelmapper used for tests. Uses the custom mapping for Polls to PollDetailsDtos.
     */
    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();

        var pollToPollDetailsDtoConverter = new PollToPollDetailsDtoConverter(mapper);
        pollToPollDetailsDtoConverter.init();
    }

    @Test
    public void mapFromPollToPollDetailsDto() {
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
        PollOption option2 = new PollOption();
        option2.setId(UUID.randomUUID());
        option2.setText("Option B");

        Set<PollOption> optionSet = new HashSet<>();
        optionSet.add(option);
        optionSet.add(option2);
        poll.setPollOptions(optionSet);

        // Act
        PollDetailsDto pollDto = mapper.map(poll, PollDetailsDto.class);

        // Assert
        PollOptionDetailsDto[] optionDtos = pollDto.getOptions().toArray(new PollOptionDetailsDto[0]);

        assertEquals(poll.getId(), pollDto.getId());
        assertEquals(poll.getText(), pollDto.getText());
        assertEquals(poll.getPollOptions().size(), pollDto.getOptions().size());
        assertNotEquals(optionDtos[0], optionDtos[1]);
        assertTrue((optionDtos[0].getOptionId() == option.getId()
                && optionDtos[0].getOptionText().equals(option.getText()))
                || (optionDtos[0].getOptionId() == option2.getId()
                && optionDtos[0].getOptionText().equals(option2.getText())));
        assertNotNull(optionDtos[0].getOptionText());
        assertTrue((optionDtos[1].getOptionId() == option.getId()
                && optionDtos[1].getOptionText().equals(option.getText()))
                || (optionDtos[1].getOptionId() == option2.getId()
                && optionDtos[1].getOptionText().equals(option2.getText())));
    }
}
