package nl.tudelft.oopp.demo.entities;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private QuestionBoard questionBoard;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "secret_code", nullable = false)
    private UUID secretCode;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    /**
     * Create a new QuestionBoard instance.
     *
     * @param questionBoard The question board where this question is asked.
     * @param text          The text of the question.
     * @param secretCode    A secret code to be used to authenticate the question's owner.
     * @param timestamp     The creation time of the question.
     */
    public Question(QuestionBoard questionBoard, String text,
                    UUID secretCode, Timestamp timestamp) {
        this.questionBoard = questionBoard;
        this.text = text;
        this.secretCode = secretCode;
        this.timestamp = timestamp;
    }

    /**
     * Create a new QuestionBoard instance and generate a secretCode automatically.
     *
     * @param questionBoard The question board where this question is asked.
     * @param text          The text of the question.
     * @param timestamp     The creation time of the question.
     */
    public Question(QuestionBoard questionBoard, String text, Timestamp timestamp) {
        this(questionBoard, text, UUID.randomUUID(), timestamp);
    }

    public Question() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public QuestionBoard getQuestionBoard() {
        return questionBoard;
    }

    public void setQuestionBoard(QuestionBoard questionBoard) {
        this.questionBoard = questionBoard;
    }

    public UUID getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(UUID secretCode) {
        this.secretCode = secretCode;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
