package nl.tudelft.oopp.qubo.dtos.question;

import java.util.UUID;

/**
 * The Question creation DTO.
 */
public class QuestionCreationDto {
    private UUID id;

    private UUID secretCode;

    /**
     * Instantiates a new Question creation DTO.
     */
    public QuestionCreationDto() {
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
     * Gets secret code.
     *
     * @return The secret code.
     */
    public UUID getSecretCode() {
        return secretCode;
    }

    /**
     * Sets secret code.
     *
     * @param secretCode The secret code.
     */
    public void setSecretCode(UUID secretCode) {
        this.secretCode = secretCode;
    }
}
