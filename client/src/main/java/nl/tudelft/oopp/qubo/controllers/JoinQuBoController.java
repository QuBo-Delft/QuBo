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
import nl.tudelft.oopp.qubo.communication.ServerCommunication;
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

    /**
     * Method that handles mouse click interaction with the create question board button.
     *
     * @param event the mouse click event
     */
    @FXML
    void createButtonClicked(ActionEvent event) {
        // Get the stage that is currently displayed on-screen
        Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();

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

        // We check whether the user input is a UUID and show the error message label when this is not the case.
        try {
            boardCode = UUID.fromString(qbCode);
        } catch (IllegalArgumentException exception) {
            errorMessageLabel.setVisible(true);
            return;
        }

        String user = userName.getText();
        // Check if the user entered a user name.
        if (user.length() == 0) {
            errorMessageLabel.setText("Error: No username was entered. Please enter a username");
            errorMessageLabel.setVisible(true);
            return;
        }

        String resBody = ServerCommunication.retrieveBoardDetails(boardCode);

        // We check whether the question board exists; If not show the error message label.
        if (resBody == null) {
            errorMessageLabel.setVisible(true);
            return;
        }

        QuestionBoardDetailsDto questionBoard = gson.fromJson(resBody, QuestionBoardDetailsDto.class);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        //Load the student view if the code entered by the user was the board ID of the question board.
        //Load the moderator view if this is not the case.
        if (boardCode.equals(questionBoard.getId())) {
            new SceneLoader().viewLoader(questionBoard, stage, user, "StudentView", null);
        } else {
            new SceneLoader().viewLoader(questionBoard, stage, user, "ModeratorView", boardCode);
        }
    }

    /**
     * Method that handles text field input changes.
     */
    @FXML
    public void textHandler() {
        if (errorMessageLabel.isVisible() && !errorMessageLabel.getText()
                .equals("Error: No username was entered. Please enter a username")) {
            try {
                UUID boardCode = UUID.fromString(questionBoardCode.getText());
            } catch (IllegalArgumentException exception) {
                return;
            }
            errorMessageLabel.setVisible(false);
        } else if (errorMessageLabel.isVisible()) {
            errorMessageLabel.setVisible(userName.getText().length() > 0);
        }
    }
}