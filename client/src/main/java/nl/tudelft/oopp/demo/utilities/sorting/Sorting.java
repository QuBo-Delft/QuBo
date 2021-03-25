package nl.tudelft.oopp.demo.utilities.sorting;

import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sorting {

    /**
     * Sorts the array of questions based on the time at which they were answered. The question that
     * was answered last will be placed at the front of the returned array.
     *
     * @param questions The array of questions that should be sorted.
     * @return The array of questions sorted in non-decreasing order based on the time at which they
     * were answered.
     */
    public QuestionDetailsDto[] sortOnTimeAnswered (QuestionDetailsDto[] questions) {
        //Convert the array to a list
        List<QuestionDetailsDto> sortList = Arrays.asList(questions);

        //Sort the list in non-decreasing order. The question that was answered last is placed at the
        //front of the list.
        Collections.sort(sortList, new QuestionTimeAnsweredComparator());

        //Return the sorted array of QuestionDetailsDtos
        return (QuestionDetailsDto[]) sortList.toArray();
    }

    /**
     * Sorts the array of questions based on the number of upvotes that they have received. The
     * question that has the highest number of upvotes is placed at the front of the returned array.
     *
     * @param questions The array of questions that should be sorted.
     * @return The array of questions sorted in non-decreasing order based on the number of upvotes
     * that they have received.
     */
    public QuestionDetailsDto[] sortOnUpvotes (QuestionDetailsDto[] questions) {
        //Convert the array to a list
        List<QuestionDetailsDto> sortList = Arrays.asList(questions);

        //Sort the list in non-decreasing order. The question that has the highest number of upvotes
        //is placed at the front of the list.
        Collections.sort(sortList, new QuestionVotesComparator());

        //Return the sorted array of QuestionDetailsDtos
        return (QuestionDetailsDto[]) sortList.toArray();
    }
}
