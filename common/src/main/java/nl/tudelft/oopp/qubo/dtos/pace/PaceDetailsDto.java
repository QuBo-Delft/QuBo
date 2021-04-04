package nl.tudelft.oopp.qubo.dtos.pace;

/**
 * The Pace details DTO.
 */
public class PaceDetailsDto {
    private int justRightVotes;

    private int tooFastVotes;

    private int tooSlowVotes;

    /**
     * Instantiates a new Pace details DTO.
     */
    public PaceDetailsDto() {
    }

    /**
     * Instantiates a new PaceDetailsDto.
     *
     * @param justRightVotes The JUST_RIGHT votes.
     * @param tooFastVotes   The TOO_FAST votes.
     * @param tooSlowVotes   The TOO_SLOW votes.
     */
    public PaceDetailsDto(int justRightVotes, int tooFastVotes, int tooSlowVotes) {
        this.justRightVotes = justRightVotes;
        this.tooFastVotes = tooFastVotes;
        this.tooSlowVotes = tooSlowVotes;
    }

    /**
     * Gets just right votes.
     *
     * @return The just right votes.
     */
    public int getJustRightVotes() {
        return justRightVotes;
    }

    /**
     * Sets just right votes.
     *
     * @param justRightVotes The just right votes.
     */
    public void setJustRightVotes(int justRightVotes) {
        this.justRightVotes = justRightVotes;
    }

    /**
     * Gets too fast votes.
     *
     * @return The too fast votes.
     */
    public int getTooFastVotes() {
        return tooFastVotes;
    }

    /**
     * Sets too fast votes.
     *
     * @param tooFastVotes The too fast votes.
     */
    public void setTooFastVotes(int tooFastVotes) {
        this.tooFastVotes = tooFastVotes;
    }

    /**
     * Gets too slow votes.
     *
     * @return The too slow votes.
     */
    public int getTooSlowVotes() {
        return tooSlowVotes;
    }

    /**
     * Sets too slow votes.
     *
     * @param tooSlowVotes The too slow votes.
     */
    public void setTooSlowVotes(int tooSlowVotes) {
        this.tooSlowVotes = tooSlowVotes;
    }
}
