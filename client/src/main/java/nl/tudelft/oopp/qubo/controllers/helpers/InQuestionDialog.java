package nl.tudelft.oopp.qubo.controllers.helpers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class InQuestionDialog {
    public Button yes;
    public Button cancel;
    public HBox dialogue;

    /**
     * This method displays a confirmation dialogue at the bottom of the question.
     *
     * @param options       The options menu node. (Needs to be disabled.)
     * @param questionBody  Text node of the question content. (Needed for layout purposes.)
     * @param questionPane  GridPane of the question. (Needed to add a row for the dialogue.)
     * @param message       Message to be displayed in the dialogue.
     */
    public InQuestionDialog(MenuButton options, Text questionBody,
                            GridPane questionPane, String message) {
        //Disable options menu
        options.setDisable(true);

        //Create new label
        Label confirmation = new Label(message);
        confirmation.setPadding(new Insets(5, 5, 5, 5));
        confirmation.setWrapText(true);
        //Set width bounds to the dialogue so it doesn't overflow the question box
        confirmation.prefWidthProperty().bind(questionBody.wrappingWidthProperty().subtract(55));
        confirmation.minWidthProperty().bind(questionBody.wrappingWidthProperty().subtract(55));
        confirmation.maxWidthProperty().bind(questionBody.wrappingWidthProperty().subtract(55));
        HBox.setHgrow(confirmation, Priority.ALWAYS);

        //Create buttons
        yes = new Button("Yes");
        yes.getStyleClass().add("inQuestionConfirmationYes");
        cancel = new Button("Cancel");
        cancel.getStyleClass().add("inQuestionConfirmationCancel");
        dialogue = new HBox(confirmation, yes, cancel);

        //Set layouts
        dialogue.setPadding(new Insets(5, 10, 5, 10));
        dialogue.setSpacing(10);
        dialogue.setAlignment(Pos.CENTER);

        //Show confirmation dialogue
        questionPane.addRow(1, dialogue);
        GridPane.setColumnSpan(dialogue, GridPane.REMAINING);
    }
}
