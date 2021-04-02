package nl.tudelft.oopp.qubo.communication;

import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

/**
 * This class hold methods related to communication for QuestionBoards.
 */
public class QuestionBoardCommunication {
    //To be added:
    // - createBoardRequest()
    // - closeBoardRequest()
    /**
     * The method sends a request to the server to create a question board.
     *
     * @param board     The QuestionBoardCreationBindingModel object that contains details of a question board.
     * @return The QuestionBoardDetailsDto of the created board in JSON String format.
     */
    public static String createBoardRequest(QuestionBoardCreationBindingModel board) {
        String fullUrl = ServerCommunication.subUrl + "board";

        //Convert the QuestionBoardCreationBindingModel to JSON
        String requestBody = ServerCommunication.gson.toJson(board);

        //Send the post request and retrieve the response from the server
        HttpResponse<String> res = ServerCommunication.post(fullUrl, requestBody, "Content-Type",
                "application/json;charset=UTF-8");

        //If the request was unsuccessful, return null
        if (res == null || res.statusCode() != 200) {
            return null;
        }

        return res.body();
    }

    /**
     * Retrieves the board details of the Question Board associated with the specified moderator code from
     *      the server.
     * Communicates with the /api/board/moderator?code={moderatorCode} server endpoint.
     *
     * @param moderatorCode     The code belonging to the Question Board whose details should be retrieved.
     * @return The QuestionBoardDetailsDto in JSON String format containing the details of the Question Board
     *         or null if this does not exist.
     */
    public static String retrieveBoardDetailsThroughModCode(UUID moderatorCode) {
        //Create a request and response object, send the request, and retrieve the response
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(ServerCommunication.subUrl + "board/moderator?code=" + moderatorCode))
                .build();
        HttpResponse<String> response = ServerCommunication.sendRequest(request);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        //Convert the JSON response to a UUID and return this
        return response.body();
    }

    /**
     * Retrieves the details of the Question Board with specified board ID from the server.
     * Communicates with the /api/board/{boardID} server endpoint.
     *
     * @param boardID   The ID of the Question Board whose details should be retrieved.
     * @return The QuestionBoardDetailsDto in JSON String format containing the details of the Question
     *         Board or null if this does not exist.
     */
    public static String retrieveBoardDetails(UUID boardID) {
        //Create a request and response object, send the request, and retrieve the response
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(ServerCommunication.subUrl + "board/" + boardID))
                .build();
        HttpResponse<String> response = ServerCommunication.sendRequest(request);

        //Check if the response object is null in which case null is returned
        if (response == null) {
            return null;
        }
        //Check if it is a moderator code instead of a board ID and retrieve the details of the
        //corresponding Question Board. Return null if the code does not exist or if the response
        //did not have status code 200.
        if (response.statusCode() == 404) {
            return retrieveBoardDetailsThroughModCode(boardID);
        } else if (response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Retrieves the question list of the specified board.
     * Communicates with the /api/board/{boardid}/questions server endpoint.
     *
     * @param boardId   The ID of the Question Board whose question list should be retrieved.
     * @return An array of QuestionDetailsDtos in JSON String format.
     */
    public static String retrieveQuestions(UUID boardId) {
        //Send the request to retrieve the questions of the question board, and retrieve the response
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(ServerCommunication.subUrl + "board/" + boardId + "/questions"))
                .build();
        HttpResponse<String> response = ServerCommunication.sendRequest(request);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

}