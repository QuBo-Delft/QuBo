package nl.tudelft.oopp.qubo.dtos.answer;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * The Answer details DTO.
 */
public class AnswerDetailsDto {
    private UUID id;

    private String text;

    private Timestamp timestamp;

    /**
     * Instantiates a new Answer details DTO.
     */
    public AnswerDetailsDto() {
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
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp The timestamp.
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets text.
     *
     * @return the text
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
}
