package nl.tudelft.oopp.demo.bindingmodels;

import java.sql.Timestamp;
import nl.tudelft.oopp.demo.entities.QuestionBoard;

public class QuestionBoardBindingModel {

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
    public QuestionBoardBindingModel(String title, Timestamp startTime, Timestamp endTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public QuestionBoard reflect() {
        return new QuestionBoard(this.title, this.startTime, this.endTime);
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
