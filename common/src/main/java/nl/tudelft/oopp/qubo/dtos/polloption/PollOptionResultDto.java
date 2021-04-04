package nl.tudelft.oopp.qubo.dtos.polloption;

import java.util.Objects;
import java.util.UUID;

public class PollOptionResultDto {
    private UUID id;

    private String text;

    private int votes;

    public PollOptionResultDto() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PollOptionResultDto that = (PollOptionResultDto) o;
        return votes == that.votes && Objects.equals(id, that.id)
            && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, votes);
    }
}
