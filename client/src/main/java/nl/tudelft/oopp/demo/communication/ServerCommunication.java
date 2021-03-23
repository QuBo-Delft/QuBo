package nl.tudelft.oopp.demo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.demo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.demo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.pacevote.PaceVoteCreationDto;
import nl.tudelft.oopp.demo.dtos.pacevote.PaceVoteDetailsDto;
import nl.tudelft.oopp.demo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.question.QuestionCreationDto;
import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.demo.dtos.question.QuestionEditingBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.demo.dtos.questionvote.QuestionVoteCreationDto;

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
     * Retrieves an HTTP response from the server by sending an HTTP delete request.
     *
     * @param fullUrl   The URL corresponding to the server endpoint.
     * @return The HTTP response returned.
     */
    private static HttpResponse<String> delete(String fullUrl) {
        //Set up the request Object
        HttpRequest request = HttpRequest.newBuilder()
            .DELETE()
            .uri(URI.create(fullUrl))
            .build();

        //Send the request, and retrieve and return the response from the server
        return sendRequest(request);
    }

    /**
     * Retrieves an HTTP response from the server by sending an HTTP patch request.
     *
     * @param fullUrl   The URL corresponding to the server endpoint.
     * @return The http response.
     */
    private static HttpResponse<String> patch(String fullUrl) {
        //Set up the request Object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{}"))
                .header("Content-Type", "application/json")
                .build();

        //Send the request, and return the http response
        return  sendRequest(request);
    }

    /**
     * Retrieves an HTTP response from the server by sending an HTTP put request.
     *
     * @param fullUrl       The URL corresponding to the server endpoint.
     * @param requestBody   The body of the request. This should contain the information that should be sent to
     *      the server.
     * @return The HTTP response returned.
     */
    private static HttpResponse<String> put(String fullUrl, String requestBody) {
        //Set up the request Object
        HttpRequest request = HttpRequest.newBuilder()
            .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
            .uri(URI.create(fullUrl))
            .headers("Content-Type", "application/json;charset=UTF-8")
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
    public static QuestionBoardCreationDto createBoardRequest(QuestionBoardCreationBindingModel board) {
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

        return gson.fromJson(res.body(), QuestionBoardCreationDto.class);
    }

    /**
     * This method aims to close a question board corresponding to a specific boardId and moderatorCode.
     *
     * @param boardId           The board id of a question board to be closed.
     * @param moderatorCode     The moderator code of this question board.
     * @return true if and only if the question board was closed successfully.
     */
    public static boolean closeBoardRequest(UUID boardId, UUID moderatorCode) {
        // Construct the full url for closing a question board
        String fullUrl = subUrl + "api/board/" + boardId + "/close?code=" + moderatorCode;

        //Send the http patch request and retrieve the response
        HttpResponse<String> response = patch(fullUrl);

        if (response == null || response.statusCode() != 200) {
            return false;
        }

        return true;
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
     * Retrieves the question list of the specified board.
     * Communicates with the /api/board/{boardid}/questions server endpoint.
     *
     * @param boardId   The ID of the Question Board whose question list should be retrieved.
     * @return Returns an array of QuestionDetailsDtos.
     */
    public static QuestionDetailsDto[] retrieveQuestions(UUID boardId) {
        //Send the request to retrieve the questions of the question board, and retrieve the response
        HttpRequest request = HttpRequest.newBuilder().GET()
            .uri(URI.create(subUrl + "api/board/" + boardId + "/questions"))
            .build();
        HttpResponse<String> response = sendRequest(request);

        //Convert the response to an array of QuestionDetailsDtos and return this
        QuestionDetailsDto[] questionArray = gson.fromJson(response.body(), QuestionDetailsDto[].class);

        return questionArray;
    }

    /**
     * Adds a question with specified text and author to the board.
     * Communicates with the /api/board/{boardid}/question server endpoint.
     *
     * @param boardId   The ID of the Question Board whose details should be retrieved.
     * @param text      The content of the question.
     * @param author    The name of the author of the question.
     * @return Returns a QuestionCreationDto that contains the ID and secret code associated
     *      with the question.
     */
    public static QuestionCreationDto addQuestion(UUID boardId, String text, String author) {
        //Instantiate a QuestionCreationBindingModel
        QuestionCreationBindingModel questionModel = new QuestionCreationBindingModel();
        questionModel.setText(text);
        questionModel.setAuthorName(author);

        //Create a request and response object, send the request, and retrieve the response
        String fullUrl = subUrl + "api/board/" + boardId + "/question";
        String requestBody = gson.toJson(questionModel);
        HttpResponse<String> response = post(fullUrl, requestBody, "Content-Type",
            "application/json;charset=UTF-8");

        //Check if the response object is null or if the status code is not equal to 200,
        //in which case null is returned
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        //Convert the JSON response to a QuestionBoardDetailsDto object
        QuestionCreationDto questionCodes = gson.fromJson(response.body(), QuestionCreationDto.class);

        return questionCodes;
    }

    /**
     * Edits the text of a question
     * Communicates with the /api/question/{questionid}?code={code} server endpoint.
     *
     * @param questionId    The ID of the question whose text should be modified.
     * @param code          The moderator code associated with the question board that contains the question or
     *      the question secret code.
     * @param text          The new question text.
     * @return Returns true if, and only if, the request was successful
     */
    public static boolean editQuestion(UUID questionId, UUID code, String text) {
        //Set up the parameters required by the put helper method
        String fullUrl = subUrl + "/api/question/" + questionId + "?code=" + code;

        QuestionEditingBindingModel editedQuestion = new QuestionEditingBindingModel();
        editedQuestion.setText(text);
        String requestBody = gson.toJson(editedQuestion);

        //Send the put request to edit the question and retrieve the response
        HttpResponse<String> response = put(fullUrl, requestBody);

        //If the request was unsuccessful, return false
        if (response == null || response.statusCode() != 200) {
            return false;
        }

        return true;
    }

    /**
     * Deletes the question from the board.
     * Communicates with the /api/question/{questionid}?code={code} server endpoint.
     *
     * @param questionId    The ID of the question that should be deleted.
     * @param code          The moderator code associated with the board or the question's secret code.
     * @return Returns true if, and only if, the question was deleted from the board.
     */
    public static boolean deleteQuestion(UUID questionId, UUID code) {
        //Set up the variables required by the delete helper method
        String fullUrl = subUrl + "/api/question/" + questionId + "?code=" + code;

        //Send the request to delete the question from the board and retrieve the response
        HttpResponse<String> response = delete(fullUrl);

        //Check if the question has been deleted properly
        //Return false if this was not the case
        if (response.statusCode() != 200) {
            return false;
        }

        return true;
    }

    /**
     * Adds a pace vote to the question board.
     * Communicated with the /api/board/{boardid}/pace server endpoint.
     *
     * @param boardId   The ID of the question board to which a pace vote should be added.
     * @param paceType  The type of pace vote that should be added.
     * @return A PaceVoteCreationDto with the ID of the pace vote.
     */
    public static PaceVoteCreationDto addPaceVote(UUID boardId, PaceType paceType) {
        //Create a PaceVoteCreationBindingModel with the specified pace type
        PaceVoteCreationBindingModel paceModel = new PaceVoteCreationBindingModel();
        paceModel.setPaceType(paceType);

        //Set up the variables needed to call the post method
        String requestBody = gson.toJson(paceModel);
        String fullUrl = subUrl + "/api/board/" + boardId + "/pace";

        //Request the pace vote creation, and retrieve the response
        HttpResponse<String> response = post(fullUrl, requestBody, "Content-Type",
            "application/json;charset=UTF-8");

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        //Convert the response body to a PaceVoteCreationDto and return this
        PaceVoteCreationDto paceVote = gson.fromJson(response.body(), PaceVoteCreationDto.class);

        return paceVote;
    }

    /**
     * Deletes a pace vote with specified ID from the question board.
     * Communicated with the /api/board/{boardid}/pace/{pacevoteid} server endpoint.
     *
     * @param boardId       The question board from which the pace vote should be deleted.
     * @param paceVoteId    The ID of the pace vote that should be deleted.
     * @return True if, and only if, the deletion was successful.
     */
    public static boolean deletePaceVote(UUID boardId, UUID paceVoteId) {
        //Set up the URL that will be sent to the delete helper method
        String fullUrl = subUrl + "/api/board/" + boardId + "/pace/" + paceVoteId;

        //Send the request to the server and receive the response
        HttpResponse<String> response = delete(fullUrl);

        //If the request was unsuccessful, return false
        if (response == null || response.statusCode() != 200) {
            return false;
        }

        //Check if the deleted pace vote had the same ID
        PaceVoteDetailsDto deletedVote = gson.fromJson(response.body(), PaceVoteDetailsDto.class);
        if (!deletedVote.getId().equals(paceVoteId)) {
            return false;
        }

        return true;
    }

    /**
     * Adds a vote to a question.
     * Communicates with the /api/question/{questionid}/vote server endpoint.
     *
     * @param questionId    The ID of the question to which a vote should be added.
     * @return Returns a QuestionVoteCreationDto associated with the created question vote.
     */
    public static QuestionVoteCreationDto addQuestionVote(UUID questionId) {
        //Set up the parameters that need to be passed to the post helper method
        String fullUrl = subUrl + "/api/question/" + questionId + "/vote";
        String requestBody = gson.toJson(questionId);

        //Send the request to add a vote, and retrieve the response
        HttpResponse<String> response = post(fullUrl, requestBody, "Content-Type",
            "application/json;charset=UTF-8");

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        //Convert the response to a QuestionVoteCreationDto and return this
        QuestionVoteCreationDto questionVote = gson.fromJson(response.body(), QuestionVoteCreationDto.class);

        return questionVote;
    }

    /**
     * Deletes a question vote from the specified question.
     * Communicates with the /api/question/{questionid}/vote/{voteid} server endpoint.
     *
     * @param questionId    The ID of the question from which a vote should be deleted.
     * @param voteId        The ID of the vote that should be deleted.
     * @return Returns true if, and only if, the vote has been deleted successfully.
     */
    public static boolean deleteQuestionVote(UUID questionId, UUID voteId) {
        //Set up the parameter required to call the delete helper method
        String fullUrl = subUrl + "/api/question/" + questionId + "/vote/" + voteId;

        //Send the request to delete the vote, and retrieve the response
        HttpResponse<String> response = delete(fullUrl);

        //If the request was unsuccessful, return false
        if (response == null || response.statusCode() != 200) {
            return false;
        }

        return true;
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
