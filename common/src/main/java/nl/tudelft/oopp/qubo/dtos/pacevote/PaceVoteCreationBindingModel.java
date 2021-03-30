package nl.tudelft.oopp.qubo.dtos.pacevote;

import javax.validation.constraints.NotNull;

public class PaceVoteCreationBindingModel {

    @NotNull
    private PaceType paceType;

    public PaceVoteCreationBindingModel() {
    }

    public PaceType getPaceType() {
        return paceType;
    }

    public void setPaceType(PaceType paceType) {
        this.paceType = paceType;
    }
}
