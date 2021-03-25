package nl.tudelft.oopp.demo.utilities.sorting;

import nl.tudelft.oopp.demo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionTimeAnsweredComparatorTest {

    private final QuestionTimeAnsweredComparator comparator = new QuestionTimeAnsweredComparator();
    private QuestionDetailsDto question1;

    //Set up the question1 object used in each test before each test is executed.
    @BeforeEach
    public void setUp () {
        question1 = new QuestionDetailsDto();
        question1.setText("Is the universe infinitely large?");
        question1.setAuthorName("Smart Hamster");
        question1.setTimestamp(Timestamp.from(Instant.now()));
        question1.setAnswered(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        question1.setAnswers(new HashSet<AnswerDetailsDto>());
        question1.setUpvotes(25);
    }

    //Test if the comparison of a question with itself will return 0.
    @Test
    public void testSameQuestions () {
        //Act
        int compare = comparator.compare(question1, question1);

        //Assert
        assertEquals(compare, 0);
    }

    //Test if the comparison of two equal questions will return 0.
    @Test
    public void testEqualQuestions () {
        //Arrange
        QuestionDetailsDto question2 = new QuestionDetailsDto();
        question2.setText("Why did the chicken cross the road?");
        question2.setAuthorName("Chicken Two");
        question2.setTimestamp(Timestamp.from(Instant.now()));
        question2.setAnswered(question1.getAnswered());
        question2.setAnswers(new HashSet<AnswerDetailsDto>());
        question2.setUpvotes(25);

        //Act
        int compare = comparator.compare(question1, question2);

        //Assert
        assertEquals(compare, 0);
    }

    //Test if the comparison of a question with a question that was answered later returns 1.
    @Test
    public void testCompareQuestionWithDifferentQuestionAnsweredLater () {
        //Arrange
        QuestionDetailsDto question2 = new QuestionDetailsDto();
        question2.setText("Why did the chicken cross the road?");
        question2.setAuthorName("Chicken Two");
        question2.setTimestamp(Timestamp.from(Instant.now()));
        question2.setAnswered(new Timestamp(question1.getTimestamp().getTime() + 10 * 1000));
        question2.setAnswers(new HashSet<AnswerDetailsDto>());
        question2.setUpvotes(30);

        //Act
        int compare = comparator.compare(question1, question2);

        //Assert
        assertEquals(compare, 1);
    }

    //Test if the comparison of a question with a question that was answered earlier returns -1.
    @Test
    public void testCompareQuestionWithDifferentQuestionAnsweredEarlier () {
        //Arrange
        QuestionDetailsDto question2 = new QuestionDetailsDto();
        question2.setText("Why did the chicken cross the road?");
        question2.setAuthorName("Chicken Two");
        question2.setTimestamp(Timestamp.from(Instant.now()));
        question2.setAnswered(new Timestamp(question1.getTimestamp().getTime() - 10 * 1000));
        question2.setAnswers(new HashSet<AnswerDetailsDto>());
        question2.setUpvotes(20);

        //Act
        int compare = comparator.compare(question1, question2);

        //Assert
        assertEquals(compare, -1);
    }
}
