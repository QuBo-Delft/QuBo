package nl.tudelft.oopp.demo.views;

import javafx.scene.control.Alert;

public class AlertDialog {
    
    /**
     * This is an alert dialog that will pop up when called with alert title and message.
     *
     * @param title     The title of this alert.
     * @param message   The message to be displayed.
     */
    public static void display(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
