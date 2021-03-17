package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;

public class BoardCodesController {
    @FXML
    private Label boardTitle;
    @FXML
    private Label adminCode;
    @FXML
    private Label studentCode;

    /**
     * Displays question board codes.
     *
     * @param qb Requested question board to display details of
     */
    public void displayCodes(QuestionBoardCreationDto qb) {
        boardTitle.setText(qb.getTitle());
        adminCode.setText(qb.getModeratorCode().toString());
        studentCode.setText(qb.getId().toString());
    }

    /**
     * Action event after clicking the button for copying the admin code.
     * Copies code to the user's system clipboard.
     */
    public void copyAdminCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(adminCode.toString());
        clipboard.setContent(content);
    }

    /**
     * Action event after clicking the button for copying the student code.
     * Copies code to the user's system clipboard.
     */
    public void copyStudentCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(studentCode.toString());
        clipboard.setContent(content);
    }

}
