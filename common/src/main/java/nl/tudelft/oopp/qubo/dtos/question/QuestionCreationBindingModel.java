package nl.tudelft.oopp.qubo.dtos.question;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * The Question creation binding model.
 */
public class QuestionCreationBindingModel {

    @NotNull
    @Length(min = 8)
    private String text;

    @NotNull
    @Length(min = 1)
    private String authorName;

    /**
     * Instantiates a new Question creation binding model.
     */
    public QuestionCreationBindingModel() {
    }

    /**
     * Gets text.
     *
     * @return the text
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

    /**
     * Gets author name.
     *
     * @return the author name
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Sets author name.
     *
     * @param authorName The author name.
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
