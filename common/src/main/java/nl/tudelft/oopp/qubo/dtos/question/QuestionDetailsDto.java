package nl.tudelft.oopp.qubo.dtos.question;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;

/**
 * The Question details DTO.
 */
public class QuestionDetailsDto {
    private UUID id;

    private String text;

    private String authorName;

    private Timestamp timestamp;

    private Timestamp answered;

    private int upvotes;

    private Set<AnswerDetailsDto> answers;

    /**
     * Instantiates a new Question details DTO.
     */
    public QuestionDetailsDto() {
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
     * Gets timestamp.
     *
     * @return the timestamp
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
     * Gets answers.
     *
     * @return the answers
     */
    public Set<AnswerDetailsDto> getAnswers() {
        return answers;
    }

    /**
     * Sets answers.
     *
     * @param answers The answers.
     */
    public void setAnswers(Set<AnswerDetailsDto> answers) {
        this.answers = answers;
    }

    /**
     * Gets author name.
     *
     * @return the author name
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
     * Gets answered.
     *
     * @return the answered
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
     * Gets upvotes.
     *
     * @return the upvotes
     */
    public int getUpvotes() {
        return upvotes;
    }

    /**
     * Sets upvotes.
     *
     * @param upvotes The upvotes.
     */
    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }
}
