package nl.tudelft.oopp.qubo.dtos.questionvote;

import java.util.UUID;

/**
 * The Question vote creation DTO.
 */
public class QuestionVoteCreationDto {
    private UUID id;

    /**
     * Instantiates a new Question vote creation DTO.
     */
    public QuestionVoteCreationDto() {
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
