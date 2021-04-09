package nl.tudelft.oopp.qubo.controllers;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
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
    private static final LocalDate yesterday = LocalDate.now().minusDays(1);
    private static final LocalDate today = LocalDate.now();
    private static final LocalDate tomorrow = LocalDate.now().plusDays(1);

    private static Spinner hourSpinner;
    private static Spinner minSpinner;
    private static DatePicker picker;

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     * @throws IOException Thrown by incorrect load in start method.
     */
    @Start
    void start(Stage stage) {
        SceneLoader.defaultLoader(stage, "CreateQuBo");
    }

    /**
     * Click create now button while null title.
     *
     * @param robot TestFX robot.
     */
    @Test
    void createNowBtnClickedNullTitle(FxRobot robot) {
        robot.clickOn("#createBtn");
        // Expect that the an error related to the title is shown
        FxAssert.verifyThat("#errorTitle", Node::isVisible);
        // Expect that the an error related to the date and time is NOT shown
        FxAssert.verifyThat("#errorDateTime", NodeMatchers.isInvisible());
    }

    /**
     * Click the create now button while the title field is empty.
     *
     * @param robot TestFX robot.
     */
    @Test
    void createNowBtnClickedEmptyTitle(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("");
        robot.clickOn("#createBtn");
        // Expect that an error related to the title is shown
        FxAssert.verifyThat("#errorTitle", Node::isVisible);
        // Expect that an error related to the date and time is NOT shown
        FxAssert.verifyThat("#errorDateTime", NodeMatchers.isInvisible());
    }

    /**
     * Click the create now button while the title has the literal value null.
     *
     * @param robot TestFX robot.
     */
    @Test
    void createNowBtnClickedTitleLiteralNull(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("null");
        robot.clickOn("#createBtn");
        // Expect that a new window is shown with the title "Created Question Board" (QuBoCodes.fxml)
        FxAssert.verifyThat(robot.window("(Created Question Board)"), WindowMatchers.isShowing());
    }

    /**
     * Click the cancel button and check whether the home screen is shown.
     *
     * @param robot TestFX robot.
     */
    @Test
    void cancelBtnClick(FxRobot robot) {
        robot.clickOn("#cancelBtn");
        // Expect that a new window is shown with the title "QuBo" (JoinQuBo.fxml)
        FxAssert.verifyThat(robot.window("(QuBo)"), WindowMatchers.isShowing());
    }

    /**
     * Click the create now button while the title has proper name.
     *
     * @param robot TestFX robot.
     */
    @Test
    void createNowBtnClickedTitleInserted(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#createBtn");
        // Expect that a new window is shown with the title "Created Question Board" (QuBoCodes.fxml)
        FxAssert.verifyThat(robot.window("(Created Question Board)"), WindowMatchers.isShowing());
    }

    /**
     * Click the schedule button while the title is null and no date or time has been set either.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedNullTitle(FxRobot robot) {
        robot.clickOn("#scheduleBtn");
        // Expect that an error related to the title is shown
        FxAssert.verifyThat("#errorTitle", Node::isVisible);
        // Expect that an error related to the date and time is shown
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
    }

    /**
     * Click the schedule button while the title is empty and no date and time is set.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedEmptyTitle(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("");
        robot.clickOn("#scheduleBtn");
        // Expect that an error related to the title is shown
        FxAssert.verifyThat("#errorTitle", Node::isVisible);
        // Expect that an error related to the date and time is shown
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
    }

    /**
     * Click the schedule button while the title has the literal value null and no date and time are set.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedTitleLiteralNull(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("null");
        robot.clickOn("#scheduleBtn");
        // Expect that an error related to the date and time is shown
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
        // Expect that an error related to the title is NOT shown
        FxAssert.verifyThat("#errorTitle", NodeMatchers.isInvisible());
    }

    /**
     * Click the schedule button while the title has a proper value and no date and time are set.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedOnlyTitleInserted(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#scheduleBtn");
        // Expect that an error related to the date and time is shown
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
        // Expect that an error related to the title is NOT shown
        FxAssert.verifyThat("#errorTitle", NodeMatchers.isInvisible());
    }

    /*
        The tests regarding clicking the schedule a QuBo button below will have titles
        properly inserted, as the title input functionality is tested above and if all tests pass above
        can be deemed as correct and working.
     */

    /**
     * Click the schedule button with a proper title but a date before today.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedDateBeforeToday(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#startDate");
        picker = robot.lookup("#startDate").queryAs(DatePicker.class);
        picker.setValue(yesterday);
        robot.clickOn("#scheduleBtn");
        // Expect that an error related to the date and time is shown
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
        // Expect that an error related to the title is NOT shown
        FxAssert.verifyThat("#errorTitle", NodeMatchers.isInvisible());
    }

    /**
     * Click the schedule button with a proper title, date and min, but an incorrect hour.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedDateTodayTimeHourBefore(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#startDate");
        picker = robot.lookup("#startDate").queryAs(DatePicker.class);
        picker.setValue(today);
        int hourBefore = LocalDateTime.now().minusHours(1).getHour();
        hourSpinner = robot.lookup("#hoursSpinner").queryAs(Spinner.class);
        hourSpinner.getValueFactory().setValue(hourBefore);
        robot.clickOn("#scheduleBtn");
        // Occurs when the hour is set back from 00:xx to 23:xx, if so the time is correct and
        // we expect no error to be shown
        if (hourBefore > LocalDateTime.now().getHour()) {
            // Expect that a new window is shown with the title "Created Question Board" (QuBoCodes.fxml)
            FxAssert.verifyThat(robot.window("(Created Question Board)"), WindowMatchers.isShowing());
        } else {
            // Expect that an error related to the date and time is shown
            FxAssert.verifyThat("#errorDateTime", Node::isVisible);
            // Expect that an error related to the title is NOT shown
            FxAssert.verifyThat("#errorTitle", NodeMatchers.isInvisible());
        }
    }

    /**
     * Click the schedule button with a proper title, date and hour, but an incorrect minute.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedDateTodayTimeMinBefore(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#startDate");
        picker = robot.lookup("#startDate").queryAs(DatePicker.class);
        picker.setValue(today);
        int hourCurrent = LocalDateTime.now().getHour();
        hourSpinner = robot.lookup("#hoursSpinner").queryAs(Spinner.class);
        hourSpinner.getValueFactory().setValue(hourCurrent);
        int minBefore = LocalDateTime.now().minusMinutes(5).getMinute();
        minSpinner = robot.lookup("#minutesSpinner").queryAs(Spinner.class);
        minSpinner.getValueFactory().setValue(minBefore);
        robot.clickOn("#scheduleBtn");
        // Expect that an error related to the date and time is shown
        FxAssert.verifyThat("#errorDateTime", Node::isVisible);
        // Expect that an error related to the title is NOT shown
        FxAssert.verifyThat("#errorTitle", NodeMatchers.isInvisible());
    }

    /**
     * Click the schedule button with a proper title and date set to tomorrow.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedDateTomorrowTimeDefault(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#startDate");
        picker = robot.lookup("#startDate").queryAs(DatePicker.class);
        picker.setValue(tomorrow);
        robot.press(KeyCode.ENTER);
        robot.clickOn("#scheduleBtn");
        // Expect that a new window is shown with the title "Created Question Board" (QuBoCodes.fxml)
        FxAssert.verifyThat(robot.window("(Created Question Board)"), WindowMatchers.isShowing());
    }

    /**
     * Click the schedule button with a proper title and date, hour and minute set to current.
     *
     * @param robot TestFX robot.
     */
    @Test
    void scheduleBtnClickedDateTodayTimeCurrent(FxRobot robot) {
        robot.clickOn("#title");
        robot.write("QuBo");
        robot.clickOn("#startDate");
        picker = robot.lookup("#startDate").queryAs(DatePicker.class);
        picker.setValue(today);
        int hourCurrent = LocalDateTime.now().getHour();
        hourSpinner = robot.lookup("#hoursSpinner").queryAs(Spinner.class);
        hourSpinner.getValueFactory().setValue(hourCurrent);
        int minCurrent = LocalDateTime.now().plusMinutes(1).getMinute();
        minSpinner = robot.lookup("#minutesSpinner").queryAs(Spinner.class);
        minSpinner.getValueFactory().setValue(minCurrent);
        robot.clickOn("#scheduleBtn");
        // Expect that a new window is shown with the title "Created Question Board" (QuBoCodes.fxml)
        FxAssert.verifyThat(robot.window("Created Question Board"), WindowMatchers.isShowing());
    }
}
