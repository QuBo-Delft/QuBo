package nl.tudelft.oopp.demo.controllers;

import javafx.scene.Node;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;

import java.io.IOException;

class QuBoCodesControllerTest extends TestFxBase{

    /*
        These elements are used for @Test, @BeforeAll or @Start
     */
    QuestionBoardCreationDto qc = createOpenQuBo();

    //Initiate testing done through the TestFX library
    @Start
    void start(Stage stage) throws IOException {
        String fxmlSheet = "QuBoCodes";
        startCreation(stage, fxmlSheet, qc);
    }

    @Test
    void displayCodes() {
    }

    // Test whether only the student success label shows when only the student code is copied
    @Test
    void copyOnlyStudentCode(FxRobot robot) {
        robot.clickOn("#copyStudentBtn");
        FxAssert.verifyThat("#studentCopySuccessful", Node::isVisible);
        FxAssert.verifyThat("#adminCopySuccessful", NodeMatchers.isInvisible());
    }

    // Test whether only the admin success label shows when only the admin code is copied
    @Test
    void copyOnlyAdminCode(FxRobot robot) {
        robot.clickOn("#copyAdminBtn");
        FxAssert.verifyThat("#adminCopySuccessful", Node::isVisible);
        FxAssert.verifyThat("#studentCopySuccessful", NodeMatchers.isInvisible());
    }

    // Test whether both labels show when both codes are successfully copied
    @Test
    void copyBothCodes(FxRobot robot) {
        robot.clickOn("#copyAdminBtn");
        robot.clickOn("#copyStudentBtn");
        FxAssert.verifyThat("#studentCopySuccessful", Node::isVisible);
        FxAssert.verifyThat("#adminCopySuccessful", Node::isVisible);
    }

    // Test whether the labels are hidden on default
    @Test
    void copyNoCodes() {
        FxAssert.verifyThat("#studentCopySuccessful", NodeMatchers.isInvisible());
        FxAssert.verifyThat("#adminCopySuccessful", NodeMatchers.isInvisible());
    }

    // Test whether the the back to home button works and directs to the correct scene
    @Test
    void backToHome(FxRobot robot) {
        robot.clickOn("#backToHome");
        FxAssert.verifyThat(robot.window("(Join Question Board)"), WindowMatchers.isShowing());
    }
}