package nl.tudelft.oopp.qubo.mappings.questionvote;

import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.qubo.entities.QuestionVote;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class QuestionVoteDetailsDtoTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapQuestionVoteToQuestionVoteDetailsDto() {
        // Arrange
        QuestionVote questionVote = new QuestionVote();
        questionVote.setId(UUID.fromString("144d4d04-2e12-4c69-8845-1225582e2b4d"));

        // Act
        QuestionVoteDetailsDto dto = mapper.map(questionVote, QuestionVoteDetailsDto.class);

        // Assert
        assertEquals(dto.getId(), questionVote.getId());
    }
}
