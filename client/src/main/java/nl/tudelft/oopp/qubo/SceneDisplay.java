package nl.tudelft.oopp.qubo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.controllers.JoinQuBoController;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;

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
        //Load the application homepage
        SceneLoader.defaultLoader(primaryStage, "JoinQuBo");

        //Show the application homepage
        primaryStage.show();
    }
}
