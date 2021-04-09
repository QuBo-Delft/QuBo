package nl.tudelft.oopp.qubo.controllers;

import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;

public class QuBoDetailsControllerModTest extends TestFxBase {

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
        new SceneLoader().viewLoader(qdOpen, stage, "", "QuBoDetails", getModCodeOpen());
    }

    /**
     * Copy the moderator code by clicking the copy moderator code button - Moderator.
     *
     * @param robot TestFX robot.
     */
    @Test
    void copyModeratorCodeModeratorView(FxRobot robot) {
        FxAssert.verifyThat("#copyModeratorCode", NodeMatchers.isVisible());
        robot.clickOn("#copyModeratorCode");
        // Expect that the content of the clipboard equals the student code of the Question Board
        clipboardTest();
    }

    /**
     * Check whether the displayed moderator code is equal to the Question Board's moderator code - Moderator.
     */
    @Test
    void moderatorCodeModeratorView() {
        // Expect that the label's text is equals to the moderator code of the Question Board
        FxAssert.verifyThat("#moderatorCode", LabeledMatchers.hasText(getModCodeOpen().toString()));
    }

    /**
     * Copy the student code by clicking the copy student code button - Moderator.
     *
     * @param robot TestFX robot.
     */
    @Test
    void copyStudentCodeModeratorView(FxRobot robot) {
        FxAssert.verifyThat("#copyStudentCode", NodeMatchers.isVisible());
        robot.clickOn("#copyStudentCode");
        // Expect that the content of the clipboard equals the student code of the Question Board
        clipboardTest();
    }

    /**
     * Check whether the displayed student code is equal to the Question Board's student code - Moderator.
     */
    @Test
    void studentCodeModeratorView() {
        // Expect that the label's text is equals to the student code of the Question Board
        FxAssert.verifyThat("#studentCode", LabeledMatchers.hasText(qdOpen.getId().toString()));
    }
}
