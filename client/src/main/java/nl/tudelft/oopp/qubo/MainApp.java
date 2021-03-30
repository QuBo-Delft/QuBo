package nl.tudelft.oopp.qubo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.qubo.communication.ServerCommunication;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionCreationDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteCreationDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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

        //Display the application homepage
        SceneDisplay.main(new String[0]);

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
        String resBody = ServerCommunication.createBoardRequest(model);
        QuestionBoardCreationDto questionBoard = gson.fromJson(resBody, QuestionBoardCreationDto.class);

        UUID boardId = questionBoard.getId();
        UUID moderatorCode = questionBoard.getModeratorCode();

        //Print the JSON representation of questionBoard to the terminal
        System.out.println("Created a Question Board\n  QuestionBoardCreationDto:"
            + gson.toJson(questionBoard));

        //Print the JSON representation of the DTO returned by retrieveBoardDetails called using the ID of
        //questionBoard
        System.out.println("Retrieved the Question Board details\n  through the Board ID:"
            + gson.toJson(ServerCommunication.retrieveBoardDetails(boardId)));

        //Print the JSON representation of the DTO returned by retrieveBoardDetails called using the moderator
        //code of questionBoard
        System.out.println("Retrieved the Question Board details\n  through the Moderator Code using "
            + "retrieveBoardDetails:"
            + gson.toJson(ServerCommunication.retrieveBoardDetails(moderatorCode)));

        //Print the JSON representation of the dto returned by retrieveBoardDetailsThroughModCode called using
        //the moderator code of questionBoard
        System.out.println("Retrieved the Question Board details\n  through the moderator code:"
            + gson.toJson(ServerCommunication
                .retrieveBoardDetailsThroughModCode(moderatorCode)));

        //Print the JSON representation of the dto returned by the addQuestion method called using the
        //ID of the questionBoard.
        String questionText = "Has this question been added successfully?";

        //QuestionCreationDto questionCodes = ServerCommunication.addQuestion(boardId, questionText, "author");
        QuestionCreationDto questionCodes = gson.fromJson(ServerCommunication.addQuestion(boardId,
                questionText, "author"), QuestionCreationDto.class);

        System.out.println("Added a question to the Question Board\n    "
            + gson.toJson(questionCodes));

        //Edit the question that was just created through the question secret code, and print true if it was
        //edited successfully.
        UUID questionId = questionCodes.getId();
        UUID secretCode = questionCodes.getSecretCode();
        System.out.println("The question has been edited: " + ((ServerCommunication
            .editQuestion(questionId, secretCode, "What is life?")) != null));

        //Edit the question through the moderator code, and print true if it was edited successfully.
        System.out.println("The question has been edited through the moderator code: "
            + (((ServerCommunication
                .editQuestion(questionId, moderatorCode, "Is the universe infinitely large?"))) != null));

        //Add another question
        QuestionCreationDto questionTwo = gson.fromJson(ServerCommunication
                .addQuestion(boardId, questionText, "author"), QuestionCreationDto.class);

        System.out.println("Added a question to the Question Board\n    "
            + gson.toJson(questionTwo));

        //Retrieve the questions associated with the question board and log them to the console
        QuestionDetailsDto[] questionList = gson.fromJson(ServerCommunication
                .retrieveQuestions(boardId), QuestionDetailsDto[].class);


        //QuestionDetailsDto[] questionArray = gson.fromJson(response.body(), QuestionDetailsDto[].class);

        System.out.print("The questions in this question board are:\n");
        for (QuestionDetailsDto question : questionList) {
            System.out.print("    " + gson.toJson(question) + "\n");
        }

        //Add a vote to questionCodes
        QuestionVoteCreationDto questionVote = gson.fromJson(ServerCommunication
                .addQuestionVote(questionId), QuestionVoteCreationDto.class);

        System.out.println("A vote has been added to the question\n    " + gson.toJson(questionVote));

        //Mark questionTwo as answered
        UUID questionTwoId = questionTwo.getId();
        System.out.println("The fact that questionTwo has been marked as answered is: "
                + ((ServerCommunication.markQuestionAsAnswered(questionTwoId, moderatorCode)) != null));

        //Delete the vote that was just created
        UUID voteId = questionVote.getId();
        System.out.println("The vote has been deleted: " + ((ServerCommunication
            .deleteQuestionVote(questionId, voteId)) != null));

        //Delete questionCodes from the question board and print true if the question was deleted successfully
        System.out.println("The question has been deleted: " + ((ServerCommunication
            .deleteQuestion(questionId, secretCode)) != null));

        //Delete questionTwo through the moderator code of the QuestionBoard and print true if the question was
        //deleted successfully
        System.out.println("The question has been deleted: " + ((ServerCommunication
            .deleteQuestion(questionTwoId, moderatorCode)) != null));

        //Add a pace vote to the question board
        PaceType paceType = PaceType.JUST_RIGHT;
        //PaceVoteCreationDto paceVote = ServerCommunication.addPaceVote(boardId, paceType);
        PaceVoteCreationDto paceVote = gson.fromJson(ServerCommunication
                .addPaceVote(boardId, paceType), PaceVoteCreationDto.class);

        System.out.println("The pace vote has been added\n " + gson.toJson(paceVote));

        //Delete a pace vote from the question board
        UUID paceVoteId = paceVote.getId();
        System.out.println("The pace vote has been deleted: " + ((ServerCommunication
            .deletePaceVote(boardId, paceVoteId)) != null));

        //Add a poll to the question board
        Set<String> pollOptions = new HashSet<>(2);
        pollOptions.add("Option A");
        pollOptions.add("Option B");
        System.out.println("This poll has been added: "
                + (gson.toJson(ServerCommunication.addPoll(boardId, moderatorCode, "Test Poll", pollOptions))));

        //Delete the poll that was added
        System.out.println("This poll has been deleted: " + ServerCommunication
                .deletePoll(boardId, moderatorCode));

        //Close the question board
        System.out.println("The fact that this question board has been closed is: "
                + ((ServerCommunication.closeBoardRequest(boardId, moderatorCode)) != null));
    }
}