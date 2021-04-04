package nl.tudelft.oopp.qubo.dtos.answer;

import javax.validation.constraints.NotNull;

/**
 * The Answer creation binding model.
 */
public class AnswerCreationBindingModel {

    @NotNull
    private String text;

    /**
     * Instantiates a new Answer creation binding model.
     */
    public AnswerCreationBindingModel() {
    }

    /**
     * Gets text.
     *
     * @return The text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text.
     *
     * @param text The text.
     */
    public void setText(String text) {
        this.text = text;
    }
}
