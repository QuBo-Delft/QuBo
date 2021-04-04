package nl.tudelft.oopp.qubo.entities;

import java.sql.Timestamp;
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
 * The Answer entity.
 */
@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "text", nullable = true)
    private String text;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    /**
     * Create a new Answer instance.
     *
     * @param question  The question this answer is related to.
     * @param text      The text of the answer.
     * @param timestamp The creation time of the answer.
     */
    public Answer(Question question, String text, Timestamp timestamp) {
        this.question = question;
        this.text = text;
        this.timestamp = timestamp;
    }

    /**
     * Instantiates a new Answer.
     */
    public Answer() {
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
     * Gets timestamp.
     *
     * @return The timestamp.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp The timestamp.
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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
     * Gets question.
     *
     * @return The question.
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Sets question.
     *
     * @param question The question.
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id)
            && Objects.equals(text, answer.text)
            && Objects.equals(timestamp, answer.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, timestamp);
    }
}
