package nl.tudelft.oopp.qubo.dtos.answer;

import java.util.UUID;

/**
 * The Answer creation DTO.
 */
public class AnswerCreationDto {
    private UUID id;

    /**
     * Instantiates a new Answer creation DTO.
     */
    public AnswerCreationDto() {
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
