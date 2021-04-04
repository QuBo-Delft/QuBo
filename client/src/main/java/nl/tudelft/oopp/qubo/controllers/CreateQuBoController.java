package nl.tudelft.oopp.qubo.controllers;

import javafx.event.ActionEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import nl.tudelft.oopp.qubo.communication.QuestionBoardCommunication;
import nl.tudelft.oopp.qubo.communication.ServerCommunication;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import nl.tudelft.oopp.qubo.views.AlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;

/**
 * The controller for CreateQuBo.fxml
 */
public class CreateQuBoController {

    @FXML
    private TextField title;
    @FXML
    private DatePicker startDate;
    @FXML
    private Spinner<Integer> hoursSpinner;
    @FXML
    private Spinner<Integer> minutesSpinner;
    @FXML
    private Label errorDateTime;
    @FXML
    private Label errorTitle;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button scheduleBtn;
    @FXML
    private Button createBtn;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    /**
     * Code that is run upon loading StudentView.fxml.
     * Sources: https://stackoverflow.com/questions/32346893/javafx-datepicker-not-updating-value,
     * https://stackoverflow.com/questions/42385584/changevalue-listener-spinner-javafx.
     */
    @FXML
    private void initialize() {
        //Change the value of the date picker when the user types.
        startDate.getEditor().textProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                startDate.setValue(startDate.getConverter().fromString(startDate.getEditor().getText()));
            } catch (Exception e) {
                errorDateTime.setVisible(true);
            }
        }));

        //Change the value of the hours spinner when the user types.
        hoursSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) ->
            updateSpinner(hoursSpinner, newValue));

        //Change the value of the minutes spinner when the focus changes.
        minutesSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) ->
                updateSpinner(minutesSpinner, newValue));
    }

    /**
     * Once the user clicks the "create now" button, this method will be invoked
     * to create a question board that opens immediately on the server.
     * If the request was unsuccessful, an alert dialog is shown to the user.
     */
    public void createNowBtnClicked(ActionEvent actionEvent) {
        String titleStr = title.getText();

        // Check if the title is not empty
        if (!titleIsEmpty(titleStr)) {
            sendAndProcessBoardCreationRequest(titleStr, new Date(), actionEvent);
        }
    }

    /**
     * Once the user clicks the "schedule" button, this method will be invoked to create
     * a question board, that opens and closes as scheduled, on the server.
     * If the request was unsuccessful, an alert dialog is shown to the user.
     */
    public void scheduleBtnClicked(ActionEvent actionEvent) {
        String titleStr = title.getText();

        // Check if title is empty
        boolean titleEmpty = titleIsEmpty(titleStr);

        // check if date is not null
        boolean invalidDate = (startDate.getValue() == null);
        if (invalidDate) {
            errorDateTime.setVisible(true);
        }

        // if either the title is empty, or the date is null, stop code execution
        if (titleEmpty || invalidDate) {
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
            errorDateTime.setVisible(true);
            return;
        }

        sendAndProcessBoardCreationRequest(titleStr, startTime, actionEvent);

    }

    /**
     * When the user clicks the "cancel" button this method loads the home screen scene.
     */
    public void cancelBtnClicked() {
        new SceneLoader().defaultLoader((Stage) cancelBtn.getScene().getWindow(),
            "JoinQuBo");
    }

    /**
     * This method aims to send and process a question board creation request.
     *
     * @param titleStr      The title of the question board.
     * @param startTime     The start time of the question board.
     */
    private void sendAndProcessBoardCreationRequest(String titleStr, Date startTime, ActionEvent event) {
        Timestamp startTimeStamp = new Timestamp(startTime.getTime());
        QuestionBoardCreationBindingModel board = new QuestionBoardCreationBindingModel(
            titleStr, startTimeStamp);

        // Send the request and retrieve the string body of QuestionBoardCreationDto
        String resBody = QuestionBoardCommunication.createBoardRequest(board);

        // Alert the user if the creation of the question board has failed
        if (resBody == null) {
            AlertDialog.display("Unsuccessful Request",
                "The question board could not be created, please try again");
            return;
        }

        // Convert the response body to QuestionBoardCreationDto
        QuestionBoardCreationDto questionBoardDto = gson.fromJson(resBody, QuestionBoardCreationDto.class);

        // Load the page that displays student code and moderator code
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        new SceneLoader().loadQuBoCodes(questionBoardDto, stage);
    }

    /**
     * This method aims to check whether the start time is after the current time.
     *
     * @param startTime     The start time of the question board.
     * @return true if and only the start time is after the current time.
     */
    public static boolean isStartTimeBeforeCurrentTime(Date startTime) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(startTime);
        c2.setTime(new Date());
        return c1.compareTo(c2) >= 0;
    }

    /**
     * This method checks whether the title TextField is empty and if it is,
     * displays an error accordingly.
     * @param     titleStr the title String to be checked.
     * @return returns true if empty.
     */
    public boolean titleIsEmpty(String titleStr) {
        if (titleStr.isEmpty()) {
            errorTitle.setVisible(true);
            return true;
        }
        return false;
    }

    /**
     * This method checks whether the title TextField is empty when it's input is changed
     * or when it is clicked. If it is empty it will display the errorTitle label, if it is
     * not empty, but the label was being displayed, it will get hidden again.
     */
    public void titleTextHandler() {
        if (errorTitle.isVisible()) {
            errorTitle.setVisible(title.getText().length() == 0);
        }
    }

    /**
     * This method updates spinner values when the user types in the corresponding text field.
     * Source: https://stackoverflow.com/questions/42385584/changevalue-listener-spinner-javafx.
     *
     * @param spinner   The spinner whose value should be updated.
     * @param value     The value that the spinner should be updated to.
     */
    public void updateSpinner(Spinner<Integer> spinner, String value) {
        try {
            //If the spinner's editor is empty, do not change anything.
            if ("".equals(value)) {
                return;
            }

            SpinnerValueFactory<Integer> factory = spinner.getValueFactory();
            StringConverter<Integer> converter = factory.getConverter();

            Integer hour = converter.fromString(value);

            //Update the spinner's value
            factory.setValue(hour);
            dateInputHandler();
        } catch (Exception e) {
            errorDateTime.setVisible(true);
        }
    }

    /**
     * This method first checks whether the enter date and time is after the current date and time.
     * It also hides or shows an error Label called errorDateTime accordingly.
     */
    public void dateInputHandler() {
        if (startDate.getValue() == null) {
            errorDateTime.setVisible(true);
        } else {
            // Create a new variable with the current date and time
            LocalDateTime today = LocalDateTime.now();
            // Retrieve the entered date
            LocalDate inputDay = startDate.getValue();
            // Add the hour and time value from the Spinners to the entered date
            LocalDateTime inputTime = inputDay.atTime(hoursSpinner.getValue(), minutesSpinner.getValue());
            // Compare both dates
            errorDateTime.setVisible(!inputTime.isAfter(today));
        }
    }

}
