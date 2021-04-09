package nl.tudelft.oopp.qubo.utilities.sorting;

import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionVotesComparatorTest {

    private final Sorting.QuestionVotesComparator comparator = new Sorting.QuestionVotesComparator();
    private QuestionDetailsDto question1;

    /**
     * Sets up the QuestionDetailsDto used in all tests.
     */
    @BeforeEach
    public void setUp() {
        question1 = new QuestionDetailsDto();
        question1.setText("Is the universe infinitely large?");
        question1.setAuthorName("Smart Hamster");
        question1.setTimestamp(Timestamp.from(Instant.now()));
        question1.setAnswered(Timestamp.from(Instant.now()));
        question1.setAnswers(new HashSet<AnswerDetailsDto>());
        question1.setUpvotes(25);
    }

    //Test if the comparison of a question with itself will return 0.
    @Test
    public void testSameQuestions() {
        //Act
        int compare = comparator.compare(question1, question1);

        //Assert
        assertEquals(compare, 0);
    }

    //Test if the comparison of a question with a question that has a higher number of votes
    //will return 1.
    @Test
    public void testCompareQuestionWithDifferentQuestionWithMoreVotes() {
        //Arrange
        QuestionDetailsDto question2 = new QuestionDetailsDto();
        question2.setText("Why did the chicken cross the road?");
        question2.setAuthorName("Chicken Two");
        question2.setTimestamp(Timestamp.from(Instant.now()));
        question2.setAnswered(Timestamp.from(Instant.now()));
        question2.setAnswers(new HashSet<AnswerDetailsDto>());
        question2.setUpvotes(30);

        //Act
        int compare = comparator.compare(question1, question2);

        //Assert
        assertEquals(compare, 1);
    }

    //Test if the comparison of a question with a question that has a higher number of votes
    //will return -1.
    @Test
    public void testCompareQuestionWithDifferentQuestionWithLessVotes() {
        //Arrange
        QuestionDetailsDto question2 = new QuestionDetailsDto();
        question2.setText("Why did the chicken cross the road?");
        question2.setAuthorName("Chicken Two");
        question2.setTimestamp(Timestamp.from(Instant.now()));
        question2.setAnswered(Timestamp.from(Instant.now()));
        question2.setAnswers(new HashSet<AnswerDetailsDto>());
        question2.setUpvotes(20);

        //Act
        int compare = comparator.compare(question1, question2);

        //Assert
        assertEquals(compare, -1);
    }
}
