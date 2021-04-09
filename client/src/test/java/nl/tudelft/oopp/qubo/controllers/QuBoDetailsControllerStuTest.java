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

class QuBoDetailsControllerStuTest extends TestFxBase {

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
        new SceneLoader().viewLoader(qdOpen, stage, "", "QuBoDetails", null);
    }

    /**
     * Check whether the displayed title is equal to the actual Question Board's title.
     */
    @Test
    void titleCheck() {
        // Expect that the label's text is equals to the title of the Question Board
        FxAssert.verifyThat("#title", LabeledMatchers.hasText(qdOpen.getTitle()));
    }

    /**
     * Check whether the displayed start time is equal to the actual Question Board's start time.
     */
    @Test
    void startTimeCheck() {
        // Expect that the label's text is equals to the start time of the Question Board
        FxAssert.verifyThat("#startTime", LabeledMatchers.hasText(qdOpen.getStartTime().toString()));
    }

    /**
     * Check whether the displayed closed status is equal to false when a board is open.
     */
    @Test
    void closedStatusOpen() {
        // Expect that the label's text is equal to false when the board is not closed
        FxAssert.verifyThat("#closedStatus", LabeledMatchers.hasText("false"));
    }

    /**
     * Check whether the displayed student code is equal to the actual Question Board's student code - Student.
     */
    @Test
    void studentCodeStudentView() {
        // Expect that the label's text is equals to the student code of the Question Board
        FxAssert.verifyThat("#studentCode", LabeledMatchers.hasText(qdOpen.getId().toString()));
    }

    /**
     * Copy the student code by clicking the copy student code button - Student.
     *
     * @param robot TestFX robot.
     */
    @Test
    void copyStudentCodeStudentView(FxRobot robot) {
        // Expect that the button is visible
        FxAssert.verifyThat("#copyStudentCode", NodeMatchers.isVisible());
        robot.clickOn("#copyStudentCode");
        // Expect that the content of the clipboard equals the student code of the Question Board
        clipboardTest();
    }

    /**
     * Check whether the displayed moderator code is not shown when opening from a Student View.
     */
    @Test
    void moderatorCodeStudentView() {
        // Expect that the label is hidden when trying to access it from a Student View
        FxAssert.verifyThat("#moderatorCode", NodeMatchers.isInvisible());
    }

    /**
     * Check whether the copy moderator code button is not shown when opening from a Student View.
     */
    @Test
    void copyModeratorCodeStudentView() {
        // Expect that the button is hidden when trying to access it from a Student View
        FxAssert.verifyThat("#copyModeratorCode", NodeMatchers.isInvisible());
    }
}