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
import org.testfx.matcher.control.LabeledMatchers;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.UUID;

/**
 * This class tests the QuBoCodesController which controls the QuBoCodes.fxml.
 */
class QuBoCodesControllerTest extends TestFxBase {

    static {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("java.awt.headless", "true");
            System.setProperty("testfx.robot", "glass");
            System.setProperty("glass.platform", "Monocle");
            System.setProperty("monocle.platform", "Headless");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.useFontConfig", "false");
        }
    }

    /*
        These elements are used for the clipBoardTest method.
     */
    String clipboard;
    UUID clipboardUuid;

    QuestionBoardCreationDto qc = createOpenQuBo();

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     * @throws IOException IOException thrown by incorrect load in start method.
     */
    @Start
    void start(Stage stage) throws IOException {
        String fxmlSheet = "QuBoCodes";
        startCreation(stage, fxmlSheet, qc);
    }

    /**
     * Verify title is correct.
     */
    @Test
    void verifyTitle() {
        FxAssert.verifyThat("#boardTitle", LabeledMatchers.hasText("QuBo"));
    }

    /**
     * Verify startTime is correct.
     */
    @Test
    void verifyStartTime() {
        String startTimeStr = qc.getStartTime().toString();
        FxAssert.verifyThat("#startTime", LabeledMatchers.hasText(startTimeStr));
    }

    /**
     * Verify admin code is correct.
     */
    @Test
    void verifyAdmin() {
        String adminCodeStr = qc.getModeratorCode().toString();
        FxAssert.verifyThat("#adminCode", LabeledMatchers.hasText(adminCodeStr));
    }

    /**
     * Verify student code is correct.
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
        robot.clickOn("#copyStudentBtn");
        FxAssert.verifyThat("#studentCopySuccessful", Node::isVisible);
        clipboardTest();
        FxAssert.verifyThat("#adminCopySuccessful", Node::isVisible);
        clipboardTest();
    }

    /**
     * Test whether the labels are hidden on default.
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
        FxAssert.verifyThat(robot.window("(Join Question Board)"), WindowMatchers.isShowing());
    }

    /**
     * This method checks whether the clipboard actually contains the correct code copied.
     * When this method fails, an IOException or UnsupportedFlavorException shall be thrown.
     * Either of these exceptions will make the test calling this method fail automatically.
     */
    private void clipboardTest() {
        if (!Boolean.getBoolean("headless")) {
            try {
                clipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor);
                clipboardUuid = UUID.fromString(clipboard);
            } catch (IOException | UnsupportedFlavorException e) {
                e.printStackTrace();
            }
        }
    }
}