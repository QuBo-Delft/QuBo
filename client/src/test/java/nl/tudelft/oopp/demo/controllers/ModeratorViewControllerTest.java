package nl.tudelft.oopp.demo.controllers;

import javafx.stage.Stage;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

class ModeratorViewControllerTest extends TestFxBase {

    /*
        These elements are returned from either the @Start or @BeforeAll to be used with testing
     */
    QuestionBoardCreationDto qcOpen = createOpenQuBo();

    //Initiate testing done through the TestFX library
    @Start
    void start(Stage stage) throws IOException {
        String fxmlSheet = "ModeratorView";
        startDetails(stage, fxmlSheet, qcOpen.getId());
    }
}