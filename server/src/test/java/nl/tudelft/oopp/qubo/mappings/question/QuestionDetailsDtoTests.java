package nl.tudelft.oopp.qubo.mappings.question;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.UUID;
import nl.tudelft.oopp.qubo.config.custommappings.QuestionToQuestionDetailsDtoConverter;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.entities.Answer;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionVote;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class QuestionDetailsDtoTests {
    private ModelMapper mapper;

    /**
     * Initialise mapper and add custom mapping configuration.
     */
    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();

        var questionToQuestionDetailsDtoConverter = new QuestionToQuestionDetailsDtoConverter(mapper);
        questionToQuestionDetailsDtoConverter.init();
    }

    @Test
    public void mapFromQuestionToQuestionDetailsDto() {
        // Arrange
        Question q = new Question();
        q.setId(UUID.fromString("8e80cddb-72ec-44c2-a702-a4d9b54a6961"));
        q.setText("Question Text");
        q.setAuthorName("Someone");
        q.setAnswered(Timestamp.valueOf("2021-03-02 05:02:00"));
        q.setTimestamp(Timestamp.valueOf("2021-03-01 00:02:00"));

        HashSet<Answer> answerSet = new HashSet<>();
        answerSet.add(new Answer());
        answerSet.add(new Answer());

        q.setAnswers(answerSet);

        HashSet<QuestionVote> voteSet = new HashSet<>();
        voteSet.add(new QuestionVote());
        voteSet.add(new QuestionVote());
        voteSet.add(new QuestionVote());

        q.setVotes(voteSet);

        // Act
        QuestionDetailsDto dto = mapper.map(q, QuestionDetailsDto.class);

        // Assert
        assertEquals(q.getId(), dto.getId());
        assertEquals(q.getText(), dto.getText());
        assertEquals(q.getAuthorName(), dto.getAuthorName());
        assertEquals(q.getTimestamp(), dto.getTimestamp());
        assertEquals(q.getAnswered(), dto.getAnswered());
        assertEquals(q.getAnswers().size(), dto.getAnswers().size());
        assertEquals(q.getVotes().size(), dto.getUpvotes());
    }
}
