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
 * The Question vote entity.
 */
@Entity
@Table(name = "question_votes")
public class QuestionVote {
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

    /**
     * Instantiates a new Question vote.
     *
     * @param question The question.
     */
    public QuestionVote(Question question) {
        this.question = question;
    }

    /**
     * Instantiates a new Question vote.
     */
    public QuestionVote() {
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
     * Gets question.
     *
     * @return the question
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
        QuestionVote vote = (QuestionVote) o;
        return Objects.equals(id, vote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
