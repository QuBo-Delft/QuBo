package nl.tudelft.oopp.qubo.dtos.pacevote;

import java.util.UUID;

/**
 * The Pace vote details DTO.
 */
public class PaceVoteDetailsDto {
    private UUID id;

    private PaceType type;

    /**
     * Instantiates a new Pace vote details DTO.
     */
    public PaceVoteDetailsDto() {
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

    /**
     * Gets pace type.
     *
     * @return the pace type
     */
    public PaceType getPaceType() {
        return type;
    }

    /**
     * Sets pace type.
     *
     * @param type The type.
     */
    public void setPaceType(PaceType type) {
        this.type = type;
    }

}
