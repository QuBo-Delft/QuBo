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
     * This method aims to make this window to display the student code, the moderator code, 
     * and the title of the question board just created.
     *
     * @param qb    The QuestionBoardCreationDto that contains the data of a question board.
     */
    public void displayCodes(QuestionBoardCreationDto qb) {
        boardTitle.setText(qb.getTitle());
        adminCode.setText(qb.getModeratorCode().toString());
        studentCode.setText(qb.getId().toString());
    }

    /**
     * This method aims to copy the admin code to the user's system clipboard
     * after clicking the button for copying the admin code.
     */
    public void copyAdminCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(adminCode.toString());
        clipboard.setContent(content);
    }

    /**
     * This method aims to copy the student code to the user's system clipboard
     * after clicking the button for copying the student code.
     */
    public void copyStudentCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(studentCode.toString());
        clipboard.setContent(content);
    }

}
