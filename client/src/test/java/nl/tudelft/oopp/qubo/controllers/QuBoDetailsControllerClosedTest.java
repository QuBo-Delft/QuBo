package nl.tudelft.oopp.qubo.controllers;

import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

public class QuBoDetailsControllerClosedTest extends TestFxBase {

    /*
        Create a closed Question Board which is loaded in @Start.
     */
    QuestionBoardDetailsDto qdClosed = createClosedQuBo();

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     */
    @Start
    void start(Stage stage) {
        // Set the board to closed
        qdClosed.setClosed(true);
        new SceneLoader().viewLoader(qdClosed, stage, "", "QuBoDetails", qdClosed.getId());
    }

    /**
     * Check whether the displayed closed status is equal to true when a board is closed.
     */
    @Test
    void closedStatusClosed() {
        // Expect that the label's text is equal to true when the board is closed
        FxAssert.verifyThat("#closedStatus", LabeledMatchers.hasText("true"));
    }
}
