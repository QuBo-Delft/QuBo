package nl.tudelft.oopp.qubo.dtos.question;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class QuestionEditingBindingModel {

    @NotNull
    @Length(min = 8)
    private String text;

    public QuestionEditingBindingModel() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
