package nl.tudelft.oopp.demo.sceneloader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.controllers.QuBoCodesController;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.views.AlertDialog;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class SceneLoader {

    public static void loadQuestionBoardCodes(QuestionBoardCreationDto qd) {
        // Create an FXMLLoader of QuBoCodes.fxml
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/QuBoCodes.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the controller of QuBoCodes
        QuBoCodesController controller = loader.getController();

        // Transfer the data for QuBoCodes
        controller.displayCodes(qd);

        Stage stage = new Stage();

        // Check if root is null
        if (root == null) {
            AlertDialog.display("", "Unable to display the codes");
            return;
        }

        // Display the scene
        stage.setScene(new Scene(root));
        stage.show();
    }

}
