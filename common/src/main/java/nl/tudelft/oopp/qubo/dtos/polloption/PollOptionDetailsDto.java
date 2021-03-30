package nl.tudelft.oopp.qubo.dtos.polloption;

import java.util.UUID;

public class PollOptionDetailsDto {
    private UUID optionId;

    private String optionText;

    public PollOptionDetailsDto() {
    }

    public UUID getOptionId() {
        return optionId;
    }

    public void setOptionId(UUID optionId) {
        this.optionId = optionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
}
