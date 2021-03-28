package nl.tudelft.oopp.demo.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;
import java.util.UUID;

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

    @OneToMany(mappedBy = "pollOption", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<PollVote> votes;

    /**
     * Create a new PollOption instance.
     *
     * @param poll      The poll that this poll option is related to.
     * @param text      The text of the poll option.
     */
    public PollOption(Poll poll, String text) {
        this.poll = poll;
        this.text = text;
    }

    public PollOption() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<PollVote> getVotes() {
        return votes;
    }

    public void setVotes(Set<PollVote> votes) {
        this.votes = votes;
    }
}
