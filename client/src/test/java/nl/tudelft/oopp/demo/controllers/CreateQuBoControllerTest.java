package nl.tudelft.oopp.demo.controllers;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This class tests the CreateQuBoController which controls the CreateQuBo.fxml.
 */
@SuppressWarnings("ALL")
class CreateQuBoControllerTest extends TestFxBase {

    /*
        These dates and objects are used for various tests. The objects are instantiated in @Start.
     */
    LocalDate yesterday = LocalDate.now().minusDays(1);
    LocalDate today = LocalDate.now();
    LocalDate tomorrow = LocalDate.now().plusDays(1);

    Spinner hourSpinner;
    Spinner minSpinner;
    DatePicker picker;

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     * @throws IOException Thrown by incorrect load in start method.
     */
    @Start
    void start(Stage stage) throws IOException {
        String fxmlSheet = "CreateQuBo";
        Scene scene = start(stage, fxmlSheet);

        hourSpinner = (Spinner) scene.lookup("#hoursSpinner");
        minSpinner = (Spinner) scene.lookup("#minutesSpinner");
        picker = (DatePicker) scene.lookup("#startDate");
    }

    /**
     * Click create now button while null title.
     *
     * @param robot TestFX robot.
     */
    @Test
    void createNowBtnClickedNullTitle(FxRobot robot) {
        robot.clickOn("#createBtn");
        FxAssert.verifyThat("#errorTitle", Node::isVisible);
        FxAssert.verifyThat("#errorDateTime", NodeMatchers.isInvisible());
    }

    /**
     * Click create now button while empty title.
     *
     * @param robot TestFX robot.
     */
    @Test
    void createNowBtnClickedEmptyTitle(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("");
        robot.clickOn("#createBtn");
        FxAssert.verifyThat("#errorTitle", Node::isVisible);
        FxAssert.verifyThat("#errorDateTime", NodeMatchers.isInvisible());
    }

    /**
     * Click create now button while title has literal value null.
     *
     * @param robot TestFX robot.
     */
    @Test
    void createNowBtnClickedTitleLiteralNull(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("null");
        robot.clickOn("#createBtn");
        FxAssert.verifyThat(robot.window("(Created Question Board)"), WindowMatchers.isShowing());
    }

    /**
     * Click cancel button and check whether home screen is shown.
     *
     * @param robot TestFX robot.
     */
    @Test
    void cancelBtnClick(FxRobot robot) {
        robot.clickOn("#cancelBtn");
        FxAssert.verifyThat(robot.window("(Join Question Board)"), WindowMatchers.isShowing());
    }

    /**
     * Click create now button while title has proper name.
     *
     * @param robot TestFX robot.
     */
    @Test
    void createNowBtnClickedTitleInserted(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#createBtn");
        FxAssert.verifyThat(robot.window("(Created Question Board)"), WindowMatchers.isShowing());
    }

    /**
     * Click schedule button while null title and no date and time set.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedNullTitle(FxRobot robot) {
        robot.clickOn("#scheduleBtn");
        FxAssert.verifyThat("#errorTitle", Node::isVisible);
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
    }

    /**
     * Click schedule button while empty title and no date and time set.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedEmptyTitle(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("");
        robot.clickOn("#scheduleBtn");
        FxAssert.verifyThat("#errorTitle", Node::isVisible);
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
    }

    /**
     * Click schedule button while title has literal value null and no date and time set.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedTitleLiteralNull(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("null");
        robot.clickOn("#scheduleBtn");
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
        FxAssert.verifyThat("#errorTitle", NodeMatchers.isInvisible());
    }

    /**
     * Click the schedule button while title has proper value and no date and time set.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedOnlyTitleInserted(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#scheduleBtn");
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
        FxAssert.verifyThat("#errorTitle", NodeMatchers.isInvisible());
    }

    /*
        The tests regarding clicking the schedule a QuBo button below will have titles
        properly inserted, as the title input functionality is tested above and on all tests above
        can be deemed as correct and working.
     */

    /**
     * Click the schedule button with proper title but date before today.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedDateBeforeToday(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#startDate");
        picker.setValue(yesterday);
        robot.clickOn("#scheduleBtn");
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
        FxAssert.verifyThat("#errorTitle", NodeMatchers.isInvisible());
    }

    /**
     * Click the schedule button with proper title, date and min, but incorrect hour.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedDateTodayTimeHourBefore(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#startDate");
        picker.setValue(today);
        int hourBefore = LocalDateTime.now().minusHours(1).getHour();
        hourSpinner.getValueFactory().setValue(hourBefore);
        robot.clickOn("#scheduleBtn");
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
        FxAssert.verifyThat("#errorTitle", NodeMatchers.isInvisible());
    }

    /**
     * Click the schedule button with proper title, date and hour, but incorrect minute.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedDateTodayTimeMinBefore(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#startDate");
        picker.setValue(today);
        int hourCurrent = LocalDateTime.now().getHour();
        int minBefore = LocalDateTime.now().minusMinutes(5).getMinute();
        hourSpinner.getValueFactory().setValue(hourCurrent);
        minSpinner.getValueFactory().setValue(minBefore);
        robot.clickOn("#scheduleBtn");
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
        FxAssert.verifyThat("#errorTitle", NodeMatchers.isInvisible());
    }

    /**
     * Click the schedule button with proper title, date set to tomorrow.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedDateTomorrowTimeDefault(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#startDate");
        picker.setValue(tomorrow);
        robot.press(KeyCode.ENTER);
        robot.clickOn("#scheduleBtn");
        FxAssert.verifyThat(robot.window("(Created Question Board)"), WindowMatchers.isShowing());
    }

    /**
     * Click the schedule button with proper title, date, hour and minute set to current.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedDateTodayTimeCurrent(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#startDate");
        picker.setValue(today);
        int hourCurrent = LocalDateTime.now().getHour();
        int minCurrent = LocalDateTime.now().plusMinutes(1).getMinute();
        hourSpinner.getValueFactory().setValue(hourCurrent);
        minSpinner.getValueFactory().setValue(minCurrent);
        robot.clickOn("#scheduleBtn");
        FxAssert.verifyThat(robot.window("Created Question Board"), WindowMatchers.isShowing());
    }
}