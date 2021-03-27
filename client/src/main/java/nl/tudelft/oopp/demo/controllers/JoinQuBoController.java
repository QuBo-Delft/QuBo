package nl.tudelft.oopp.demo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.demo.sceneloader.SceneLoader;

import java.util.UUID;

/**
 * Controller for the JoinQuBo.fxml
 */
public class JoinQuBoController {

    @FXML
    private TextField questionBoardCode;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private Button joinBtn;
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
        SceneLoader.loadCreateQuBo(currentStage);
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
            new SceneLoader().loadStudentView(questionBoard, stage);
        } else {
            SceneLoader.loadModeratorView(questionBoard, stage);
        }
    }
}