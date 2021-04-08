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
class StudentViewControllerClosedTest extends TestFxBase {

    /*
        Create an open Question Board which is loaded in @Start.
     */
    QuestionBoardDetailsDto qdClosed = createClosedQuBo();

    /**
     * Initiate testing done through the TestFX library.
     *
     * @param stage Test stage created by the TestFX library.
     */
    @Start
    void start(Stage stage) {
        // A scheduled Question Board is loaded
        new SceneLoader().viewLoader(qdClosed, stage, "author", "StudentView", qdClosed.getId());
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


}