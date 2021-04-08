package nl.tudelft.oopp.qubo.utilities;

import java.text.SimpleDateFormat;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.utilities.sorting.Sorting;

/**
 * An utility class for converting questions into a formatted string.
 */
public class QuestionToStringConverter {
    /**
     * Converts questions into a string.
     *
     * @param questions The array of questions to be converted.
     * @return The output string.
     */
    public static String convertToString(QuestionDetailsDto[] questions) {
        StringBuilder sb = new StringBuilder();
        sb.append("Questions:\n\n");

        Sorting.sortOnTimestamp(questions);

        var dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (QuestionDetailsDto q : questions) {
            sb.append(q.getAuthorName());
            sb.append(":\n  ");
            sb.append(q.getText());
            sb.append(" (");
            sb.append(q.getUpvotes());
            sb.append("↑) [");
            sb.append(dateFormat.format(q.getTimestamp()));
            sb.append("]\n");

            for (AnswerDetailsDto a : q.getAnswers()) {
                sb.append("    - ");
                sb.append(a.getText());
                sb.append(" [");
                sb.append(dateFormat.format(a.getTimestamp()));
                sb.append("]\n");
            }

            sb.append('\n');
        }

        return sb.toString();
    }
}
