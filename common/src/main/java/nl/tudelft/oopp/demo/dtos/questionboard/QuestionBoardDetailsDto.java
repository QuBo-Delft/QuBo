package nl.tudelft.oopp.demo.dtos.questionboard;

import java.sql.Timestamp;
import java.util.UUID;

public class QuestionBoardDetailsDto {
    private UUID id;

    private String title;

    private Timestamp startTime;

    private boolean closed;

    public QuestionBoardDetailsDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
