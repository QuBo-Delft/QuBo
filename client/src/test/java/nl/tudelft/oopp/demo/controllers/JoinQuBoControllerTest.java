package nl.tudelft.oopp.demo.controllers;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.WindowMatchers;

import java.io.IOException;

/**
 * This class tests the JoinQuBoController which controls the JoinQuBo.fxml.
 */
class JoinQuBoControllerTest extends TestFxBase {

    /*
        These Strings allow store the UUIDs to string from the @BeforeAll.
     */
    static String openBoard;
    static String openBoardModerator;
    static String closedBoard;
    static String closedBoardModerator;

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     * @throws IOException IOException thrown by incorrect load in start method.
     */
    @Start
    void start(Stage stage) throws IOException {
        String fxmlSheet = "JoinQuBo";
        start(stage, fxmlSheet);
    }

    /**
     * Creates a closed and open QuBo for testing purposes.
     */
    @BeforeAll
    static void setQuBos() {
        openBoard = createOpenQuBo().getId().toString();
        openBoardModerator = createOpenQuBo().getModeratorCode().toString();
        closedBoard = createClosedQuBo().getId().toString();
        closedBoardModerator = createClosedQuBo().getModeratorCode().toString();
    }

    /**
     * Click on the create QuBo button and ensure the create scene is displayed.
     *
     * @param robot TestFX robot.
     */
    @Test
    void createButtonClickedTest(FxRobot robot) {
        robot.clickOn("#createBtn");
        FxAssert.verifyThat(robot.window("(Create Question Board)"), WindowMatchers.isShowing());
    }

    /**
     * Click on the join button with no QuBo code entered.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedNoCode(FxRobot robot) {
        robot.clickOn("#joinBtn");
        FxAssert.verifyThat("#errorMessageLabel", Node::isVisible);
    }

    /**
     * Click on the join button with null entered.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedNullCode(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write("null");
        robot.clickOn("#joinBtn");
        FxAssert.verifyThat("#errorMessageLabel", Node::isVisible);
    }

    /**
     * Click on the join button with empty entered.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedEmptyCode(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write("");
        robot.clickOn("#joinBtn");
        FxAssert.verifyThat("#errorMessageLabel", Node::isVisible);
    }

    /**
     * Click on the join button with an incorrect UUID entered.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedIncorrectUuid(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write("1b219900-27c0-4e40-9d0a-a0fa3951b224");
        robot.clickOn("#joinBtn");
        FxAssert.verifyThat("#errorMessageLabel", Node::isVisible);
    }

    /**
     * Click on the join button with a correct UUID entered for an open meeting - student.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedCorrectUuidOpenStu(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write(openBoard);
        robot.clickOn("#joinBtn");
        FxAssert.verifyThat(robot.window("(QuBo)"), WindowMatchers.isShowing());
    }

    /**
     * Click on the join button with a correct UUID entered for a closed meeting - student.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedCorrectUuidClosedStu(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write(closedBoard);
        robot.clickOn("#joinBtn");
        FxAssert.verifyThat(robot.window("(QuBo)"), WindowMatchers.isShowing());
    }

    /**
     * Click on the join button with a correct UUID entered for a open meeting - moderator.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedCorrectUuidOpenMod(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write(openBoardModerator);
        robot.clickOn("#joinBtn");
        FxAssert.verifyThat(robot.window("(QuBo - Moderator)"), WindowMatchers.isShowing());
    }

    /**
     * Click on the join button with a correct UUID entered for a closed meeting - moderator.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedCorrectUuidClosedMod(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write(closedBoardModerator);
        robot.clickOn("#joinBtn");
        FxAssert.verifyThat(robot.window("(QuBo - Moderator)"), WindowMatchers.isShowing());
    }
}