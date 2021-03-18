package nl.tudelft.oopp.demo.dtos.question;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class QuestionCreationBindingModel {

    @NotNull
    @Length(min = 8)
    private String text;

    public QuestionCreationBindingModel() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
