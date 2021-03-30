package nl.tudelft.oopp.qubo.dtos.answer;

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
