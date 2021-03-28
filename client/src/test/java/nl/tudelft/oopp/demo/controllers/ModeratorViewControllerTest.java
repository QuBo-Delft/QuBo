package nl.tudelft.oopp.demo.controllers;

import javafx.stage.Stage;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

/**
 * This class tests the ModeratorViewController which controls the ModeratorView.fxml
 */
class ModeratorViewControllerTest extends TestFxBase {

    static {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("java.awt.headless", "true");
            System.setProperty("testfx.robot", "glass");
            System.setProperty("glass.platform", "Monocle");
            System.setProperty("monocle.platform", "Headless");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.useFontConfig", "false");
        }
    }

    /*
        These dto's are used in various tests as well as in @Start
     */
    QuestionBoardCreationDto qcOpen = createOpenQuBo();

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     * @throws IOException IOException thrown by incorrect load in start method.
     */
    @Start
    void start(Stage stage) throws IOException {
        String fxmlSheet = "ModeratorView";
        startDetails(stage, fxmlSheet, qcOpen.getId());
    }
}