package nl.tudelft.oopp.demo.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GetTextDialog {

    private static String userInput;

    /**
     * This method creates a dialog window which allows users to enter text. 
     * After the user presses "Confirm submission" the textual input is returned 
     * and the dialog window disappears.
     *
     * @param title     The title of this dialog.
     * @param message   The message to be displayed.
     * @return The user's text input.
     */
    public static String display(String title, String message) {

        Stage window = new Stage();

        // Block user action
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        TextArea inputField = new TextArea();
        inputField.setPromptText("Please enter the text");

        Button submitButton = new Button("Confirm submission");
        // Set button click action
        submitButton.setOnAction(e -> {
            userInput = inputField.getText();
            window.close();
        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, inputField, submitButton);
        // Center all components
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        // Display the dialog
        window.setScene(scene);
        // Can return back only if the current dialog is closed
        window.showAndWait();
        
        // Return user's text input
        return userInput;
    }
}
