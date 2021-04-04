package nl.tudelft.oopp.qubo.entities;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * The Poll vote entity.
 */
@Entity
@Table(name = "poll_votes")
public class PollVote {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "poll_option", nullable = false)
    private PollOption pollOption;

    /**
     * Instantiates a new Poll vote.
     *
     * @param pollOption The poll option.
     */
    public PollVote(PollOption pollOption) {
        this.pollOption = pollOption;
    }

    /**
     * Instantiates a new Poll vote.
     */
    public PollVote() {
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
     * Gets poll option.
     *
     * @return The poll option.
     */
    public PollOption getPollOption() {
        return pollOption;
    }

    /**
     * Sets poll option.
     *
     * @param pollOption The poll option.
     */
    public void setPollOption(PollOption pollOption) {
        this.pollOption = pollOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PollVote pollVote = (PollVote) o;
        return Objects.equals(id, pollVote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
