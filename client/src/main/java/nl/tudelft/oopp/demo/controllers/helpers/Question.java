package nl.tudelft.oopp.demo.controllers.helpers;

import java.util.List;
import java.util.UUID;

public class Question {
    private UUID questionId;
    private int upvoteNumber;
    private String questionContent;
    private String authorName;
    private List<String> answers;

    public UUID getQuestionId() {
        return questionId;
    }

    public int getUpvoteNumber() {
        return upvoteNumber;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public String getAuthorName() {
        return authorName;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public Question(UUID questionId, int upvoteNumber, String questionContent, String authorName,
                    List<String> answers) {
        this.questionId = questionId;
        this.upvoteNumber = upvoteNumber;
        this.questionContent = questionContent;
        this.authorName = authorName;
        this.answers = answers;
    }
}
