package nl.tudelft.oopp.qubo.dtos.pollvote;

import java.util.UUID;

/**
 * The Poll vote details DTO.
 */
public class PollVoteDetailsDto {
    private UUID id;

    /**
     * Instantiates a new Poll vote details DTO.
     */
    public PollVoteDetailsDto() {
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
}
