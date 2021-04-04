package nl.tudelft.oopp.qubo.dtos.questionvote;

import java.util.UUID;

/**
 * The Question vote details DTO.
 */
public class QuestionVoteDetailsDto {
    private UUID id;

    /**
     * Instantiates a new Question vote details DTO.
     */
    public QuestionVoteDetailsDto() {
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

