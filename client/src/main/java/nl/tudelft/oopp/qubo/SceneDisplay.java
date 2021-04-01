package nl.tudelft.oopp.qubo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.controllers.JoinQuBoController;

import java.io.IOException;
import java.net.URL;

public class SceneDisplay extends Application {

    /**
     * The entry point of application.
     *
     * @param args  The input arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method that loads a scene.
     *
     * @param primaryStage  The stage.
     * @throws IOException if the resource is not found.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        // By giving the resource path, it is able to display an specific fxml file
        URL xmlUrl = getClass().getResource("/fxmlsheets/JoinQuBo.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(JoinQuBoController::closeApplication);
        primaryStage.show();
    }
}
