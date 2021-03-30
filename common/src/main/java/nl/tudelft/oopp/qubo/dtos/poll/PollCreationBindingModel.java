package nl.tudelft.oopp.qubo.dtos.poll;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class PollCreationBindingModel {
    @NotNull
    @Length(min = 1)
    private String text;

    @NotNull
    @Size(min = 2)
    private Set<String> pollOptions;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<String> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(Set<String> pollOptions) {
        this.pollOptions = pollOptions;
    }
}
