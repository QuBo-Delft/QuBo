package nl.tudelft.oopp.qubo.dtos.answer;

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
