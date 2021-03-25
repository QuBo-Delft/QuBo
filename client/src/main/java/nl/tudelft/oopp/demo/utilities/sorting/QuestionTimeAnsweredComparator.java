package nl.tudelft.oopp.demo.utilities.sorting;

import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;

import java.util.Comparator;

public class QuestionTimeAnsweredComparator implements Comparator<QuestionDetailsDto> {

    /**
     * This method compares the time at which two questions were answered, and returns an integer.
     *
     * @param o1    A QuestionDetailsDto that is to be compared with o2.
     * @param o2    A QuestionDetailsDto that is to be compared with o1.
     *
     * @return 0 if they were answered at the same time, 1 if o1 was answered before o2,
     * and -1 if o1 was answered after o2. This ensures that the list of questions starts with
     * the question that was answered last.
     */
    @Override
    public int compare(QuestionDetailsDto o1, QuestionDetailsDto o2) {
        if (o1.getAnswered().equals(o2.getAnswered())){
            return 0;
        } else if (o1.getAnswered().before(o2.getAnswered())) {
            return 1;
        } else {
            return -1;
        }
    }
}
