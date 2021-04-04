package nl.tudelft.oopp.qubo.dtos.pacevote;

import java.util.UUID;

/**
 * The Pace vote creation DTO.
 */
public class PaceVoteCreationDto {

    private UUID id;

    /**
     * Instantiates a new Pace vote creation DTO.
     */
    public PaceVoteCreationDto() {
    }

    /**
     * Gets id.
     *
     * @return the id
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
