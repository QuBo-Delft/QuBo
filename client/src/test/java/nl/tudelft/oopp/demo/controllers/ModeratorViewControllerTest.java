package nl.tudelft.oopp.demo.controllers;

import javafx.stage.Stage;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

class ModeratorViewControllerTest extends TestFxBase {

    /*
        These elements are returned from either the @Start or @BeforeAll to be used with testing
     */
    // Placeholder for elements

    //Initiate testing done through the TestFX library
    @Start
    void start(Stage stage) throws IOException {
        String fxmlSheet = "ModeratorView";
        start(stage, fxmlSheet);
    }
}