package nl.tudelft.oopp.qubo.controllers;

import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import org.testfx.framework.junit5.Start;

/**
 * This class tests the ModeratorViewController which controls the ModeratorView.fxml
 */
class ModeratorViewControllerTest extends TestFxBase {

    /*
        Create an open Question Board which is loaded in @Start.
     */
    QuestionBoardDetailsDto qdOpen = createOpenQuBo();

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     */
    @Start
    void start(Stage stage) {
        new SceneLoader().viewLoader(qdOpen, stage, "", "StudentView", qdOpen.getId());
    }


    
}