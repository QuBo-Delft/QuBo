package nl.tudelft.oopp.qubo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.communication.QuestionBoardCommunication;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;

import java.util.UUID;

/**
 * Controller for the JoinQuBo.fxml
 */
public class JoinQuBoController {
    // Input field for the question board code
    @FXML
    private TextField questionBoardCode;
    // Input field for the user name
    @FXML
    private TextField userName;
    // Error message shown on incorrect question board code entered or empty username
    @FXML
    private Label errorMessageLabel;
    // Button to be clicked when wanting to join a question board
    @FXML
    private Button joinBtn;
    // Button to be clicked when wanting to create a question board
    @FXML
    private Button createBtn;

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    private static final String quBoError = "Error: Could not find the requested question board!\nPlease "
            + "check if you inserted the code correctly!";
    private static final String userNameError = "Error: No username was entered. Please enter a username";

    /**
     * Method that handles mouse click interaction with the create question board button.
     *
     * @param event the mouse click event
     */
    @FXML
    void createButtonClicked(ActionEvent event) {
        // Get the stage that is currently displayed on-screen
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        //Load the create question board page
        SceneLoader.defaultLoader(currentStage, "CreateQuBo");
    }

    /**
     * Method that handles mouse click interaction with the join question board button.
     *
     * @param event the mouse click event
     */
    @FXML
    void joinButtonClicked(ActionEvent event) {

        String qbCode = questionBoardCode.getText();
        UUID boardCode;

        // We check whether the user input is a UUID and show the corresponding error when this is not the case.
        try {
            boardCode = UUID.fromString(qbCode);
        } catch (IllegalArgumentException exception) {
            setErrorMessageLabel(true);
            return;
        }

        String user = userName.getText();
        // Check if the user entered a user name and show the corresponding error when this is not the case.
        if (user.length() == 0) {
            setErrorMessageLabel(false);
            return;
        }

        String resBody = QuestionBoardCommunication.retrieveBoardDetails(boardCode);

        // We check whether the question board exists; If not show the corresponding error message.
        if (resBody == null) {
            setErrorMessageLabel(true);
            return;
        }

        QuestionBoardDetailsDto questionBoard = gson.fromJson(resBody, QuestionBoardDetailsDto.class);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        //Load the student view if the code entered by the user was the board ID of the question board.
        //Load the moderator view if this is not the case.
        if (boardCode.equals(questionBoard.getId())) {
            new SceneLoader().viewLoader(questionBoard, stage, user, "StudentView", null);
        } else {
            new SceneLoader().viewLoader(questionBoard, stage, user, "ModeratorView", boardCode);
        }
    }

    /**
     * Method that handles text field input changes of the text field where a user types their name.
     */
    @FXML
    public void userTextHandler() {
        String userNameText = userName.getText();

        //Do not change the error message if it is displaying the question board error message as this
        //should only be changed if the question board exists, which is checked when the join button is
        //pressed.
        if (errorMessageLabel.getText().equals(quBoError) && errorMessageLabel.isVisible()) {
            return;
        //Set the error message to the user name error if no username was entered
        } else if (userNameText.equals("")) {
            setErrorMessageLabel(false);
        //If the user name error was visible when this method was called, a character must have been typed
        //in the user name text field. Set the visibility of the error to false.
        } else if (errorMessageLabel.getText().equals(userNameError)) {
            errorMessageLabel.setVisible(false);
        }
    }

    /**
     * This method sets the error message label's text to the quBoError if the passed boolean is true and sets
     * it to the userNameError otherwise. It also sets its visibility to true.
     *
     * @param isQuBoMessage The boolean that decides what error message is shown.
     */
    private void setErrorMessageLabel(boolean isQuBoMessage) {
        if (isQuBoMessage) {
            errorMessageLabel.setText(quBoError);
        } else {
            errorMessageLabel.setText(userNameError);
        }

        errorMessageLabel.setVisible(true);
    }
}