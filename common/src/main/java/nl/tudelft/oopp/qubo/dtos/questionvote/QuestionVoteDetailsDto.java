package nl.tudelft.oopp.qubo.dtos.questionvote;

import java.util.UUID;

public class QuestionVoteDetailsDto {
    private UUID id;

    public QuestionVoteDetailsDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

