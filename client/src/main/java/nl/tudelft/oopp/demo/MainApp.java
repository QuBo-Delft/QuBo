package nl.tudelft.oopp.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.question.QuestionCreationDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.views.QuoteDisplay;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class MainApp {

    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    /**
     * This method displays currently implemented functionality.
     * It displays the working windows and views of the client.
     * It displays the working endpoints between the server and client.
     *
     * @param args  Parameters passed by the user.
     */
    public static void main(String[] args) {
        //Display working views

        //Display a window that gives quotes when a button is clicked
        QuoteDisplay.main(new String[0]);

        //Create a QuestionBoardCreationBindingModel to create a Question Board
        QuestionBoardCreationBindingModel model = new QuestionBoardCreationBindingModel();
        model.setTitle("This is a demo Question Board");
        model.setStartTime(Timestamp.from(Instant.now()));

        //Display the behaviour of working endpoints
        showWorkingEndpoints(model);
    }

    /**
     * This method displays the working endpoints between the server and client. It logs the JSON-formatted
     *      returned DTOs to the terminal.
     *
     * @param model  An initialised QuestionBoardCreationBindingModel with which a Question Board
     *      will be created.
     */
    private static void showWorkingEndpoints(QuestionBoardCreationBindingModel model) {
        //Create the Question Board and map the returned data onto a QuestionBoardCreationDto
        QuestionBoardCreationDto questionBoard = ServerCommunication.createBoardRequest(model);

        //Print the JSON representation of questionBoard to the terminal
        System.out.println("Created a Question Board\n  QuestionBoardCreationDto:"
            + gson.toJson(questionBoard));

        //Print the JSON representation of the DTO returned by retrieveBoardDetails called using the ID of
        //questionBoard
        if (questionBoard.getId() != null) {
            System.out.println("Retrieved the Question Board details\n  through the Board ID:"
                + gson.toJson(ServerCommunication.retrieveBoardDetails(questionBoard.getId())));
        }

        //Print the JSON representation of the DTO returned by retrieveBoardDetail called using the moderator
        //code of questionBoard
        System.out.println("Retrieved the Question Board details\n  through the Moderator Code using "
            + "retrieveBoardDetails:"
            + gson.toJson(ServerCommunication.retrieveBoardDetails(questionBoard.getModeratorCode())));

        //Print the JSON representation of the dto returned by retrieveBoardDetailsThroughModCode called using
        //the moderator code of questionBoard
        System.out.println("Retrieved the Question Board details\n  through the moderator code:"
            + gson.toJson(ServerCommunication
                .retrieveBoardDetailsThroughModCode(questionBoard.getModeratorCode())));

        //Print the JSON representation of the dto returned by the addQuestion method called using the ID of the
        //ID of the questionBoard.
        String questionText = "Has this question been added successfully?";
        UUID boardId = questionBoard.getId();
        QuestionCreationDto questionCodes = ServerCommunication
            .addQuestion(boardId, questionText, "author");
        System.out.println("Added a question to the Question Board\n"
            + gson.toJson(questionCodes));
    }
}