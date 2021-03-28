package nl.tudelft.oopp.demo.dtos.polloption;

import java.util.UUID;

public class PollOptionCreationDto {
    private UUID id;

    public PollOptionCreationDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
