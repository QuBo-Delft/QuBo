package nl.tudelft.oopp.demo.test;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneDisplay extends Application {

    /**
     *
     * @param primaryStage  The stage.
     * @throws IOException if the resource is not found.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        // By changing the resource path, it is able to display an specific fxml file
        URL xmlUrl = getClass().getResource("/QuestionBoardCodes.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * The entry point of application.
     *
     * @param args  The input arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
