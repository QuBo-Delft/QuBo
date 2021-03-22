package nl.tudelft.oopp.demo.mappings.questionvote;

import nl.tudelft.oopp.demo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.demo.entities.QuestionVote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
