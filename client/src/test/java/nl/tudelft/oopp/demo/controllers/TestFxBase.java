package nl.tudelft.oopp.demo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.demo.sceneloader.SceneLoader;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This is the base class used for all JavaFX testing by using the TestFX library.
 */
@ExtendWith(ApplicationExtension.class)
public abstract class TestFxBase {
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();

    /**
     * This method gets called by the CreateQuBo and JoinQuBo controller tests.
     * It takes the generated stage and a fxml sheet. With these it generates a scene which it returns.
     *
     * @param stage         test stage generated by TestFX.
     * @param fxmlSheet     fxml sheet that is to be loaded.
     * @return              current scene being displayed on the stage.
     * @throws IOException  IOException from load method.
     */
    public Scene start(Stage stage, String fxmlSheet) throws IOException {
        Parent root = fxmlLoader(fxmlSheet).load();
        return stageShower(stage, root);
    }

    /**
     * This method gets called by the QuBoCodes controller test.
     * It takes the generated stage, a fxml sheet and the creation details of a question board.
     * With these it generates a scene.
     *
     * @param stage         test stage generated by TestFX.
     * @param fxmlSheet     fxml sheet that is to be loaded.
     * @param qc            creation details of the to be shown question board.
     * @throws IOException  IOException from load method.
     */
    public void startCreation(Stage stage, String fxmlSheet, QuestionBoardCreationDto qc) throws IOException {
        FXMLLoader loadedFxml = fxmlLoader(fxmlSheet);
        Parent root = loadedFxml.load();

        // Get the controller of QuBoCodes
        QuBoCodesController controller = loadedFxml.getController();

        // Transfer the data for QuBoCodes
        controller.displayCodes(qc);

        stageShower(stage, root);
    }

    /**
     * This method gets called by the StudentView and ModeratorView controller tests.
     * It takes the generated stage, a fxml sheet and the id of a question board.
     * With these it generates a scene which it returns.
     *
     * @param stage         test stage generated by TestFX.
     * @param fxmlSheet     fxml sheet that is to be loaded.
     * @param boardId       id of the to be shown question board.
     * @return              current scene being displayed on stage
     * @throws IOException  IOException from load method.
     */
    public Scene startDetails(Stage stage, String fxmlSheet, UUID boardId) throws IOException {
        FXMLLoader loadedFxml = fxmlLoader(fxmlSheet);
        Parent root = loadedFxml.load();

        String resBody = ServerCommunication.retrieveBoardDetails(boardId);

        QuestionBoardDetailsDto qd = gson.fromJson(resBody, QuestionBoardDetailsDto.class);

        if (fxmlSheet.equals("StudentView")) {
            // Get the controller of StudentView
            StudentViewController controller = loadedFxml.getController();
        } else {
            //Get the controller of ModeratorView
            ModeratorViewController controller = loadedFxml.getController();
        }
        //TODO:currentStage.setTitle(qd.getTitle());
        return stageShower(stage, root);
    }

    /**
     * This method takes the generated scene and root. With these it creates a new scene which it displays.
     * This method is used by all start methods above.
     *
     * @param stage test stage generated by TestFX.
     * @param root  root node of the to be displayed scene
     * @return      current scene being displayed on stage
     */
    private Scene stageShower(Stage stage, Parent root) {
        stage.setScene(new Scene(root));
        stage.show();
        return stage.getScene();
    }

    /**
     * This method takes a fxml sheet, locates it and loads it. It is used by all start methods.
     *
     * @param fxmlSheet fxml sheet that is to be loaded.
     * @return          a loaded fxml sheet.
     */
    private FXMLLoader fxmlLoader(String fxmlSheet) {
        return new FXMLLoader(SceneLoader.class.getResource("/fxmlsheets/" + fxmlSheet + ".fxml"));
    }

    /**
     * This method takes the time at which to schedule a question board, schedules it
     * and returns its creation details.
     *
     * @param startTime time for which to schedule the question board.
     * @return          question board creation details.
     */
    private static QuestionBoardCreationDto quBoBuilder(Timestamp startTime) {
        QuestionBoardCreationBindingModel board = new QuestionBoardCreationBindingModel("QuBo", startTime);

        // Send the request and retrieve the string body of QuestionBoardCreationDto
        String resBody = ServerCommunication.createBoardRequest(board);

        // Convert the response body to QuestionBoardCreationDto
        return gson.fromJson(resBody, QuestionBoardCreationDto.class);
    }

    /**
     * Creates an open question board and returns it's details.
     * Primarily used for easy generation, as this method is not strictly necessary.
     *
     * @return the question board creation dto
     */
    public static QuestionBoardCreationDto createOpenQuBo() {
        Timestamp startTimeNow = Timestamp.valueOf(LocalDateTime.now());
        return quBoBuilder(startTimeNow);
    }

    /**
     * Create a closed question board and returns it's details.
     * Primarily used for easy generation, as this method is not strictly necessary.
     *
     * @return the question board creation dto
     */
    public static QuestionBoardCreationDto createClosedQuBo() {
        Timestamp startTimeTomorrow = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        return quBoBuilder(startTimeTomorrow);
    }
}
