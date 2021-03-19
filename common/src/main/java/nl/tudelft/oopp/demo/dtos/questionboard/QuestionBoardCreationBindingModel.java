package nl.tudelft.oopp.demo.dtos.questionboard;

import java.sql.Timestamp;
import javax.validation.constraints.NotNull;

public class QuestionBoardCreationBindingModel {

    @NotNull
    private String title;

    @NotNull
    private Timestamp startTime;


    /**
     * Instantiates a new Question board binding model.
     *
     * @param title     The title.
     * @param startTime The start time.
     */
    public QuestionBoardCreationBindingModel(String title, Timestamp startTime) {
        this.title = title;
        this.startTime = startTime;
    }

    public QuestionBoardCreationBindingModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

}
