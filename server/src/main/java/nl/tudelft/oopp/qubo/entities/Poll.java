package nl.tudelft.oopp.qubo.entities;

import java.util.Objects;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "polls")
public class Poll {
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

    @Column(name = "open", nullable = false)
    private boolean open;

    @OneToOne
    @JoinColumn(name = "board_id", nullable = false)
    private QuestionBoard questionBoard;

    @OneToMany(mappedBy = "poll", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<PollOption> pollOptions;

    /**
     * Create a new instance of an open Poll.
     *
     * @param text          The question associated with the poll.
     * @param questionBoard The question board in which the poll was created.
     * @param pollOptions   The answer options of the poll.
     */
    public Poll(String text, QuestionBoard questionBoard, Set<PollOption> pollOptions) {
        this.text = text;
        this.questionBoard = questionBoard;
        this.pollOptions = pollOptions;
        this.open = true;
    }

    public Poll() {
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

    public QuestionBoard getQuestionBoard() {
        return questionBoard;
    }

    public void setQuestionBoard(QuestionBoard questionBoard) {
        this.questionBoard = questionBoard;
    }

    public Set<PollOption> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(Set<PollOption> pollOptions) {
        this.pollOptions = pollOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Poll poll = (Poll) o;
        return open == poll.open
            && Objects.equals(id, poll.id)
            && Objects.equals(text, poll.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, open);
    }
}
