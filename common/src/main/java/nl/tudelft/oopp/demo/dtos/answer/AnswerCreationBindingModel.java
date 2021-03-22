package nl.tudelft.oopp.demo.dtos.answer;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class AnswerCreationBindingModel {

    @NotNull
    private String text;

    public AnswerCreationBindingModel() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
