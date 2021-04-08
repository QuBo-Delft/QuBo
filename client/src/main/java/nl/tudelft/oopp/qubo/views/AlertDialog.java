package nl.tudelft.oopp.qubo.views;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;
import javafx.stage.StageStyle;

/**
 * The Alert dialog.
 */
public class AlertDialog {

    /**
     * This is an alert dialog with a title and message
     * that will pop up on the screen when this method is called.
     *
     * @param title   The title of this alert.
     * @param message The message to be displayed.
     */
    public static void display(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(title);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        dialogPane.getStylesheets().add("/css/AlertDialog.css");
        dialogPane.getStyleClass().add("aDialog");
        alert.showAndWait();
    }

}
