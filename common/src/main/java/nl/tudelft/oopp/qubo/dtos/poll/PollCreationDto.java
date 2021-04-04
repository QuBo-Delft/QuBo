package nl.tudelft.oopp.qubo.dtos.poll;

import java.util.UUID;

/**
 * The Poll creation DTO.
 */
public class PollCreationDto {
    private UUID pollId;

    /**
     * Instantiates a new Poll creation DTO.
     */
    public PollCreationDto() {
    }

    /**
     * Gets poll id.
     *
     * @return the poll id
     */
    public UUID getPollId() {
        return this.pollId;
    }

    /**
     * Sets poll id.
     *
     * @param pollId The poll id.
     */
    public void setPollId(UUID pollId) {
        this.pollId = pollId;
    }
}
