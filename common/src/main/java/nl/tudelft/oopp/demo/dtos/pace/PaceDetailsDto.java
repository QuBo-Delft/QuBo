package nl.tudelft.oopp.demo.dtos.pace;

public class PaceDetailsDto {
    private int justRightVotes;

    private int tooFastVotes;

    private int tooSlowVotes;

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

    public int getJustRightVotes() {
        return justRightVotes;
    }

    public void setJustRightVotes(int justRightVotes) {
        this.justRightVotes = justRightVotes;
    }

    public int getTooFastVotes() {
        return tooFastVotes;
    }

    public void setTooFastVotes(int tooFastVotes) {
        this.tooFastVotes = tooFastVotes;
    }

    public int getTooSlowVotes() {
        return tooSlowVotes;
    }

    public void setTooSlowVotes(int tooSlowVotes) {
        this.tooSlowVotes = tooSlowVotes;
    }
}
