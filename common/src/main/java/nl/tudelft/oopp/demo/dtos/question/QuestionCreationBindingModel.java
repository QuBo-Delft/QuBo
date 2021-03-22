package nl.tudelft.oopp.demo.dtos.question;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class QuestionCreationBindingModel {

    @NotNull
    @Length(min = 8)
    private String text;

    @NotNull
    @Length(min = 1)
    private String authorName;

    public QuestionCreationBindingModel() {
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
}
