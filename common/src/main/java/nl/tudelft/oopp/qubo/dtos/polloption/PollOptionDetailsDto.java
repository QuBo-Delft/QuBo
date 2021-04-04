package nl.tudelft.oopp.qubo.dtos.polloption;

import java.util.UUID;

/**
 * The Poll option details DTO.
 */
public class PollOptionDetailsDto {
    private UUID optionId;

    private String optionText;

    /**
     * Instantiates a new Poll option details DTO.
     */
    public PollOptionDetailsDto() {
    }

    /**
     * Gets option id.
     *
     * @return the option id
     */
    public UUID getOptionId() {
        return optionId;
    }

    /**
     * Sets option id.
     *
     * @param optionId The option id.
     */
    public void setOptionId(UUID optionId) {
        this.optionId = optionId;
    }

    /**
     * Gets option text.
     *
     * @return the option text
     */
    public String getOptionText() {
        return optionText;
    }

    /**
     * Sets option text.
     *
     * @param optionText The option text.
     */
    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
}
