package nl.tudelft.oopp.demo.dtos.question;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import nl.tudelft.oopp.demo.dtos.answer.AnswerDetailsDto;

public class QuestionDetailsDto {
    private UUID id;

    private String text;

    private String authorName;

    private Timestamp timestamp;

    private boolean answered;

    private Set<AnswerDetailsDto> answers;

    public QuestionDetailsDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Set<AnswerDetailsDto> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<AnswerDetailsDto> answers) {
        this.answers = answers;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}
