package nl.tudelft.oopp.demo.dtos.questionboard;

import java.sql.Timestamp;

public class QuestionBoardCreationBindingModel {

    private String title;
    private Timestamp startTime;
    private Timestamp endTime;


    /**
     * Instantiates a new Question board binding model.
     *
     * @param title     The title.
     * @param startTime The start time.
     * @param endTime   The end time.
     */
    public QuestionBoardCreationBindingModel(String title, Timestamp startTime, Timestamp endTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
