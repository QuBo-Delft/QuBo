package nl.tudelft.oopp.qubo.dtos.polloption;

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
}
