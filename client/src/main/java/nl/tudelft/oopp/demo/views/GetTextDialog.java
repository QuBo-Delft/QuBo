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

    static String userInput;

    /**
     * This method gets user's text input after clicking submission button.
     *
     * @param title the title of dialog.
     * @param message  the message to be displayed.
     * @return the user text input.
     */
    public static String display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);
        TextArea inputField = new TextArea();
        inputField.setPromptText("Please enter the text");
        Button submitButton = new Button("Confirm submission");
        submitButton.setOnAction(e -> {
            userInput = inputField.getText();
            window.close();
        });
        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, inputField, submitButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return userInput;
    }
}
