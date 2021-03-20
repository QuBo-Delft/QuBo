package nl.tudelft.oopp.demo.controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.views.AlertDialog;

import java.io.IOException;
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

        QuestionBoardCreationBindingModel board = new QuestionBoardCreationBindingModel(
            titleStr, startTime);

        // Send the request and retrieve the QuestionBoardCreationDto
        QuestionBoardCreationDto questionBoardDto = ServerCommunication.createBoardRequest(board);

        // Alert the user if the creation of the question board has failed
        if (questionBoardDto == null) {
            AlertDialog.display("Unsuccessful Request", 
                                "The question board could not be created, please try again");
            return;
        }

        // Load the page that displays student code and moderator code
        loadQuestionBoardCodes(questionBoardDto);

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
            AlertDialog.display("No Title Input", "Please enter a meaningful title for the lecture");
            return;
        }

        // Get the start date input in string format
        String date = startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Get the minutes input
        int minutes = minutesSpinner.getValue();
        // Get the minutes input in string format
        String minutesStr = minutes < 10 ? "0" + minutes : String.valueOf(minutes);

        // Get the hours input
        int hours = hoursSpinner.getValue();
        // Get the hours input in string format
        String hoursStr = hours < 10 ? "0" + minutes : String.valueOf(minutes);

        // Get start time from its string format
        Date startTime = null;
        try {
            startTime = dateFormat.parse(date + " " + hoursStr + "-" + minutesStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Check if the start time is after the current time or it is null
        if (startTime == null || !startTimeIsBeforeCurrentTime(startTime)) {
            AlertDialog.display("Invalid Start Time",
                    "The start time is before the current time, please try again");
            return;
        }

        sendAndProcessBoardCreationRequest(titleStr, startTime);

    }

            return;
        }

        // TODO: we need two datePickers in the fxml
    }

    /**
     * This method aims to load the page that displays the student code and moderator code.
     *
     * @param qd    The QuestionBoardCreationDto object to be transferred to the controller
     *              of QuestionBoardCodes.
     *
     */
    public void loadQuestionBoardCodes(QuestionBoardCreationDto qd) {
        // Create an FXMLLoader of QuestionBoardCodes.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuestionBoardCodes.fxml"));

        Parent root = null;
        try {
            root = (Parent) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the controller of QuestionBoardCodes
        BoardCodesController controller = loader.getController();

        // Transfer the data for QuestionBoardCodes
        controller.displayCodes(qd);

        Stage stage = new Stage();

        // Check if root is null
        if (root == null) {
            AlertDialog.display("", "Unable to display the codes");
            return;
        }

        // Display the scene
        stage.setScene(new Scene(root));
        stage.show();
    }

}
