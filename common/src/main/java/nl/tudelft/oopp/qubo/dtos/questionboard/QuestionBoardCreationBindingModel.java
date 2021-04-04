package nl.tudelft.oopp.qubo.dtos.questionboard;

import java.sql.Timestamp;
import javax.validation.constraints.NotNull;

/**
 * The Question board creation binding model.
 */
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

    /**
     * Instantiates a new Question board creation binding model.
     */
    public QuestionBoardCreationBindingModel() {
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime The start time.
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

}
