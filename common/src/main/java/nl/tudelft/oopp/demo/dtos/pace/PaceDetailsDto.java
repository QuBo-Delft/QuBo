package nl.tudelft.oopp.demo.dtos.pace;

public class PaceDetailsDto {
    private int justRightVotes;

    private int tooFastVotes;

    private int tooSlowVotes;

    public PaceDetailsDto() {
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
