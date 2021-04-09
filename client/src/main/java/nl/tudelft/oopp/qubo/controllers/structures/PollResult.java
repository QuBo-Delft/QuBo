package nl.tudelft.oopp.qubo.controllers.structures;

import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionResultDto;

import java.util.Set;

public class PollResult {
    private String text;
    private PollOptionResultDto[] pollOptionResults;

    /**
     * Initialises a PollResult object with the provided text and poll option results.
     *
     * @param text              The text that should be associated with the poll result.
     * @param pollOptionResults The poll option results that should be associated with the poll result.
     */
    public PollResult(String text, PollOptionResultDto[] pollOptionResults) {
        this.text = text;
        this.pollOptionResults = pollOptionResults;
    }

    /**
     * Gets the text associated with the poll.
     *
     * @return The text associated with the poll.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text associated with the poll.
     *
     * @param text The text that should be associated with the poll.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the set of poll option results of the poll.
     *
     * @return The set of poll option results associated with the poll.
     */
    public PollOptionResultDto[] getPollOptionResults() {
        return pollOptionResults;
    }

    /**
     * Sets the set of poll option results of the poll.
     *
     * @param pollOptionResults The set of poll option results of the poll.
     */
    public void setPollOptionResults(PollOptionResultDto[] pollOptionResults) {
        this.pollOptionResults = pollOptionResults;
    }
}
