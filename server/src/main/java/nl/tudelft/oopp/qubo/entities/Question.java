package nl.tudelft.oopp.qubo.entities;

import java.sql.Timestamp;
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
 * The Question entity.
 */
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

    @Column(name = "ip")
    private String ip;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Answer> answers;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true,
        fetch = FetchType.EAGER)
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

    /**
     * Instantiates a new Question.
     */
    public Question() {
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
     * Gets question board.
     *
     * @return The question board.
     */
    public QuestionBoard getQuestionBoard() {
        return questionBoard;
    }

    /**
     * Sets question board.
     *
     * @param questionBoard The question board.
     */
    public void setQuestionBoard(QuestionBoard questionBoard) {
        this.questionBoard = questionBoard;
    }

    /**
     * Gets secret code.
     *
     * @return The secret code.
     */
    public UUID getSecretCode() {
        return secretCode;
    }

    /**
     * Sets secret code.
     *
     * @param secretCode The secret code.
     */
    public void setSecretCode(UUID secretCode) {
        this.secretCode = secretCode;
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
     * Gets author name.
     *
     * @return The author name.
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Sets author name.
     *
     * @param authorName The author name.
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * Gets votes.
     *
     * @return The votes.
     */
    public Set<QuestionVote> getVotes() {
        return votes;
    }

    /**
     * Sets votes.
     *
     * @param votes The votes.
     */
    public void setVotes(Set<QuestionVote> votes) {
        this.votes = votes;
    }

    /**
     * Gets answers.
     *
     * @return The answers.
     */
    public Set<Answer> getAnswers() {
        return answers;
    }

    /**
     * Sets answers.
     *
     * @param answers The answers.
     */
    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    /**
     * Gets answered.
     *
     * @return The answered.
     */
    public Timestamp getAnswered() {
        return answered;
    }

    /**
     * Sets answered.
     *
     * @param answered The answered.
     */
    public void setAnswered(Timestamp answered) {
        this.answered = answered;
    }

    /**
     * Gets ip.
     *
     * @return The ip.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets ip.
     *
     * @param ip The ip.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return Objects.equals(id, question.id)
            && Objects.equals(text, question.text)
            && Objects.equals(authorName, question.authorName)
            && Objects.equals(secretCode, question.secretCode)
            && Objects.equals(timestamp, question.timestamp)
            && Objects.equals(answered, question.answered)
            && Objects.equals(ip, question.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, authorName, secretCode, timestamp, answered, ip);
    }
}
