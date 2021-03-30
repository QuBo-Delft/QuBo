package nl.tudelft.oopp.qubo.dtos.answer;

import java.sql.Timestamp;
import java.util.UUID;

public class AnswerDetailsDto {
    private UUID id;

    private String text;

    private Timestamp timestamp;

    public AnswerDetailsDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
