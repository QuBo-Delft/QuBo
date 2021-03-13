package nl.tudelft.oopp.demo.mappings.question;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.UUID;
import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.entities.Question;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class QuestionDetailsDtoTests {
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
    }

    @Test
    public void mapFromQuestionToQuestionDetailsDto() {
        //Arrange
        Question q = new Question();
        q.setId(UUID.fromString("8e80cddb-72ec-44c2-a702-a4d9b54a6961"));
        q.setText("Question Text");
        q.setTimestamp(Timestamp.valueOf("2021-03-01 00:02:00"));

        HashSet<Answer> answerSet = new HashSet<>();
        answerSet.add(new Answer());
        answerSet.add(new Answer());

        q.setAnswers(answerSet);

        // Act
        QuestionDetailsDto dto = mapper.map(q, QuestionDetailsDto.class);

        // Assert
        assertEquals(q.getId(), dto.getId());
        assertEquals(q.getText(), dto.getText());
        assertEquals(q.getTimestamp(), dto.getTimestamp());
        assertEquals(q.getAnswers().size(), dto.getAnswers().size());
    }
}
