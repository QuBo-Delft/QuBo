package nl.tudelft.oopp.qubo.dtos.poll;

import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionDetailsDto;

import java.util.Set;
import java.util.UUID;

/**
 * The Poll details DTO.
 */
public class PollDetailsDto {
    private UUID id;

    private String text;

    private boolean open;

    private Set<PollOptionDetailsDto> options;

    /**
     * Instantiates a new Poll details DTO.
     */
    public PollDetailsDto() {
    }

    /**
     * Gets id.
     *
     * @return The id.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id The id.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets text.
     *
     * @return The text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text.
     *
     * @param text The text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the open boolean.
     *
     * @return The the open boolean.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Sets the open boolean.
     *
     * @param open The the open boolean.
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * Gets options.
     *
     * @return The options.
     */
    public Set<PollOptionDetailsDto> getOptions() {
        return options;
    }

    /**
     * Sets options.
     *
     * @param options The options.
     */
    public void setOptions(Set<PollOptionDetailsDto> options) {
        this.options = options;
    }
}
