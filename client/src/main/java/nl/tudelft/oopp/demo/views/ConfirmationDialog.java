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
     * This method gets user's choice after clicking Tes or No button.
     *
     * @param title     The title of this dialog.
     * @param message   The message to be displayed.
     * @return True iff the user clicked "yes".
     */
    public static boolean display(String title, String message) {
        Stage window = new Stage();
        // Block user action
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        // Create components
        Label label = new Label();
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
        // Can return back only if close the current dialog
        window.showAndWait();
        // Return user's choice: true or false
        return answer;
    }
}
