package nl.tudelft.oopp.demo.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.sceneloader.SceneLoader;
import nl.tudelft.oopp.demo.views.AlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;

public class CreateQuBoController {

    @FXML
    private TextField title;
    @FXML
    private DatePicker startDate;
    @FXML
    private Spinner<Integer> hoursSpinner;
    @FXML
    private Spinner<Integer> minutesSpinner;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");

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
            AlertDialog.display("No Title Input",
                                "Please enter a meaningful title for the lecture");
            return;
        }

        sendAndProcessBoardCreationRequest(titleStr, new Date(), actionEvent);

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
        String hoursStr = hours < 10 ? "0" + hours : String.valueOf(hours);

        // Get start time from its string format
        Date startTime = null;
        try {
            startTime = dateFormat.parse(date + " " + hoursStr + "-" + minutesStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Check if the start time is after the current time or is null
        if (startTime == null || !isStartTimeBeforeCurrentTime(startTime)) {
            AlertDialog.display("Invalid Start Time",
                    "The start time is before the current time, please try again");
            return;
        }

        sendAndProcessBoardCreationRequest(titleStr, startTime, actionEvent);

    }

    /**
     * This method aims to send and process a question board creation request.
     *
     * @param titleStr      The title of the question board.
     * @param startTime     The start time of the question board.
     */
    private void sendAndProcessBoardCreationRequest(String titleStr, Date startTime, Event event) {
        Timestamp startTimeStamp = new Timestamp(startTime.getTime());
        QuestionBoardCreationBindingModel board = new QuestionBoardCreationBindingModel(
                titleStr, startTimeStamp);

        // Send the request and retrieve the QuestionBoardCreationDto
        QuestionBoardCreationDto questionBoardDto = ServerCommunication.createBoardRequest(board);

        // Alert the user if the creation of the question board has failed
        if (questionBoardDto == null) {
            AlertDialog.display("Unsuccessful Request",
                    "The question board could not be created, please try again");
            return;
        }

        // Get the stage that is currently open
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        // Load the page that displays student code and moderator code
        SceneLoader.loadQuestionBoardCodes(questionBoardDto, stage);
    }

    /**
     * This method aims to check whether the start time is after the current time.
     *
     * @param startTime     The start time of the question board.
     * @return true if and only the start time is after the current time.
     */
    private boolean isStartTimeBeforeCurrentTime(Date startTime) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(startTime);
        c2.setTime(new Date());
        return c1.compareTo(c2) >= 0;
    }

}
