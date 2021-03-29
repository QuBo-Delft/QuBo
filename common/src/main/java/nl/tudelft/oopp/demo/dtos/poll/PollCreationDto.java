package nl.tudelft.oopp.demo.dtos.poll;

import java.util.UUID;

public class PollCreationDto {
    private UUID pollId;

    public PollCreationDto() {
    }

    public UUID getPollId() {
        return this.pollId;
    }

    public void setPollId(UUID pollId) {
        this.pollId = pollId;
    }
}
