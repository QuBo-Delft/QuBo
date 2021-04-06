package nl.tudelft.oopp.qubo.dtos.questionboard;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * The Question board details DTO.
 */
public class QuestionBoardDetailsDto {
    private UUID id;

    private String title;

    private Timestamp startTime;

    private boolean closed;

    /**
     * Instantiates a new Question board details DTO.
     */
    public QuestionBoardDetailsDto() {
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
     * Gets title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets start time.
     *
     * @return The start time.
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime The start time.
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Is closed boolean.
     *
     * @return The boolean.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Sets closed.
     *
     * @param closed The closed.
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
