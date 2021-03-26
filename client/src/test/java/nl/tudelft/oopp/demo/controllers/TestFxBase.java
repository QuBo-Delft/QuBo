package nl.tudelft.oopp.demo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.demo.views.AlertDialog;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(ApplicationExtension.class)
public abstract class TestFxBase {

    public Scene start(Stage stage, String fxmlSheet) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        // By giving the resource path, it is able to display a specific fxml file
        URL xmlUrl = getClass().getResource("/fxmlsheets/" + fxmlSheet + ".fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.show();

        return stage.getScene();
    }

    private static QuestionBoardCreationDto QuBoBuilder(Timestamp startTime) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();

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
