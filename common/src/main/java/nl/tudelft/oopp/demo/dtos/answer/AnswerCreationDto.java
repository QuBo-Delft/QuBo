package nl.tudelft.oopp.demo.dtos.answer;

import java.sql.Timestamp;
import java.util.UUID;

public class AnswerCreationDto {
    private UUID id;

    public AnswerCreationDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
