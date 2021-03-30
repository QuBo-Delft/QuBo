package nl.tudelft.oopp.qubo.dtos.pacevote;

import java.util.UUID;

public class PaceVoteDetailsDto {
    private UUID id;

    private PaceType type;

    public PaceVoteDetailsDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PaceType getPaceType() {
        return type;
    }

    public void setPaceType(PaceType type) {
        this.type = type;
    }

}
