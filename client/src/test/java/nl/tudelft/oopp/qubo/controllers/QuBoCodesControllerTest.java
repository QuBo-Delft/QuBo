package nl.tudelft.oopp.qubo.controllers;

import javafx.scene.Node;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.LabeledMatchers;

/**
 * This class tests the QuBoCodesController which controls QuBoCodes.fxml.
 */
class QuBoCodesControllerTest extends TestFxBase {

    /*
        Retrieve the QuestionBoardCreationDto of a new Question Board.
     */
    QuestionBoardCreationDto qc = createQuBoCreation();

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     */
    @Start
    void start(Stage stage) {
        SceneLoader.loadQuBoCodes(qc, stage);
    }

    /**
     * Verify the title.
     */
    @Test
    void verifyTitle() {
        FxAssert.verifyThat("#boardTitle", LabeledMatchers.hasText("QuBo"));
    }

    /**
     * Verify the startTime.
     */
    @Test
    void verifyStartTime() {
        String startTimeStr = qc.getStartTime().toString();
        FxAssert.verifyThat("#startTime", LabeledMatchers.hasText(startTimeStr));
    }

    /**
     * Verify the admin code.
     */
    @Test
    void verifyAdmin() {
        String adminCodeStr = qc.getModeratorCode().toString();
        FxAssert.verifyThat("#adminCode", LabeledMatchers.hasText(adminCodeStr));
    }

    /**
     * Verify student code.
     */
    @Test
    void verifyStudent() {
        String studentCodeStr = qc.getId().toString();
        FxAssert.verifyThat("#studentCode", LabeledMatchers.hasText(studentCodeStr));
    }

    /**
     * Test whether only the student success label shows when only the student code is copied.
     *
     * @param robot TestFX robot.
     */
    @Test
    void copyOnlyStudentCode(FxRobot robot) {
        robot.clickOn("#copyStudentBtn");
        FxAssert.verifyThat("#studentCopySuccessful", Node::isVisible);
        FxAssert.verifyThat("#adminCopySuccessful", NodeMatchers.isInvisible());
        clipboardTest();
    }

    /**
     * Test whether only the admin success label shows when only the admin code is copied.
     *
     * @param robot TestFX robot.
     */
    @Test
    void copyOnlyAdminCode(FxRobot robot) {
        robot.clickOn("#copyAdminBtn");
        FxAssert.verifyThat("#adminCopySuccessful", Node::isVisible);
        FxAssert.verifyThat("#studentCopySuccessful", NodeMatchers.isInvisible());
        clipboardTest();
    }

    /**
     * Test whether both labels show when both codes are successfully copied.
     *
     * @param robot TestFX robot.
     */
    @Test
    void copyBothCodes(FxRobot robot) {
        robot.clickOn("#copyAdminBtn");
        FxAssert.verifyThat("#adminCopySuccessful", Node::isVisible);
        clipboardTest();
        robot.clickOn("#copyStudentBtn");
        FxAssert.verifyThat("#studentCopySuccessful", Node::isVisible);
        clipboardTest();
    }

    /**
     * Test whether the labels are hidden by default.
     */
    @Test
    void copyNoCodes() {
        FxAssert.verifyThat("#studentCopySuccessful", NodeMatchers.isInvisible());
        FxAssert.verifyThat("#adminCopySuccessful", NodeMatchers.isInvisible());
    }

    /**
     * Test whether the the back to home button works and directs to the correct scene.
     *
     * @param robot TestFX robot.
     */
    @Test
    void backToHome(FxRobot robot) {
        robot.clickOn("#backToHome");
        FxAssert.verifyThat(robot.window("(QuBo)"), WindowMatchers.isShowing());
    }
}
