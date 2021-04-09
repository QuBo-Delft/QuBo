package nl.tudelft.oopp.qubo.utilities.sorting;

import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionResultDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PollOptionResultsComparatorTest {
    private final Sorting.PollOptionResultsComparator comparator = new Sorting
            .PollOptionResultsComparator();
    private PollOptionResultDto pollOption1;

    /**
     * Sets up the PollOptionResultDto used in all tests.
     */
    @BeforeEach
    public void setUp() {
        pollOption1 = new PollOptionResultDto();
        pollOption1.setId(UUID.fromString("1b219900-27c0-4e40-9d0a-a0fa3951b224"));
    }

    //Test if the comparison of a poll option with itself will return 0.
    @Test
    public void testSamePollOption() {
        //Act
        int compare = comparator.compare(pollOption1, pollOption1);

        //Assert
        assertEquals(0, compare);
    }

    //Test if the comparison of a poll option with the same ID will return 0.
    @Test
    public void testSamePollOptionId() {
        //Arrange
        PollOptionResultDto option2 = new PollOptionResultDto();
        option2.setId(UUID.fromString("1b219900-27c0-4e40-9d0a-a0fa3951b224"));

        //Act
        int compare = comparator.compare(pollOption1, option2);

        //Assert
        assertEquals(0, compare);
    }

    //Test if the comparison of poll option with a lesser poll option ID than another poll option returns -1.
    @Test
    public void testLesserPollOptionId() {
        //Arrange
        PollOptionResultDto option2 = new PollOptionResultDto();
        option2.setId(UUID.fromString("2b219900-27c0-4e40-9d0a-a0fa3951b224"));

        //Act
        int compare = comparator.compare(pollOption1, option2);

        //Assert
        assertEquals(-1, compare);
    }

    //Test if the comparison of a greater poll option ID than another returns 1.
    @Test
    public void testGreaterPollOptionId() {
        //Arrange
        PollOptionResultDto option2 = new PollOptionResultDto();
        option2.setId(UUID.fromString("0b219900-27c0-4e40-9d0a-a0fa3951b224"));

        //Act
        int compare = comparator.compare(pollOption1, option2);

        //Assert
        assertEquals(1, compare);
    }
}
