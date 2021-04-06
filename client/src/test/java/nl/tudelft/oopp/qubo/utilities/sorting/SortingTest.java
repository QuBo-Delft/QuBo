package nl.tudelft.oopp.qubo.utilities.sorting;

import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortingTest {

    private final Sorting.QuestionVotesComparator comparatorVotes = new Sorting.QuestionVotesComparator();
    private final Sorting.QuestionTimeAnsweredComparator comparatorAnswered = new Sorting
            .QuestionTimeAnsweredComparator();
    private QuestionDetailsDto[] questions;
    private List<AnswerDetailsDto> answerList;
    private Timestamp now;

    /**
     * Set the Timestamp used in test methods to the time at which the test execution starts.
     */
    @BeforeEach
    public void setUp() {
        now = Timestamp.from(Instant.now());
    }

    //Tests if a long mixed array will be sorted correctly by sortOnTimeAnswered.
    @Test
    public void testSortOnTimeAnsweredMixed() {
        //Arrange
        questions = new QuestionDetailsDto[10];
        questions[0] = new QuestionDetailsDto();
        questions[1] = new QuestionDetailsDto();
        questions[2] = new QuestionDetailsDto();
        questions[3] = new QuestionDetailsDto();
        questions[4] = new QuestionDetailsDto();
        questions[5] = new QuestionDetailsDto();
        questions[6] = new QuestionDetailsDto();
        questions[7] = new QuestionDetailsDto();
        questions[8] = new QuestionDetailsDto();
        questions[9] = new QuestionDetailsDto();

        questions[0].setAnswered(new Timestamp(now.getTime() + 8 * 1000));
        questions[1].setAnswered(new Timestamp(now.getTime() + 1000));
        questions[2].setAnswered(new Timestamp(now.getTime() + 2 * 1000));
        questions[3].setAnswered(now);
        questions[4].setAnswered(new Timestamp(now.getTime() + 4 * 1000));
        questions[5].setAnswered(new Timestamp(now.getTime() + 7 * 1000));
        questions[6].setAnswered(new Timestamp(now.getTime() + 3 * 1000));
        questions[7].setAnswered(new Timestamp(now.getTime() + 6 * 1000));
        questions[8].setAnswered(new Timestamp(now.getTime() + 4 * 1000));
        questions[9].setAnswered(new Timestamp(now.getTime() + 5 * 1000));

        //Act
        Sorting.sortOnTimeAnswered(questions);

        //Assert
        assertTrue(questions[0].getAnswered().getTime() >= questions[1].getAnswered().getTime());
        assertTrue(questions[1].getAnswered().getTime() >= questions[2].getAnswered().getTime());
        assertTrue(questions[2].getAnswered().getTime() >= questions[3].getAnswered().getTime());
        assertTrue(questions[3].getAnswered().getTime() >= questions[4].getAnswered().getTime());
        assertTrue(questions[4].getAnswered().getTime() >= questions[5].getAnswered().getTime());
        assertTrue(questions[5].getAnswered().getTime() >= questions[6].getAnswered().getTime());
        assertTrue(questions[6].getAnswered().getTime() >= questions[7].getAnswered().getTime());
        assertTrue(questions[7].getAnswered().getTime() >= questions[8].getAnswered().getTime());
        assertTrue(questions[8].getAnswered().getTime() >= questions[9].getAnswered().getTime());
    }

    //Tests if a sorted array will stay sorted by sortOnTimeAnswered.
    @Test
    public void testSortOnTimeAnsweredSorted() {
        //Arrange
        questions = new QuestionDetailsDto[3];
        questions[0] = new QuestionDetailsDto();
        questions[1] = new QuestionDetailsDto();
        questions[2] = new QuestionDetailsDto();

        questions[0].setAnswered(new Timestamp(now.getTime() + 2 * 1000));
        questions[1].setAnswered(new Timestamp(now.getTime() + 1000));
        questions[2].setAnswered(now);

        //Act
        Sorting.sortOnTimeAnswered(questions);

        //Assert
        assertTrue(questions[0].getAnswered().getTime() >= questions[1].getAnswered().getTime());
        assertTrue(questions[1].getAnswered().getTime() >= questions[2].getAnswered().getTime());
    }

    //Tests if an array sorted in reverse order will be sorted correctly by sortOnTimeAnswered.
    @Test
    public void testSortOnTimeAnsweredReversed() {
        //Arrange
        questions = new QuestionDetailsDto[3];
        questions[0] = new QuestionDetailsDto();
        questions[1] = new QuestionDetailsDto();
        questions[2] = new QuestionDetailsDto();

        questions[0].setAnswered(now);
        questions[1].setAnswered(new Timestamp(now.getTime() + 1000));
        questions[2].setAnswered(new Timestamp(now.getTime() + 2 * 1000));

        //Act
        Sorting.sortOnTimeAnswered(questions);

        //Assert
        assertTrue(questions[0].getAnswered().getTime() >= questions[1].getAnswered().getTime());
        assertTrue(questions[1].getAnswered().getTime() >= questions[2].getAnswered().getTime());
    }

    //Tests if a medium-sized mixed array is sorted correctly by sortOnTimeAnswered.
    @Test
    public void testSortOnUpvotesMixed() {
        //Arrange
        questions = new QuestionDetailsDto[6];
        questions[0] = new QuestionDetailsDto();
        questions[1] = new QuestionDetailsDto();
        questions[2] = new QuestionDetailsDto();
        questions[3] = new QuestionDetailsDto();
        questions[4] = new QuestionDetailsDto();
        questions[5] = new QuestionDetailsDto();

        questions[0].setUpvotes(10);
        questions[1].setUpvotes(1);
        questions[2].setUpvotes(3);
        questions[3].setUpvotes(5);
        questions[4].setUpvotes(0);
        questions[5].setUpvotes(10);

        //Act
        Sorting.sortOnUpvotes(questions);

        //Assert
        assertTrue(questions[0].getUpvotes() >= questions[1].getUpvotes());
        assertTrue(questions[1].getUpvotes() >= questions[2].getUpvotes());
        assertTrue(questions[2].getUpvotes() >= questions[3].getUpvotes());
        assertTrue(questions[3].getUpvotes() >= questions[4].getUpvotes());
        assertTrue(questions[4].getUpvotes() >= questions[5].getUpvotes());
    }

    //Tests if a sorted array is sorted correctly by sortOnTimeAnswered.
    @Test
    public void testSortOnUpvotesSorted() {
        //Arrange
        questions = new QuestionDetailsDto[3];
        questions[0] = new QuestionDetailsDto();
        questions[1] = new QuestionDetailsDto();
        questions[2] = new QuestionDetailsDto();

        questions[0].setUpvotes(10);
        questions[1].setUpvotes(3);
        questions[2].setUpvotes(1);

        //Act
        Sorting.sortOnUpvotes(questions);

        //Assert
        assertTrue(questions[0].getUpvotes() >= questions[1].getUpvotes());
        assertTrue(questions[1].getUpvotes() >= questions[2].getUpvotes());
    }

    //Tests if an array that was sorted in reversed order is sorted correctly by sortOnTimeAnswered.
    @Test
    public void testSortOnUpvotesReversed() {
        //Arrange
        questions = new QuestionDetailsDto[3];
        questions[0] = new QuestionDetailsDto();
        questions[1] = new QuestionDetailsDto();
        questions[2] = new QuestionDetailsDto();

        questions[0].setUpvotes(1);
        questions[1].setUpvotes(3);
        questions[2].setUpvotes(10);

        //Act
        Sorting.sortOnUpvotes(questions);

        //Assert
        assertTrue(questions[0].getUpvotes() >= questions[1].getUpvotes());
        assertTrue(questions[1].getUpvotes() >= questions[2].getUpvotes());
    }

    @Test
    public void testAnswerSortOnTimePostedMixed() {
        //Arrange
        answerList = new ArrayList<>();
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());

        answerList.get(0).setTimestamp(new Timestamp(now.getTime() + 2 * 1000));
        answerList.get(1).setTimestamp(new Timestamp(now.getTime()));
        answerList.get(2).setTimestamp(new Timestamp(now.getTime() + 6 * 1000));
        answerList.get(3).setTimestamp(new Timestamp(now.getTime() + 4 * 1000));
        answerList.get(4).setTimestamp(new Timestamp(now.getTime() + 8 * 1000));

        //Act
        Sorting.sortAnswersOnTime(answerList);

        //Assert
        assertTrue(answerList.get(0).getTimestamp().getTime() <= answerList.get(1).getTimestamp().getTime());
        assertTrue(answerList.get(1).getTimestamp().getTime() <= answerList.get(2).getTimestamp().getTime());
        assertTrue(answerList.get(2).getTimestamp().getTime() <= answerList.get(3).getTimestamp().getTime());
        assertTrue(answerList.get(3).getTimestamp().getTime() <= answerList.get(4).getTimestamp().getTime());
    }

    @Test
    public void testAnswerSortOnTimePostedReversed() {
        //Arrange
        answerList = new ArrayList<>();
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());

        answerList.get(0).setTimestamp(new Timestamp(now.getTime() + 8 * 1000));
        answerList.get(1).setTimestamp(new Timestamp(now.getTime() + 6 * 1000));
        answerList.get(2).setTimestamp(new Timestamp(now.getTime() + 4 * 1000));
        answerList.get(3).setTimestamp(new Timestamp(now.getTime() + 2 * 1000));
        answerList.get(4).setTimestamp(new Timestamp(now.getTime()));

        //Act
        Sorting.sortAnswersOnTime(answerList);

        //Assert
        assertTrue(answerList.get(0).getTimestamp().getTime() <= answerList.get(1).getTimestamp().getTime());
        assertTrue(answerList.get(1).getTimestamp().getTime() <= answerList.get(2).getTimestamp().getTime());
        assertTrue(answerList.get(2).getTimestamp().getTime() <= answerList.get(3).getTimestamp().getTime());
        assertTrue(answerList.get(3).getTimestamp().getTime() <= answerList.get(4).getTimestamp().getTime());
    }

    @Test
    public void testAnswerSortOnTimePostedSorted() {
        //Arrange
        answerList = new ArrayList<>();
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());
        answerList.add(new AnswerDetailsDto());

        answerList.get(0).setTimestamp(new Timestamp(now.getTime()));
        answerList.get(1).setTimestamp(new Timestamp(now.getTime() + 2 * 1000));
        answerList.get(2).setTimestamp(new Timestamp(now.getTime() + 4 * 1000));
        answerList.get(3).setTimestamp(new Timestamp(now.getTime() + 6 * 1000));
        answerList.get(4).setTimestamp(new Timestamp(now.getTime() + 8 * 1000));

        //Act
        Sorting.sortAnswersOnTime(answerList);

        //Assert
        assertTrue(answerList.get(0).getTimestamp().getTime() <= answerList.get(1).getTimestamp().getTime());
        assertTrue(answerList.get(1).getTimestamp().getTime() <= answerList.get(2).getTimestamp().getTime());
        assertTrue(answerList.get(2).getTimestamp().getTime() <= answerList.get(3).getTimestamp().getTime());
        assertTrue(answerList.get(3).getTimestamp().getTime() <= answerList.get(4).getTimestamp().getTime());
    }
}
