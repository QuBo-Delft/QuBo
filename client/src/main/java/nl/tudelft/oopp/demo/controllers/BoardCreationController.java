package nl.tudelft.oopp.demo.controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.views.AlertDialog;

import java.net.http.HttpResponse;
import java.util.Date;
import java.sql.Timestamp;

public class BoardCreationController {

    @FXML
    private TextField title = null;
    @FXML
    private DatePicker startDate = null;
    @FXML
    private DatePicker endDate = null;

    /**
     * Once the user clicked the "create now" button, this method will be invoked to request to create
     * a question board (open immediately) on the server. If the request was unsuccessful, pop up an 
     * alert dialog.
     *
     * @param actionEvent   The click button event.
     */
    public void createNowBtnClicked(ActionEvent actionEvent) {
        String titleStr = title.getText();
        // Check if title is empty
        if (titleStr.length() == 0) {
            AlertDialog.display("No title", 
                                "Please enter a meaningful title for the lecture");
            return;
        }

        Date currentDate = new Date();
        Timestamp startTime = new Timestamp(currentDate.getTime());

        // End time needs to be reconsidered
        Timestamp endTime = startTime;

        QuestionBoardCreationBindingModel board = new QuestionBoardCreationBindingModel(
            titleStr, startTime, endTime);

        HttpResponse<String> res = ServerCommunication.createBoardRequest(board);
        if (res == null) {
            AlertDialog.display("Unsuccessful request", 
                                "Sorry, the request has failed, please try again");
            return;
        }

        Gson gson = new Gson();
        QuestionBoardCreationDto qb = gson.fromJson(res.body(), QuestionBoardCreationDto.class);

        // TODO: Continue to load the window that gives codes

    }


    /**
     * Once the user clicked the "schedule" button, this method will be invoked to request to create
     * a question board (open and close as scheduled) on the server. If the request was unsuccessful, 
     * pop up an alert dialog.
     *
     * @param actionEvent   The click button event.
     */
    public void scheduleBtnClicked(ActionEvent actionEvent) {
        String titleStr = title.getText();
        // Check if title is empty
        if (titleStr.length() == 0) {
            AlertDialog.display("No title", "Please enter a meaningful title for the lecture");
            return;
        }
        // TODO: we need two datePickers in the fxml
    }
}
