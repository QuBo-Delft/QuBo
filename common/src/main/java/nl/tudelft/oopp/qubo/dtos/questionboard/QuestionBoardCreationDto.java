package nl.tudelft.oopp.qubo.dtos.questionboard;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * The Question board creation DTO.
 */
public class QuestionBoardCreationDto {
    private UUID id;

    private UUID moderatorCode;

    private String title;

    private Timestamp startTime;

    /**
     * Instantiates a new Question board creation DTO.
     */
    public QuestionBoardCreationDto() {
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
     * Gets moderator code.
     *
     * @return The moderator code.
     */
    public UUID getModeratorCode() {
        return moderatorCode;
    }

    /**
     * Sets moderator code.
     *
     * @param moderatorCode The moderator code.
     */
    public void setModeratorCode(UUID moderatorCode) {
        this.moderatorCode = moderatorCode;
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
}
