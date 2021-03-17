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

public class QuestionBoardCreationController {

    @FXML
    private TextField title = null;
    @FXML
    private DatePicker startDate = null;
    @FXML
    private DatePicker endDate = null;

    /**
     * Once the user clicks the "create now" button, this method will be invoked 
     * to create a question board that opens immediately on the server. 
     * If the request was unsuccessful, an alert dialog is shown to the user
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

        //Send the request and retrieve the response
        HttpResponse<String> res = ServerCommunication.createBoardRequest(board);

        //Alert the user if the creation of the question board has failed
        if (res == null) {
            AlertDialog.display("Unsuccessful Request", 
                                "The question board could not be created, please try again");
            return;
        }

        //Convert the response object to a QuestionBoardCreationDto
        Gson gson = new Gson();
        QuestionBoardCreationDto qb = gson.fromJson(res.body(), QuestionBoardCreationDto.class);

        // TODO: Continue to load the window that gives codes

    }


    /**
     * Once the user clicks the "schedule" button, this method will be invoked to create
     * a question board, that opens and closes as scheduled, on the server. 
     * If the request was unsuccessful, an alert dialog is shown to the user.
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
