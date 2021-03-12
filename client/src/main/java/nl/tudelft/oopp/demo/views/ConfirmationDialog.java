package nl.tudelft.oopp.demo.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationDialog {

    static boolean answer;

    /**
     * This method creates a dialog window with a question as the message of the window. 
     * Users can click on the "Yes" and "No" buttons in this dialog window to answer said question.
     *
     * @param title     The title of this dialog.
     * @param message   The message to be displayed.
     * @return True if and only if the user clicked "Yes".
     */
    public static boolean display(String title, String message) {

        Stage window = new Stage();
        // Block user action
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        // Set label to the message
        label.setText(message);
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        // Set button click action
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        // Add components to layout
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        // Display this dialog
        window.setScene(scene);
        // Can return back only if the current dialog is closed
        window.showAndWait();

        // Return user's choice: true or false
        return answer;
    }
}
