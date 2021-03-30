package nl.tudelft.oopp.qubo.dtos.poll;

import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionDetailsDto;

import java.util.Set;
import java.util.UUID;

public class PollDetailsDto {
    private UUID id;

    private String text;

    private boolean open;

    private Set<PollOptionDetailsDto> options;

    public PollDetailsDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Set<PollOptionDetailsDto> getOptions() {
        return options;
    }

    public void setOptions(Set<PollOptionDetailsDto> options) {
        this.options = options;
    }
}
