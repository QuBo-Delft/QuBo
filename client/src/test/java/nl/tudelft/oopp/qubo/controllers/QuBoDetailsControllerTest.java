package nl.tudelft.oopp.qubo.controllers;

import javafx.scene.Node;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.LabeledMatchers;

class QuBoDetailsControllerTest extends TestFxBase {

    /*
        These dates and objects are used for various tests. The objects are instantiated in @Start.
     */
    Stage testFxStage;
    QuestionBoardDetailsDto qdOpen = createOpenQuBo();
    QuestionBoardDetailsDto qdClosed = createClosedQuBo();

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     */
    @Start
    void start(Stage stage) {
        testFxStage = stage;
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", qdOpen.getId());
    }

    /**
     * Check whether the displayed title is equal to the actual Question Board's title.
     *
     * @param robot TestFX robot.
     */
    @Test
    void titleCheck(FxRobot robot) {
        FxAssert.verifyThat("#title", LabeledMatchers.hasText(qdOpen.getTitle()));
    }

    /**
     * Check whether the displayed start time is equal to the actual Question Board's start time.
     *
     * @param robot TestFX robot.
     */
    @Test
    void startTimeCheck(FxRobot robot) {
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", qdOpen.getId());
        FxAssert.verifyThat("#startTime", LabeledMatchers.hasText(qdOpen.getStartTime().toString()));
    }

    /**
     * Check whether the displayed closed status is equal to false when a board is open.
     *
     * @param robot TestFX robot.
     */
    @Test
    void closedStatusOpen(FxRobot robot) {
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", qdOpen.getId());
        FxAssert.verifyThat("#closedStatus", LabeledMatchers.hasText("false"));
    }

    /**
     * Check whether the displayed closed status is equal to true when a board is closed.
     *
     * @param robot TestFX robot.
     */
    @Test
    void closedStatusClosed(FxRobot robot) {
        new SceneLoader().viewLoader(qdClosed, testFxStage, "", "QuBoDetails", qdClosed.getId());
        FxAssert.verifyThat("#closedStatus", LabeledMatchers.hasText("true"));
    }

    /**
     * Check whether the displayed student code is equal to the actual Question Board's student code - Student.
     *
     * @param robot TestFX robot.
     */
    @Test
    void studentCodeStudentView(FxRobot robot) {
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", qdOpen.getId());
        FxAssert.verifyThat("#studentCode", LabeledMatchers.hasText(qdOpen.getId().toString()));
    }

    /**
     * Check whether the displayed student code is equal to the actual Question Board's student code - Moderator.
     *
     * @param robot TestFX robot.
     */
    @Test
    void studentCodeModeratorView(FxRobot robot) {
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", getModCodeOpen());
        FxAssert.verifyThat("#studentCode", LabeledMatchers.hasText(qdOpen.getId().toString()));
    }

    /**
     * Copy the student code by clicking the copy student code button - Student.
     *
     * @param robot TestFX robot.
     */
    @Test
    void copyStudentCodeStudentView(FxRobot robot) {
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", qdOpen.getId());
        FxAssert.verifyThat("#copyStudentCode", NodeMatchers.isVisible());
        robot.clickOn("#copyStudentCode");
        clipboardTest();
    }

    /**
     * Copy the student code by clicking the copy student code button - Moderator.
     *
     * @param robot TestFX robot.
     */
    @Test
    void copyStudentCodeModeratorView(FxRobot robot) {
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", getModCodeOpen());
        FxAssert.verifyThat("#copyStudentCode", NodeMatchers.isVisible());
        robot.clickOn("#copyStudentCode");
        clipboardTest();
    }

    /**
     * Check whether the displayed moderator code is not shown when opening from a Student View.
     *
     * @param robot TestFX robot.
     */
    @Test
    void moderatorCodeStudentView(FxRobot robot) {
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", qdOpen.getId());
        FxAssert.verifyThat("#moderatorCode", NodeMatchers.isInvisible());
    }

    /**
     * Check whether the displayed moderator code is equal to the actual Question Board's moderator code - Moderator.
     *
     * @param robot TestFX robot.
     */
    @Test
    void moderatorCodeModeratorView(FxRobot robot) {
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", getModCodeOpen());
        FxAssert.verifyThat("#moderatorCode", LabeledMatchers.hasText(getModCodeOpen().toString()));
    }

    /**
     * Check whether the copy moderator code button is not shown when opening from a Student View.
     *
     * @param robot TestFX robot.
     */
    @Test
    void copyModeratorCodeStudentView(FxRobot robot) {
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", qdOpen.getId());
        FxAssert.verifyThat("#copyModeratorCode", NodeMatchers.isInvisible());
    }

    /**
     * Click create now button while null title.
     *
     * @param robot TestFX robot.
     */
    @Test
    void copyModeratorCodeModeratorView(FxRobot robot) {
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", getModCodeOpen());
        FxAssert.verifyThat("#copyModeratorCode", NodeMatchers.isVisible());
        robot.clickOn("#copyModeratorCode");
        clipboardTest();
    }

    /**
     * Click create now button while null title.
     *
     * @param robot TestFX robot.
     */
    @Test
    void closeButton(FxRobot robot) {
        new SceneLoader().viewLoader(qdOpen, testFxStage, "", "QuBoDetails", qdOpen.getId());
        robot.clickOn("#close");
        //FxAssert.verifyThat("#errorTitle", WindowMatchers::isNotShowing );
    }
}