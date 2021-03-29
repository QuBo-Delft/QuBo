package nl.tudelft.oopp.demo.controllers.helpers;

import java.util.List;
import java.util.UUID;

public class Question {
    private UUID questionId;
    private int upvoteNumber;
    private String questionBody;
    private String authorName;
    private List<String> answers;

    public UUID getQuestionId() {
        return questionId;
    }

    public int getUpvoteNumber() {
        return upvoteNumber;
    }

    public String getQuestionContent() {
        return questionBody;
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

    /**
     * Constructs a question object to be mapped onto a QuestionListCell.
     *
     * @param questionId        The ID of the question.
     * @param upvoteNumber      The number of upvotes.
     * @param questionBody      The body of the question.
     * @param authorName        The author of the question.
     * @param answers           The answer(s) to the question.
     */
    public Question(UUID questionId, int upvoteNumber, String questionBody, String authorName,
                    List<String> answers) {
        this.questionId = questionId;
        this.upvoteNumber = upvoteNumber;
        this.questionBody = questionBody;
        this.authorName = authorName;
        this.answers = answers;
    }
}
