package nl.tudelft.oopp.qubo.dtos.questionvote;

import java.util.UUID;

public class QuestionVoteCreationDto {
    private UUID id;

    public QuestionVoteCreationDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
