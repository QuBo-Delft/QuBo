package nl.tudelft.oopp.qubo.entities;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * The Poll option entity.
 */
@Entity
@Table(name = "poll_options")
public class PollOption {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @OneToMany(mappedBy = "pollOption", cascade = CascadeType.REMOVE, orphanRemoval = true,
        fetch = FetchType.EAGER)
    private Set<PollVote> votes;

    /**
     * Create a new PollOption instance.
     *
     * @param poll The poll that this poll option is related to.
     * @param text The text of the poll option.
     */
    public PollOption(Poll poll, String text) {
        this.poll = poll;
        this.text = text;
    }

    /**
     * Instantiates a new Poll option.
     */
    public PollOption() {
    }

    /**
     * Gets id.
     *
     * @return the id
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
     * Gets poll.
     *
     * @return the poll
     */
    public Poll getPoll() {
        return poll;
    }

    /**
     * Sets poll.
     *
     * @param poll The poll.
     */
    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    /**
     * Gets text.
     *
     * @return the text
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
     * Gets votes.
     *
     * @return the votes
     */
    public Set<PollVote> getVotes() {
        return votes;
    }

    /**
     * Sets votes.
     *
     * @param votes The votes.
     */
    public void setVotes(Set<PollVote> votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PollOption option = (PollOption) o;
        return Objects.equals(id, option.id) && Objects.equals(text, option.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}
