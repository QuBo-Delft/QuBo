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
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;

import java.util.UUID;

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

    private UUID modCode;
    private QuestionBoardDetailsDto qd;

    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();

    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    /**
     * Sets only the fields that are supposed to be shown when details are requested
     * through a student view or shows all when requested through a moderator view.
     * It also sets the moderator code and QuestionBoardDetailsDto so they can be used for copying.
     *
     * @param qd      The QuestionBoardDetailsDto.
     * @param modCode The moderator code of the question board.
     */
    public void setDetails(QuestionBoardDetailsDto qd, UUID modCode) {
        title.setText(qd.getTitle());
        closedStatus.setText(Boolean.toString(qd.isClosed()));
        studentCode.setText(qd.getId().toString());
        startTime.setText(qd.getStartTime().toString());

        if (modCode == null) {
            copyModeratorCode.visibleProperty().bindBidirectional(moderatorCode.visibleProperty());
            copyModeratorCode.visibleProperty().bindBidirectional(moderatorCodeLabel.visibleProperty());
            copyModeratorCode.setVisible(false);
            modRow1.setMaxHeight(0);
            modRow2.setMaxHeight(0);
        } else {
            moderatorCode.setText(modCode.toString());
        }
        this.qd = qd;
        this.modCode = modCode;
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
     * Copies the moderator code when the copy moderator code button is pressed.
     */
    public void copyModeratorCodeHandler() {
        clipboardContent.putString(modCode.toString());
        clipboard.setContent(clipboardContent);
    }

    /**
     * Copies the student code when the copy student code button is pressed.
     */
    public void copyStudentCodeHandler() {
        clipboardContent.putString(qd.getId().toString());
        clipboard.setContent(clipboardContent);
    }
}
