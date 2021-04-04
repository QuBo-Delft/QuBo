package nl.tudelft.oopp.qubo.dtos.poll;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * The Poll creation binding model.
 */
public class PollCreationBindingModel {
    @NotNull
    @Length(min = 1)
    private String text;

    @NotNull
    @Size(min = 2)
    private Set<String> pollOptions;

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

    /**
     * Gets poll options.
     *
     * @return The poll options.
     */
    public Set<String> getPollOptions() {
        return pollOptions;
    }

    /**
     * Sets poll options.
     *
     * @param pollOptions The poll options.
     */
    public void setPollOptions(Set<String> pollOptions) {
        this.pollOptions = pollOptions;
    }
}
