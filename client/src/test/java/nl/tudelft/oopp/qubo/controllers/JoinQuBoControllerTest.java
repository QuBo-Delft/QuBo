package nl.tudelft.oopp.qubo.controllers;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.WindowMatchers;


import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the JoinQuBoController which controls JoinQuBo.fxml.
 */
class JoinQuBoControllerTest extends TestFxBase {

    /*
        These Strings will be used to store the toString representations of the UUIDs created in the @BeforeAll.
     */
    private static String openBoard;
    private static String openBoardModerator;
    private static String closedBoard;
    private static String closedBoardModerator;

    Label errorMessageLabel;

    /*
     * Error messages that can be shown by the controller.
     */
    private final String errorBoard = "Error: Could not find the requested question board!\nPlease "
        + "check if you inserted the code correctly!";
    private final String errorUsername = "Error: No username was entered. Please enter a username";

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     */
    @Start
    void start(Stage stage) {
        SceneLoader.defaultLoader(stage, "JoinQuBo");
    }

    /**
     * Creates a closed and open Question Board for testing purposes and ready String input for the tests
     * by converting the respective UUID's to String.
     */
    @BeforeAll
    static void setQuBos() {
        QuestionBoardDetailsDto qcOpen = createOpenQuBo();
        QuestionBoardDetailsDto qcClosed = createClosedQuBo();
        openBoard = qcOpen.getId().toString();
        openBoardModerator = getModCodeOpen().toString();
        closedBoard = qcClosed.getId().toString();
        closedBoardModerator = getModCodeClosed().toString();
    }

    /**
     * Click on the create QuBo button and ensure the create scene is displayed.
     *
     * @param robot TestFX robot.
     */
    @Test
    void createButtonClickedTest(FxRobot robot) {
        robot.clickOn("#createBtn");
        // Expect that a new window is shown with the title "Create Question Board" (CreateQuBo.fxml)
        FxAssert.verifyThat(robot.window("(Create Question Board)"), WindowMatchers.isShowing());
    }

    /**
     * Click on the join button with nothing entered in either fields.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedNoCodeNoUser(FxRobot robot) {
        robot.clickOn("#joinBtn");
        // Expect that an error is shown
        FxAssert.verifyThat("#errorMessageLabel", Node::isVisible);
        errorMessageLabel = robot.lookup("#errorMessageLabel").queryAs(Label.class);
        // Expect that the error label contains an error message related to the Question Board field
        assertEquals(errorMessageLabel.getText(), errorBoard);
    }

    /**
     * Click on the join button with only the board id entered.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedOnlyId(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write(openBoard);
        robot.clickOn("#joinBtn");
        // Expect that an error is shown
        FxAssert.verifyThat("#errorMessageLabel", Node::isVisible);
        errorMessageLabel = robot.lookup("#errorMessageLabel").queryAs(Label.class);
        // Expect that the error label contains an error message related to the username field
        assertEquals(errorMessageLabel.getText(), errorUsername);
    }

    /**
     * Click on the join button with only the username entered.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedOnlyUsername(FxRobot robot) {
        robot.clickOn("#userName");
        robot.write("stu");
        robot.clickOn("#joinBtn");
        // Expect that an error is shown
        FxAssert.verifyThat("#errorMessageLabel", Node::isVisible);
        errorMessageLabel = robot.lookup("#errorMessageLabel").queryAs(Label.class);
        // Expect that the error label contains an error message related to the Question Board field
        assertEquals(errorMessageLabel.getText(), errorBoard);
    }

    /**
     * Click on the join button with null entered as id and user name.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedNullCode(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write("null");
        robot.clickOn("#userName");
        robot.write("null");
        robot.clickOn("#joinBtn");
        // Expect that an error is shown
        FxAssert.verifyThat("#errorMessageLabel", Node::isVisible);
        errorMessageLabel = robot.lookup("#errorMessageLabel").queryAs(Label.class);
        // Expect that the error label contains an error message related to the Question Board field
        assertEquals(errorMessageLabel.getText(), errorBoard);
    }

    /**
     * Click on the join button with empty ("") entered as id and user name.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedEmptyCode(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write("");
        robot.clickOn("#userName");
        robot.write("");
        robot.clickOn("#joinBtn");
        // Expect that an error is shown
        FxAssert.verifyThat("#errorMessageLabel", Node::isVisible);
        errorMessageLabel = robot.lookup("#errorMessageLabel").queryAs(Label.class);
        // Expect that the error label contains an error message related to the Question Board field
        assertEquals(errorMessageLabel.getText(), errorBoard);
    }

    /**
     * Click on the join button with a correct UUID but an empty user name.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedCorrectUuidNoUsername(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write(openBoard);
        robot.clickOn("#joinBtn");
        // Expect that an error is shown
        FxAssert.verifyThat("#errorMessageLabel", Node::isVisible);
        errorMessageLabel = robot.lookup("#errorMessageLabel").queryAs(Label.class);
        // Expect that the error label contains an error message related to the username field
        assertEquals(errorMessageLabel.getText(), errorUsername);
    }

    /**
     * Click on the join button with an incorrect UUID entered but entered username.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedIncorrectUuid(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        // A non existing UUID
        robot.write("1b219900-27c0-4e40-9d0a-a0fa3951b224");
        robot.clickOn("#userName");
        robot.write("stu");
        robot.clickOn("#joinBtn");
        // Expect that an error is shown
        FxAssert.verifyThat("#errorMessageLabel", Node::isVisible);
        errorMessageLabel = robot.lookup("#errorMessageLabel").queryAs(Label.class);
        // Expect that the error label contains an error message related to the Question Board field
        assertEquals(errorMessageLabel.getText(), errorBoard);
    }

    /**
     * Click on the join button with a correct UUID and user entered for an open meeting - student.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedCorrectUuidOpenStu(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write(openBoard);
        robot.clickOn("#userName");
        robot.write("stu");
        robot.clickOn("#joinBtn");
        // Expect that a new window is shown with the title "QuBo" (StudentView.fxml)
        FxAssert.verifyThat(robot.window("(QuBo)"), WindowMatchers.isShowing());
    }

    /**
     * Click on the join button with a correct UUID and user entered for a closed meeting - student.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedCorrectUuidClosedStu(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write(closedBoard);
        robot.clickOn("#userName");
        robot.write("stu");
        robot.clickOn("#joinBtn");
        // Expect that a new window is shown with the title "QuBo" (StudentView.fxml)
        FxAssert.verifyThat(robot.window("(QuBo)"), WindowMatchers.isShowing());
    }

    /**
     * Click on the join button with a correct UUID and user entered for an open meeting - moderator.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedCorrectUuidOpenMod(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write(openBoardModerator);
        robot.clickOn("#userName");
        robot.write("stu");
        robot.clickOn("#joinBtn");
        // Expect that a new window is shown with the title "QuBo - Moderator" (ModeratorView.fxml)
        FxAssert.verifyThat(robot.window("(QuBo - Moderator)"), WindowMatchers.isShowing());
    }

    /**
     * Click on the join button with a correct UUID and user entered for a closed meeting - moderator.
     *
     * @param robot TestFX robot.
     */
    @Test
    void joinButtonClickedCorrectUuidClosedMod(FxRobot robot) {
        robot.clickOn("#questionBoardCode");
        robot.write(closedBoardModerator);
        robot.clickOn("#userName");
        robot.write("stu");
        robot.clickOn("#joinBtn");
        // Expect that a new window is shown with the title "QuBo - Moderator" (ModeratorView.fxml)
        FxAssert.verifyThat(robot.window("(QuBo - Moderator)"), WindowMatchers.isShowing());
    }
}