package nl.tudelft.oopp.qubo.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the StudentViewController which controls StudentView.fxml.
 */
class StudentViewControllerClosedTest extends TestFxBase {

    /*
        Create an open Question Board which is loaded in @Start.
     */
    QuestionBoardDetailsDto qdClosed;

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     */
    @Start
    void start(Stage stage) throws IOException {
        qdClosed = createOpenQuBo();
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/fxmlsheets/StudentView.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("QuBo");
        stage.show();

        StudentViewController stuVie = loader.getController();

        stuVie.setQuBo(qdClosed);
        stuVie.setAuthorName("QuBo");
        stuVie.setBoardDetails();
        stuVie.refresh();
    }
    //    askQuestionBtn
    //    boardInfo
    //        helpDoc
    //    hamburger
    //            ansQuestions
    //    polls
    //                boardTitle
    //    boardStatusText
    //                    boardStatusIcon
    //    leaveQuBo
    //                        tooSlow
    //    justRight
    //                            TooFast
    
    /**
     * Test whether the proper open icon is shown for an open question board.
     *
     * @param robot TestFX robot.
     */
    @Test
    void verifyBoardStatusIconScheduled(FxRobot robot) {
        ImageView view = robot.lookup("#boardStatusIcon").queryAs(ImageView.class);
        // Expect the icon to be the scheduled variant, meaning that
        // the image URL should end with status_scheduled.png
        assertTrue(view.getImage().getUrl().endsWith("status_scheduled.png"));
    }

    /**
     * Test whether the proper open icon is shown for an open question board.
     *
     * @param robot TestFX robot.
     */

    @Test
    void verifyBoardStatusIconClosed(FxRobot robot) {
        ImageView view = robot.lookup("#boardStatusIcon").queryAs(ImageView.class);
        // Expect the icon to be the open variant, meaning that
        // the image URL should end with status_closed.png
        assertTrue(view.getImage().getUrl().endsWith("status_closed.png"));
    }


}