package nl.tudelft.oopp.qubo.dtos.pollvote;

import java.util.UUID;

public class PollVoteCreationDto {
    private UUID id;

    public PollVoteCreationDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
