package nl.tudelft.oopp.qubo;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;

import java.io.IOException;

/**
 * The Scene display.
 */
public class SceneDisplay extends Application {

    /**
     * The entry point of application.
     *
     * @param args The input arguments.
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
