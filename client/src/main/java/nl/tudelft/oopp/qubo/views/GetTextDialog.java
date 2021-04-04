package nl.tudelft.oopp.qubo.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The get text dialog which allows users to enter text in a pop-up dialog.
 */
public class GetTextDialog {

    private static String userInput;

    /**
     * This method creates a dialog window which allows users to enter text,
     * and send the user input to the method caller.
     *
     * @param message            The message to be displayed as a prompt
     *                           in the text area.
     * @param submitBtnText      The display name of the submit button.
     * @param cancelBtnText      The display name of the cancel button.
     * @param checkMinEightChars The fact that the user input needs to be
     *                           check if it has at least 8 characters.
     * @return The user's text input.
     */
    public static String display(String message, String submitBtnText,
                                 String cancelBtnText, boolean checkMinEightChars) {

        Stage window = new Stage();

        // Block the user from performing other actions
        window.initModality(Modality.APPLICATION_MODAL);
        window.setHeight(405);
        window.setWidth(720);
        // Hide the title bar of the window
        window.initStyle(StageStyle.UNDECORATED);

        TextArea inputField = new TextArea();
        inputField.setPromptText(message);
        inputField.setWrapText(true);
        inputField.setMinWidth(320);
        inputField.setMinHeight(240);
        // Set the input field to not be initially focused in order to show the prompt
        inputField.setFocusTraversable(false);

        // The cancel button's text is set to be the cancelBtnText
        Button cancelButton = new Button(cancelBtnText);
        cancelButton.setOnAction(e -> {
            window.close();
            userInput = null;
        });

        Label messageLabel = new Label();

        // The submit button's text is set to be the submitBtnText
        Button submitButton = new Button(submitBtnText);
        submitButton.setOnAction(e -> {
            userInput = inputField.getText();
            // Check if it is required to check if the user input is at least 8 characters long
            if (checkMinEightChars) {
                if (isAtLeastEightChars(messageLabel)) {
                    // The user input is checked to be at least 8 characters long
                    window.close();
                }
            } else if (userInput == null || userInput.length() == 0) {
                messageLabel.setText("Error: Please enter text");
            } else {
                // The user input is valid
                window.close();
            }
        });

        // Add style classes to the elements
        submitButton.getStyleClass().add("submitBtn");
        cancelButton.getStyleClass().add("cancelBtn");

        // The top hHox, act like a spacer
        HBox topHBox = new HBox(25);
        Region r0 = new Region();
        topHBox.getChildren().add(r0);

        // The middle hBox, contains the input field
        HBox middleHBox = new HBox(30);
        Region r1 = new Region();
        Region r2 = new Region();
        middleHBox.getChildren().addAll(r1, inputField, r2);

        // The bottom hBox, contains the message label, the submit button, and the cancel button
        HBox bottomHBox = new HBox(20);
        Region r3 = new Region();
        Region r4 = new Region();
        bottomHBox.getChildren().addAll(r4, messageLabel, r3, submitButton, cancelButton);

        // Set the r3 spacer to scale with other layouts
        HBox.setHgrow(r3, Priority.ALWAYS);

        bottomHBox.setPadding(new Insets(0, 15, 15, 10));
        bottomHBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(30);
        layout.getChildren().addAll(topHBox, middleHBox, bottomHBox);

        // Center all components
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("/css/GetTextDialog.css");
        // Display the dialog
        window.setScene(scene);
        // Can return back only if the current dialog is closed
        window.showAndWait();

        // Return user's text input
        return userInput;
    }

    /**
     * This method checks if the user input is at least 8 characters long.
     *
     * @param messageLabel The message label that displays a message.
     * @return True if and only if the user input is at least 8 characters long.
     */
    private static boolean isAtLeastEightChars(Label messageLabel) {
        if (userInput == null || userInput.length() < 8) {
            // The user input is not at least 8 characters long, display an error massage
            messageLabel.setText("Error: the text has to be at least 8 characters long");
            return false;
        }
        return true;
    }
}
