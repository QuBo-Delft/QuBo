package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.net.URL;

@ExtendWith(ApplicationExtension.class)
public abstract class TestFxBase {

    public Scene start(Stage stage, String fxmlSheet) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        // By giving the resource path, it is able to display a specific fxml file
        URL xmlUrl = getClass().getResource("/fxmlsheets/" + fxmlSheet + ".fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();

        return stage.getScene();
    }
}
