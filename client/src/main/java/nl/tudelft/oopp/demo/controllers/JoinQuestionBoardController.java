package nl.tudelft.oopp.demo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

/**
 * Controller for the JoinQuestionBoard.fxml
 */
public class JoinQuestionBoardController {

    @FXML // fx:id="questionBoardCode"
    private TextField questionBoardCode;

    @FXML // fx:id="errorMessageLabel"
    private Label errorMessageLabel;

    /**
     * Method that handles mouse click interaction with the create question board button.
     *
     * @param event the mouse click event
     * @throws IOException the io exception
     */
    @FXML
    void createButtonClicked(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/CreateQuestionBoardWindow.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
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
        // TODO: joinQuestionBoard method
    }
}