package nl.tudelft.oopp.qubo.controllers;

import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

/**
 * This class tests the StudentViewController which controls StudentView.fxml.
 */
class StudentViewControllerTest extends TestFxBase {

    /*
        These dto's are used in various tests as well as in @Start.
     */
    QuestionBoardDetailsDto qcOpen = createOpenQuBo();
    QuestionBoardDetailsDto qcClosed = createClosedQuBo();

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     * @throws IOException Thrown by incorrect load in start method.
     */
    @Start
    void start(Stage stage) throws IOException {
        new SceneLoader().viewLoader(qcOpen, stage, "", "StudentView", qcOpen.getId());
    }

    /**
     * Show hide side screen.
     */
    @Test
    void showHideSideScreen() {
    }

    /**
     * Show hide ans questions.
     */
    @Test
    void showHideAnsQuestions() {
    }

    /**
     * Show ans questions.
     */
    @Test
    void showAnsQuestions() {
    }

    /**
     * Show hide polls.
     */
    @Test
    void showHidePolls() {
    }

    /**
     * Show polls.
     */
    @Test
    void showPolls() {
    }
}