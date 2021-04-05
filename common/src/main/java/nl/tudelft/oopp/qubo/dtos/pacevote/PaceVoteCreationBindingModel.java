package nl.tudelft.oopp.qubo.dtos.pacevote;

import javax.validation.constraints.NotNull;

/**
 * The Pace vote creation binding model.
 */
public class PaceVoteCreationBindingModel {

    @NotNull
    private PaceType paceType;

    /**
     * Instantiates a new Pace vote creation binding model.
     */
    public PaceVoteCreationBindingModel() {
    }

    /**
     * Gets pace type.
     *
     * @return The pace type.
     */
    public PaceType getPaceType() {
        return paceType;
    }

    /**
     * Sets pace type.
     *
     * @param paceType The pace type.
     */
    public void setPaceType(PaceType paceType) {
        this.paceType = paceType;
    }
}
