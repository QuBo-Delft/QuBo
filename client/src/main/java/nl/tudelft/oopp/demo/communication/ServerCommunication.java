package nl.tudelft.oopp.demo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class ServerCommunication {

    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final String subUrl = "http://localhost:8080/";
    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    /**
     * Retrieves an http response from the server by sending an http request.
     *
     * @param request       The http request to be sent to be server.
     * @return The http response returned.
     */
    private static HttpResponse<String> sendRequest(HttpRequest request) {
        //Instantiate a response object
        HttpResponse<String> response = null;

        //Send the request to the server and retrieve the response
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Retrieves an http response from the server by sending an http post request.
     *
     * @param fullUrl         The full url of the request.
     * @param requestBody     The request body of JSON form.
     * @param headers         The http headers of the request.
     * @return The http response returned.
     */
    private static HttpResponse<String> post(String fullUrl, String requestBody, String... headers) {
        //Set up the request Object
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(fullUrl))
                .headers(headers)
                .build();

        //Send the request, and retrieve and return the response from the server
        return sendRequest(request);
    }

    /**
     * The method sends a request to the server to create a question board.
     *
     * @param board     The QuestionBoardCreationBindingModel object that contains details of a question board.
     * @return The http response returned.
     */
    public static HttpResponse<String> createBoardRequest(QuestionBoardCreationBindingModel board) {
        String fullUrl = subUrl + "api/board";

        //Convert the QuestionBoardCreationBindingModel to JSON
        String requestBody = gson.toJson(board);

        //Send the post request and retrieve the response from the server
        HttpResponse<String> res = post(fullUrl, requestBody, "Content-Type", 
                                        "application/json;charset=UTF-8");

        //Check if the request was sent and received properly and return null if this is not the case.
        if (res == null || res.statusCode() != 200) {
            return null;
        }

        return res;
    }

    /**
     * Retrieves the board details of the Question Board associated with the specified moderator code from
     *      the server.
     * Communicates with the /api/board/moderator?code={moderatorCode} server endpoint.
     *
     * @param moderatorCode     The code belonging to the Question Board whose details should be retrieved.
     * @return Returns a QuestionBoardDetailsDto containing the details of the Question Board or null
     *      if this does not exist.
     */
    public static QuestionBoardDetailsDto retrieveBoardDetailsThroughModCode(UUID moderatorCode) {
        //Create a request and response object, send the request, and retrieve the response
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(subUrl + "/api/board/moderator?code=" + moderatorCode)).build();
        HttpResponse<String> response = sendRequest(request);

        //If the code was not a moderator code or the response was null, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        //Convert the JSON response to a UUID and return this
        return gson.fromJson(response.body(), QuestionBoardDetailsDto.class);
    }

    /**
     * Retrieves the details of the Question Board with specified board ID from the server.
     * Communicates with the /api/board/{boardID} server endpoint.
     *
     * @param boardID   The ID of the Question Board whose details should be retrieved.
     * @return Returns a QuestionBoardDetailsDto containing the details of the Question Board or null
     *      if this does not exist.
     */
    public static QuestionBoardDetailsDto retrieveBoardDetails(UUID boardID) {
        //Create a request and response object, send the request, and retrieve the response
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(subUrl + "api/board/" + boardID)).build();
        HttpResponse<String> response = sendRequest(request);

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

        //Convert the JSON response to a QuestionBoardDetailsDto object
        QuestionBoardDetailsDto details = gson.fromJson(response.body(), QuestionBoardDetailsDto.class);

        return details;
    }

    /**
     * Retrieves a quote from the server.
     * @return the body of a get request to the server.
     */
    public static String getQuote() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/quote")).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return response.body();
    }

}
