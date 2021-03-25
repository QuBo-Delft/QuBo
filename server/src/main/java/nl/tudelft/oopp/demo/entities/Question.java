package nl.tudelft.oopp.demo.entities;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "secret_code", nullable = false)
    private UUID secretCode;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "answered")
    private Timestamp answered;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Answer> answers;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<QuestionVote> votes;


    /**
     * Create a new Question instance.
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
     * Create a new Question instance and generate a secretCode automatically.
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Set<QuestionVote> getVotes() {
        return votes;
    }

    public void setVotes(Set<QuestionVote> votes) {
        this.votes = votes;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Timestamp getAnswered() {
        return answered;
    }

    public void setAnswered(Timestamp answered) {
        this.answered = answered;
    }
}
