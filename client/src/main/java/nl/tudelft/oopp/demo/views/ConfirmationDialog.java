package nl.tudelft.oopp.demo.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfirmationDialog {

    private static boolean answer;

    /**
     * This method creates a dialog window with a question and a brief description.
     * Users can click on the "Yes" and "No" buttons in this dialog window to answer said question.
     *
     * @param titleParam     The title of this dialog.
     * @param messageParam   The message to be displayed.
     * @return True if and only if the user clicked "Yes".
     */
    public static boolean display(String titleParam, String messageParam) {
        Stage window = new Stage();
        // Block user action
        window.initModality(Modality.APPLICATION_MODAL);

        // Set title and message parameter into their respective labels
        Label title = new Label(titleParam);
        title.setStyle("-fx-font-size: 18");
        Label message = new Label(messageParam);
        // Wrap Text if too long
        int maxWidth = 340;
        title.setWrapText(true);
        message.setWrapText(true);
        title.setMaxWidth(maxWidth);
        message.setMaxWidth(maxWidth);
        // Center everything
        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        message.setAlignment(Pos.CENTER);
        message.setTextAlignment(TextAlignment.CENTER);

        // Create the buttons
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        yesButton.setStyle("-fx-font-size: 15");
        noButton.setStyle("-fx-font-size: 15");
        // Set button click action
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        // Create HBox to arrange the buttons
        HBox hbox = new HBox(yesButton, noButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(40);
        hbox.setPadding(new Insets(10,0,0,0));

        // Create VBox to arrange title, message and hbox with buttons
        VBox vbox = new VBox(title, message, hbox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(15);

        // Create BorderPane to put vbox
        BorderPane dialogue = new BorderPane(vbox);
        dialogue.setPadding(new Insets(30,40,30,40));

        // Set the scene to dialogue
        Scene scene = new Scene(dialogue);
        // Hide the title bar of the window
        window.initStyle(StageStyle.UNDECORATED);
        // Display this dialog
        window.setScene(scene);
        // Can return back only if the current dialog is closed
        window.showAndWait();

        // Return user's choice: true or false
        return answer;
    }
}
