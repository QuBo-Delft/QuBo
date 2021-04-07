package nl.tudelft.oopp.qubo.utilities;

import java.sql.Timestamp;
import java.util.Set;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class QuestionToStringConverterTests {
    @Test
    public void convertToString_withQuestions_worksCorrectly() {
        // Arrange
        QuestionDetailsDto question1 = new QuestionDetailsDto();
        question1.setTimestamp(Timestamp.valueOf("2020-03-01 10:02:31"));
        question1.setText("Test question 1");
        question1.setUpvotes(5);
        question1.setAuthorName("Author 1");
        question1.setAnswers(Set.of());

        QuestionDetailsDto question2 = new QuestionDetailsDto();
        question2.setTimestamp(Timestamp.valueOf("2020-03-01 10:02:30"));
        question2.setText("Test question 2");
        question2.setUpvotes(15);
        question2.setAuthorName("Author 2");

        AnswerDetailsDto answer = new AnswerDetailsDto();
        answer.setText("Test answer");
        answer.setTimestamp(Timestamp.valueOf("2020-03-01 10:12:35"));

        question2.setAnswers(Set.of(answer));

        QuestionDetailsDto[] questions = {question1, question2};

        String expected = "Questions:\n\n"
            + "[01/03/2020 10:02] Author 2: (15↑)\n"
            + "  Test question 2\n"
            + "    - Test answer [01/03/2020 10:12]\n\n"
            + "[01/03/2020 10:02] Author 1: (5↑)\n"
            + "  Test question 1\n\n";

        // Act
        String actual = QuestionToStringConverter.convertToString(questions);

        // Assert
        assertEquals(expected, actual);
    }
}
