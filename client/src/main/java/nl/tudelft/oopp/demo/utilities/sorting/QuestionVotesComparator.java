package nl.tudelft.oopp.demo.utilities.sorting;

import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;

import java.util.Comparator;

public class QuestionVotesComparator implements Comparator<QuestionDetailsDto> {

    /**
     * This method compares the number of votes of two questions, and returns an integer.
     *
     * @param o1    The QuestionDetailsDto that is to be compared with o2.
     * @param o2    The QuestionDetailsDto that is to be compared with o1.
     *
     * @return 0 if the number of votes are equal, 1 if the number of votes of o1 is smaller than that
     * of o2, and -1 if the number of votes of o1 is greater than that of o2. This ensures that the list
     * of questions starts with the question with the greatest number of upvotes.
     */
    @Override
    public int compare(QuestionDetailsDto o1, QuestionDetailsDto o2) {
        if (o1.getUpvotes() == o2.getUpvotes()) {
            return 0;
        } else if (o1.getUpvotes() < o2.getUpvotes()) {
            return 1;
        } else {
            return -1;
        }
    }
}
