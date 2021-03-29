package nl.tudelft.oopp.demo.sceneloader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;

import java.sql.Timestamp;

public class SceneLoaderTest {
    @Test
    public void qbTest() {
        Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

        String titleStr = "title";
        Date startTime = new Date();

        Timestamp startTimeStamp = new Timestamp(startTime.getTime());
        QuestionBoardCreationBindingModel board = new QuestionBoardCreationBindingModel(
            titleStr, startTimeStamp);

        // Send the request and retrieve the string body of QuestionBoardCreationDto
        String resBody = ServerCommunication.createBoardRequest(board);

        // Convert the response body to QuestionBoardCreationDto
        QuestionBoardCreationDto questionBoardDto = gson.fromJson(resBody, QuestionBoardCreationDto.class);

        ServerCommunication.addQuestion(questionBoardDto.getId(), "What is life?", "monkey");
    }
}
