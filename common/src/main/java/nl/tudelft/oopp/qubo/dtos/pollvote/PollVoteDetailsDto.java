package nl.tudelft.oopp.qubo.dtos.pollvote;

import java.util.UUID;

public class PollVoteDetailsDto {
    private UUID id;

    public PollVoteDetailsDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
