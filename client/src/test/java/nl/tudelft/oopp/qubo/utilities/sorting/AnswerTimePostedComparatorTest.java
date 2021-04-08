package nl.tudelft.oopp.qubo.utilities.sorting;

import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnswerTimePostedComparatorTest {

    private final Sorting.AnswerTimePostedComparator comparator = new Sorting
        .AnswerTimePostedComparator();
    private AnswerDetailsDto answer1;
    private Timestamp now;

    /**
     * Sets up the AnswerDetailsDto used in all tests.
     */
    @BeforeEach
    public void setUp() {
        now = Timestamp.from(Instant.now());

        answer1 = new AnswerDetailsDto();
        answer1.setTimestamp(now);
    }

    //Test if the comparison of an answer with itself will return 0.
    @Test
    public void testSameAnswer() {
        //Act
        int compare = comparator.compare(answer1, answer1);

        //Assert
        assertEquals(0, compare);
    }

    //Test if the comparison of an answer with the same TimeStamp will return 0.
    @Test
    public void testSameTimeAnswer() {
        //Arrange
        AnswerDetailsDto answer2 = new AnswerDetailsDto();
        answer2.setTimestamp(now);

        //Act
        int compare = comparator.compare(answer1, answer2);

        //Assert
        assertEquals(0, compare);
    }

    //Test if the comparison of an answer with another answer that was answered earlier returns -1.
    @Test
    public void testEarlierTimeAnswer() {
        //Arrange
        AnswerDetailsDto answer2 = new AnswerDetailsDto();
        answer2.setTimestamp(new Timestamp(now.getTime() + 1000));

        //Act
        int compare = comparator.compare(answer1, answer2);

        //Assert
        assertEquals(-1, compare);
    }

    //Test if the comparison of an answer with another answer that was answered later returns 1.
    @Test
    public void testLaterTimeAnswer() {
        //Arrange
        AnswerDetailsDto answer2 = new AnswerDetailsDto();
        answer2.setTimestamp(new Timestamp(now.getTime() + 1000));

        //Act
        int compare = comparator.compare(answer2, answer1);

        //Assert
        assertEquals(1, compare);
    }
}
