package nl.tudelft.oopp.qubo.dtos.questionboard;

import java.sql.Timestamp;
import java.util.UUID;

public class QuestionBoardCreationDto {
    private UUID id;

    private UUID moderatorCode;

    private String title;

    private Timestamp startTime;

    public QuestionBoardCreationDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getModeratorCode() {
        return moderatorCode;
    }

    public void setModeratorCode(UUID moderatorCode) {
        this.moderatorCode = moderatorCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
}
