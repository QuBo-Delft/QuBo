package nl.tudelft.oopp.qubo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.communication.ServerCommunication;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;

/**
 * This class is a controller for the QuBoDetails.fxml
 */
public class QuBoDetailsController {
    @FXML
    private Label title;

    @FXML
    private Label closedStatus;

    @FXML
    private Label studentCode;

    @FXML
    private Label startTime;

    @FXML
    private Button copyStudentCode;

    @FXML
    private Button copyModeratorCode;

    @FXML
    private Label moderatorCode;

    @FXML
    private Label moderatorCodeLabel;

    @FXML
    private Button close;

    @FXML
    private RowConstraints modRow1;

    @FXML
    private RowConstraints modRow2;

    private QuestionBoardDetailsDto qd;
    private QuestionBoardCreationDto qc;

    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();

    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    /**
     * Sets the QuestionBoardDetailsDto to be used.
     *
     * @param qd The QuestionBoardDetailsDto.
     */
    public void setQd(QuestionBoardDetailsDto qd) {
        this.qd = qd;
    }

    /**
     * Sets the QuestionBoardCreationDto to be used.
     *
     * @param qc The QuestionBoardCreationDto.
     */
    public void setQc(QuestionBoardCreationDto qc) {
        this.qc = qc;
    }

    /**
     * Sets only the fields that are supposed to be shown when the details are requested
     * through the student view and hides moderator related fields.
     */
    public void setStudent() {
        title.setText(qd.getTitle());
        closedStatus.setText(Boolean.toString(qd.isClosed()));
        studentCode.setText(qd.getId().toString());
        startTime.setText(qd.getStartTime().toString());
        copyModeratorCode.visibleProperty().bindBidirectional(moderatorCode.visibleProperty());
        copyModeratorCode.visibleProperty().bindBidirectional(moderatorCodeLabel.visibleProperty());
        copyModeratorCode.setVisible(false);
        modRow1.setMaxHeight(0);
        modRow2.setMaxHeight(0);
    }

    /**
     * Sets all fields of the details pane by creation a QuestionBoardDetailsDto to retrieve closed status.
     */
    public void setModerator() {
        title.setText(qc.getTitle());
        String resBody = ServerCommunication.retrieveBoardDetails(qd.getId());
        QuestionBoardDetailsDto qdMod = gson.fromJson(resBody, QuestionBoardDetailsDto.class);
        closedStatus.setText(Boolean.toString(qd.isClosed()));
        studentCode.setText(qc.getId().toString());
        moderatorCode.setText(qc.getModeratorCode().toString());
        startTime.setText(qc.getStartTime().toString());
    }

    /**
     * Closes the details pane when the close button is pressed.
     *
     * @param event The pressing of the close button.
     */
    public void closeHandler(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Copies the moderator code when either the code itself or the button is pressed.
     */
    public void copyModeratorCodeHandler() {
        clipboardContent.putString(qc.getModeratorCode().toString());
        clipboard.setContent(clipboardContent);
    }

    /**
     * Copies the student code when either the code itself or the button is pressed.
     */
    public void copyStudentCodeHandler() {
        if (qc == null) {
            clipboardContent.putString(qd.getId().toString());
        } else {
            clipboardContent.putString(qc.getId().toString());
        }
        clipboard.setContent(clipboardContent);
    }
}
