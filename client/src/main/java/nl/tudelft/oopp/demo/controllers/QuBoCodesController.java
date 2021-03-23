package nl.tudelft.oopp.demo.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.sceneloader.SceneLoader;

import java.io.IOException;
import java.net.URL;

public class QuBoCodesController {

    @FXML
    private Label boardTitle;
    @FXML
    private Label startTime;
    @FXML
    private Label adminCode;
    @FXML
    private Label studentCode;
    @FXML
    private Label adminCopySuccessful;
    @FXML
    private Label studentCopySuccessful;
    @FXML
    private Button backToHome;

    private final PauseTransition pause = new PauseTransition(Duration.seconds(1));
    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent content = new ClipboardContent();


    /**
     * This method aims to make a window to display the student code, the moderator code, 
     * the title, and start time of the question board just created.
     *
     * @param qb    The QuestionBoardCreationDto that contains the data of a question board.
     */
    public void displayCodes(QuestionBoardCreationDto qb) {
        startTime.setText(qb.getStartTime().toString());
        boardTitle.setText(qb.getTitle());
        adminCode.setText(qb.getModeratorCode().toString());
        studentCode.setText(qb.getId().toString());
    }

    /**
     * This method aims to copy the admin code to the user's system clipboard
     * after clicking the button for copying the admin code.
     * Displays a message after successfully copying the code.
     */
    public void copyAdminCode() {
        content.putString(adminCode.getText());
        clipboard.setContent(content);

        //Shows adminCopySuccessful label for 1 second and hides it again
        if (!adminCopySuccessful.isVisible()) {
            adminCopySuccessful.setVisible(true);
            pause.setOnFinished(e -> adminCopySuccessful.setVisible(false));
            pause.play();
        }
    }

    /**
     * This method aims to copy the student code to the user's system clipboard
     * after clicking the button for copying the student code.
     * Displays a message after successfully copying the code.
     */
    public void copyStudentCode() {
        content.putString(studentCode.getText());
        clipboard.setContent(content);

        //Shows studentCopySuccessful label for 1 second and hides it again
        if (!studentCopySuccessful.isVisible()) {
            studentCopySuccessful.setVisible(true);
            pause.setOnFinished(e -> studentCopySuccessful.setVisible(false));
            pause.play();
        }
    }

    /**
     * This method takes you back to JoinQuBo after clicking the Back to Homepage button.
     */
    public void backToHome(){
        SceneLoader.backToHome((Stage) backToHome.getScene().getWindow());
    }

}
