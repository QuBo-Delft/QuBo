package nl.tudelft.oopp.qubo.utilities.sorting;

import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An utility class for sorting.
 */
public class Sorting {

    /**
     * This inner class contains the method that sets the sorting order used in sortOnTimeAnswered.
     */
    public static class QuestionTimeAnsweredComparator implements Comparator<QuestionDetailsDto> {

        /**
         * This method compares the time at which two questions were answered, and returns an integer.
         *
         * @param o1    A QuestionDetailsDto that is to be compared with o2.
         * @param o2    A QuestionDetailsDto that is to be compared with o1.
         *
         * @return 0 if they were answered at the same time, 1 if o1 was answered before o2,
         *      and -1 if o1 was answered after o2. This ensures that the list of questions starts with
         *      the question that was answered last.
         */
        @Override
        public int compare(QuestionDetailsDto o1, QuestionDetailsDto o2) {
            if (o1.getAnswered().equals(o2.getAnswered())) {
                return 0;
            } else if (o1.getAnswered().before(o2.getAnswered())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * This inner class contains the method that sets the sorting order used in sortOnUpvotes.
     */
    public static class QuestionVotesComparator implements Comparator<QuestionDetailsDto> {

        /**
         * This method compares the number of votes of two questions, and returns an integer.
         *
         * @param o1    The QuestionDetailsDto that is to be compared with o2.
         * @param o2    The QuestionDetailsDto that is to be compared with o1.
         *
         * @return the inverse of the comparason of timestamps if the upvotes are equal,
         *      1 if the number of votes of o1 is smaller than that of o2,
         *      and -1 if the number of votes of o1 is greater than that of o2. This ensures that the
         *      list of questions starts with the question with the greatest number of upvotes,
         *      and secondly the order of equal upvotes is goes from recent to longest ago.
         */
        @Override
        public int compare(QuestionDetailsDto o1, QuestionDetailsDto o2) {
            if (o1.getUpvotes() == o2.getUpvotes()) {
                return o1.getTimestamp().compareTo(o2.getTimestamp()) * -1;
            } else if (o1.getUpvotes() < o2.getUpvotes()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * Sorts the array of questions in-place based on the time at which they were answered. The
     * question that was answered last will be placed at the front of the returned array.
     *
     * @param questions The array of questions that should be sorted.
     */
    public static void sortOnTimeAnswered(QuestionDetailsDto[] questions) {
        //Convert the array to a list
        List<QuestionDetailsDto> sortList = Arrays.asList(questions);

        //Sort the list in non-decreasing order. The question that was answered last is placed at the
        //front of the list.
        sortList.sort(new QuestionTimeAnsweredComparator());
    }

    /**
     * Sorts the array of questions in-place based on the number of upvotes that they have received. The
     * question that has the highest number of upvotes is placed at the front of the returned array.
     *
     * @param questions The array of questions that should be sorted.
     */
    public static void sortOnUpvotes(QuestionDetailsDto[] questions) {
        //Convert the array to a list
        List<QuestionDetailsDto> sortList = Arrays.asList(questions);

        //Sort the list in non-decreasing order. The question that has the highest number of upvotes
        //is placed at the front of the list.
        Collections.sort(sortList, new QuestionVotesComparator());
    }

    /**
     * Sorts the list of questions based on the TimeStamp they have been posted.
     *
     * @param answers   The list of answers that should be sorted.
     */
    public static void sortAnswersOnTime(List<AnswerDetailsDto> answers) {
        answers.sort(new AnswerTimePostedComparator());
    }

    public static class AnswerTimePostedComparator implements Comparator<AnswerDetailsDto> {

        /**
         * This method compares the time at which two textual answers were posted, and returns an integer.
         *
         * @param o1    A AnswerDetailsDto that is to be compared with o2.
         * @param o2    A AnswerDetailsDto that is to be compared with o1.
         *
         * @return 0 if they were answered at the same time, -1 if o1 was answered before o2,
         *      and 1 if o1 was answered after o2. This ensures that the list of answers starts with
         *      the answer that was post first.
         */
        @Override
        public int compare(AnswerDetailsDto o1, AnswerDetailsDto o2) {
            if (o1.getTimestamp().equals(o2.getTimestamp())) {
                return 0;
            } else if (o1.getTimestamp().before(o2.getTimestamp())) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
