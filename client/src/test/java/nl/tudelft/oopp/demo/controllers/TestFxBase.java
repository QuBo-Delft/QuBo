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
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(ApplicationExtension.class)
public abstract class TestFxBase {
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();

    public Scene preStart(Stage stage, String fxmlSheet) throws IOException {
        FXMLLoader loadedFxml = loader(fxmlSheet);
        Parent root = loadedFxml.load();
        return start(stage, root);
    }

    public Scene start(Stage stage, Parent root) {
        stage.setScene(new Scene(root));
        stage.show();

        return stage.getScene();
    }

    public Scene startCreation(Stage stage, String fxmlSheet, QuestionBoardCreationDto qc) throws IOException {
        FXMLLoader loadedFxml = loader(fxmlSheet);
        Parent root = loadedFxml.load();

        // Get the controller of QuBoCodes
        QuBoCodesController controller = loadedFxml.getController();

        // Transfer the data for QuBoCodes
        controller.displayCodes(qc);

        return start(stage, root);
    }

    public Scene startDetails(Stage stage, String fxmlSheet, UUID boardIdT) throws IOException {
        FXMLLoader loadedFxml = loader(fxmlSheet);
        Parent root = loadedFxml.load();

        String resBody = ServerCommunication.retrieveBoardDetails(boardIdT);

        QuestionBoardDetailsDto qd = gson.fromJson(resBody, QuestionBoardDetailsDto.class);

        if (fxmlSheet.equals("StudentView")) {
            // Get the controller of StudentView
            StudentViewController controller = loadedFxml.getController();
        } else {
            //Get the controller of ModeratorView
            ModeratorViewController controller = loadedFxml.getController();
        }

        UUID boardId = qd.getId();
        //TODO:currentStage.setTitle(qd.getTitle());
        return start(stage, root);
    }

    public FXMLLoader loader(String fxmlSheet) {
        FXMLLoader loader = new FXMLLoader();
        // By giving the resource path, it is able to display a specific fxml file
        URL xmlUrl = getClass().getResource("/fxmlsheets/" + fxmlSheet + ".fxml");
        loader.setLocation(xmlUrl);
        return loader;
    }

    private static QuestionBoardCreationDto QuBoBuilder(Timestamp startTime) {
        QuestionBoardCreationBindingModel board = new QuestionBoardCreationBindingModel("QuBo", startTime);

        // Send the request and retrieve the string body of QuestionBoardCreationDto
        String resBody = ServerCommunication.createBoardRequest(board);

        // Convert the response body to QuestionBoardCreationDto
        return gson.fromJson(resBody, QuestionBoardCreationDto.class);
    }

    public static QuestionBoardCreationDto createOpenQuBo() {
        Timestamp startTimeNow = Timestamp.valueOf(LocalDateTime.now());
        return QuBoBuilder(startTimeNow);
    }

    public static QuestionBoardCreationDto createClosedQuBo() {
        Timestamp startTimeTomorrow = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        return QuBoBuilder(startTimeTomorrow);
    }
}
