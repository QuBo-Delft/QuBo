package nl.tudelft.oopp.demo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.demo.sceneloader.SceneLoader;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

/**
 * Controller for the JoinQuBo.fxml
 */
public class JoinQuBoController {

    @FXML // fx:id="questionBoardCode"
    private TextField questionBoardCode;

    @FXML // fx:id="errorMessageLabel"
    private Label errorMessageLabel;

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

        QuestionBoardDetailsDto questionBoard = ServerCommunication.retrieveBoardDetails(boardCode);

        // We check whether the question board exists; If not show the error message label.
        if (questionBoard == null) {
            errorMessageLabel.setVisible(true);
        }

        // Get the stage that is currently open
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        // Load the student view.
        SceneLoader.loadStudentView(questionBoard, stage);

    }
}