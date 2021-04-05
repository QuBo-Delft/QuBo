package nl.tudelft.oopp.qubo.dtos.question;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * The Question editing binding model.
 */
public class QuestionEditingBindingModel {

    @NotNull
    @Length(min = 8)
    private String text;

    /**
     * Instantiates a new Question editing binding model.
     */
    public QuestionEditingBindingModel() {
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
