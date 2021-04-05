package nl.tudelft.oopp.qubo.dtos.polloption;

import java.util.Objects;
import java.util.UUID;

/**
 * The Poll option result DTO.
 */
public class PollOptionResultDto {
    private UUID id;

    private String text;

    private int votes;

    /**
     * Instantiates a new Poll option result DTO.
     */
    public PollOptionResultDto() {

    }

    /**
     * Gets id.
     *
     * @return The id.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id The id.
     */
    public void setId(UUID id) {
        this.id = id;
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

    /**
     * Gets votes.
     *
     * @return The votes.
     */
    public int getVotes() {
        return votes;
    }

    /**
     * Sets votes.
     *
     * @param votes The votes.
     */
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
