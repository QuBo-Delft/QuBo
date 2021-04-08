package nl.tudelft.oopp.qubo.controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

/**
 * This class tests the ModeratorViewController which controls the ModeratorView.fxml
 */
class ModeratorViewControllerTest extends TestFxBase {

    /*
        These dto's are used in various tests as well as in @Start
     */
    QuestionBoardDetailsDto qcOpen = createOpenQuBo();

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     * @throws IOException IOException thrown by incorrect load in start method.
     */
    @Start
    void start(Stage stage) throws IOException {
        new SceneLoader().viewLoader(qcOpen, stage, "", "ModeratorView", getModCodeOpen());
    }
}