package nl.tudelft.oopp.qubo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteDetailsDto;
import nl.tudelft.oopp.qubo.dtos.poll.PollCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.question.QuestionEditingBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteDetailsDto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.UUID;

public class ServerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();
    private static final String subUrl = "http://localhost:8080/api/";
    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    /**
     * Set the value of the client of ServerCommunication.
     *
     * @param client    The HttpClient object to set.
     */
    public static void setClient(HttpClient client) {
        ServerCommunication.client = client;
    }

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
     * @return The QuestionBoardDetailsDto of the created board in JSON String format.
     */
    public static String createBoardRequest(QuestionBoardCreationBindingModel board) {
        String fullUrl = subUrl + "board";

        //Convert the QuestionBoardCreationBindingModel to JSON
        String requestBody = gson.toJson(board);

        //Send the post request and retrieve the response from the server
        HttpResponse<String> res = post(fullUrl, requestBody, "Content-Type",
                                        "application/json;charset=UTF-8");

        //If the request was unsuccessful, return null
        if (res == null || res.statusCode() != 200) {
            return null;
        }

        return res.body();
    }

    /**
     * This method aims to close a question board corresponding to a specific boardId and moderatorCode.
     *
     * @param boardId           The board id of a question board to be closed.
     * @param moderatorCode     The moderator code of this question board.
     * @return The QuestionBoardDetailsDto in JSON String format if, and only if the question board was
     *          closed successfully.
     */
    public static String closeBoardRequest(UUID boardId, UUID moderatorCode) {
        // Construct the full url for closing a question board
        String fullUrl = subUrl + "board/" + boardId + "/close?code=" + moderatorCode;

        //Send the http patch request and retrieve the response
        HttpResponse<String> response = patch(fullUrl);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
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
                .uri(URI.create(subUrl + "board/moderator?code=" + moderatorCode)).build();
        HttpResponse<String> response = sendRequest(request);

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
                .uri(URI.create(subUrl + "board/" + boardID)).build();
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
            .uri(URI.create(subUrl + "board/" + boardId + "/questions"))
            .build();
        HttpResponse<String> response = sendRequest(request);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Adds a question with specified text and author to the board.
     * Communicates with the /api/board/{boardid}/question server endpoint.
     *
     * @param boardId   The ID of the Question Board whose details should be retrieved.
     * @param text      The content of the question.
     * @param author    The name of the author of the question.
     * @return The QuestionCreationDto in JSON String format that contains the ID and
     *         secret code associated with the question.
     */
    public static String addQuestion(UUID boardId, String text, String author) {
        //Instantiate a QuestionCreationBindingModel
        QuestionCreationBindingModel questionModel = new QuestionCreationBindingModel();
        questionModel.setText(text);
        questionModel.setAuthorName(author);

        //Create a request and response object, send the request, and retrieve the response
        String fullUrl = subUrl + "board/" + boardId + "/question";
        String requestBody = gson.toJson(questionModel);
        HttpResponse<String> response = post(fullUrl, requestBody, "Content-Type",
            "application/json;charset=UTF-8");

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Edits the text of a question
     * Communicates with the /api/question/{questionid}?code={code} server endpoint.
     *
     * @param questionId    The ID of the question whose text should be modified.
     * @param code          The moderator code associated with the question board
     *                      that contains the question or the question secret code.
     * @param text          The new question text.
     * @return The QuestionDetailsDto associated with the question in JSON String
     *          format if, and only if, the request was successful.
     */
    public static String editQuestion(UUID questionId, UUID code, String text) {
        //Set up the parameters required by the put helper method
        String fullUrl = subUrl + "question/" + questionId + "?code=" + code;

        QuestionEditingBindingModel editedQuestion = new QuestionEditingBindingModel();
        editedQuestion.setText(text);
        String requestBody = gson.toJson(editedQuestion);

        //Send the put request to edit the question and retrieve the response
        HttpResponse<String> response = put(fullUrl, requestBody);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Deletes the question from the board.
     * Communicates with the /api/question/{questionid}?code={code} server endpoint.
     *
     * @param questionId    The ID of the question that should be deleted.
     * @param code          The moderator code associated with the board or the question's secret code.
     * @return The QuestionDetailsDto associated with the deleted question in JSON String format if,
     *          and only if, the question was deleted from the board.
     */
    public static String deleteQuestion(UUID questionId, UUID code) {
        //Set up the variables required by the delete helper method
        String fullUrl = subUrl + "question/" + questionId + "?code=" + code;

        //Send the request to delete the question from the board and retrieve the response
        HttpResponse<String> response = delete(fullUrl);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Mark the question from the board as answered.
     *
     * @param questionId    The ID of the question to be marked as answered.
     * @param code          The moderator code that is associated with the board
     *                      the question is part of, or the question's secret code.
     * @return The QuestionDetailsDto of the answered question in JSON String format
     *          if and only if the question has been marked as answered successfully.
     */
    public static String markQuestionAsAnswered(UUID questionId, UUID code) {
        //Set up the variables required by the patch helper method
        String fullUrl = subUrl + "question/" + questionId + "/answer?code=" + code;

        //Send the request to mark the question as answered and retrieve the response
        HttpResponse<String> response = patch(fullUrl);

        //Check if the request was successful
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        //The question has been marked as answered
        return response.body();
    }

    /**
     * Adds a vote to a question.
     * Communicates with the /api/question/{questionid}/vote server endpoint.
     *
     * @param questionId    The ID of the question to which a vote should be added.
     * @return The QuestionVoteCreationDto associated with the created question vote
     *         in JSON String format.
     */
    public static String addQuestionVote(UUID questionId) {
        //Set up the parameters that need to be passed to the post helper method
        String fullUrl = subUrl + "question/" + questionId + "/vote";
        String requestBody = gson.toJson(questionId);

        //Send the request to add a vote, and retrieve the response
        HttpResponse<String> response = post(fullUrl, requestBody, "Content-Type",
            "application/json;charset=UTF-8");

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Deletes a question vote from the specified question.
     * Communicates with the /api/question/{questionid}/vote/{voteid} server endpoint.
     *
     * @param questionId    The ID of the question from which a vote should be deleted.
     * @param voteId        The ID of the vote that should be deleted.
     * @return The QuestionVoteDetailsDto associated with the deleted vote in JSON String format
     *          if, and only if, the vote has been deleted successfully.
     */
    public static String deleteQuestionVote(UUID questionId, UUID voteId) {
        //Set up the parameter required to call the delete helper method
        String fullUrl = subUrl + "question/" + questionId + "/vote/" + voteId;

        //Send the request to delete the vote, and retrieve the response
        HttpResponse<String> response = delete(fullUrl);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        //Check if the deleted question vote was the right vote
        QuestionVoteDetailsDto deletedVote = gson.fromJson(response.body(), QuestionVoteDetailsDto.class);
        if (!deletedVote.getId().equals(voteId)) {
            return null;
        }

        return response.body();
    }

    /**
     * Adds a pace vote to the question board.
     * Communicated with the /api/board/{boardid}/pace server endpoint.
     *
     * @param boardId   The ID of the question board to which a pace vote should be added.
     * @param paceType  The type of pace vote that should be added.
     * @return The PaceVoteCreationDto in JSON String format if the vote was added successfully.
     */
    public static String addPaceVote(UUID boardId, PaceType paceType) {
        //Create a PaceVoteCreationBindingModel with the specified pace type
        PaceVoteCreationBindingModel paceModel = new PaceVoteCreationBindingModel();
        paceModel.setPaceType(paceType);

        //Set up the variables needed to call the post method
        String requestBody = gson.toJson(paceModel);
        String fullUrl = subUrl + "board/" + boardId + "/pace";

        //Request the pace vote creation, and retrieve the response
        HttpResponse<String> response = post(fullUrl, requestBody, "Content-Type",
            "application/json;charset=UTF-8");

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Deletes a pace vote with specified ID from the question board.
     * Communicated with the /api/board/{boardid}/pace/{pacevoteid} server endpoint.
     *
     * @param boardId       The question board from which the pace vote should be deleted.
     * @param paceVoteId    The ID of the pace vote that should be deleted.
     * @return The PaceVoteDetailsDto associated with the deleted pace vote in JSON String
     *          format if, and only if, the deletion was successful.
     */
    public static String deletePaceVote(UUID boardId, UUID paceVoteId) {
        //Set up the URL that will be sent to the delete helper method
        String fullUrl = subUrl + "board/" + boardId + "/pace/" + paceVoteId;

        //Send the request to the server and receive the response
        HttpResponse<String> response = delete(fullUrl);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        //Check if the deleted pace vote had the same ID
        PaceVoteDetailsDto deletedVote = gson.fromJson(response.body(), PaceVoteDetailsDto.class);
        if (!deletedVote.getId().equals(paceVoteId)) {
            return null;
        }

        return response.body();
    }

    /**
     * Adds a poll to the question board.
     * Communicates with the /api/board/{boardid}/poll?code={moderatorcode} server endpoint.
     *
     * @param boardId       The ID of the question board to which a poll should be added.
     * @param moderatorCode The moderator code of the question board to which the poll should be added.
     * @param pollText      The text that is associated with the poll.
     * @param pollOptions   The set of answer options of the poll.
     * @return The PollCreationDto in JSON String format if the vote was added successfully.
     */
    public static String addPoll(UUID boardId, UUID moderatorCode, String pollText, Set<String> pollOptions) {
        //Create a PollCreationBindingModel
        PollCreationBindingModel pollModel = new PollCreationBindingModel();
        pollModel.setText(pollText);
        pollModel.setPollOptions(pollOptions);

        //Set up the variables needed to call the post method
        String requestBody = gson.toJson(pollModel);
        String fullUrl = subUrl + "board/" + boardId + "/poll?code=" + moderatorCode;

        //Request the poll creation, and retrieve the response
        HttpResponse<String> response = post(fullUrl, requestBody, "Content-Type",
                "application/json;charset=UTF-8");

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Retrieves the details of the poll associated with the question board whose ID was provided.
     * Communicates with the /api/board/{boardid}/poll server endpoint.
     *
     * @param boardId   The ID of the question board whose poll's details should be retrieved.
     * @return The PollDetailsDto associated with the poll in JSON String format if there was a poll associated
     *      with the question board whose ID was provided, null otherwise.
     */
    public static String retrievePollDetails(UUID boardId) {
        //Create a request and response object, send the request, and retrieve the response
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(subUrl + "board/" + boardId + "/poll")).build();
        HttpResponse<String> response = sendRequest(request);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        //Return the JSON String representation of the PollDetailsDto
        return response.body();
    }

    /**
     * Deletes the poll from the board.
     * Communicates with the /api/board/{boardid}/poll?code={moderatorcode} server endpoint.
     *
     * @param boardId       The ID of the question board whose poll should be deleted.
     * @param moderatorCode The moderator code associated with the question board.
     * @return The PollDetailsDto of the poll that was deleted in JSON String format if it existed,
     *      null otherwise.
     */
    public static String deletePoll(UUID boardId, UUID moderatorCode) {
        //Set up the variables required by the delete helper method
        String fullUrl = subUrl + "board/" + boardId + "/poll?code=" + moderatorCode;

        //Send the request to delete the question from the board and retrieve the response
        HttpResponse<String> response = delete(fullUrl);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Gets aggregated pace votes from the question board with the provided ID.
     *
     * @param boardId    The ID of the question board from which the pace votes should be counted.
     * @param moderatorCode The ID of the pace vote from which the results should be counted.
     * @return The aggregated pace votes in a PaceDetailsDto in JSON format.
     */
    public static String getAggregatedPaceVotes(UUID boardId, UUID moderatorCode) {
        //Set up the URL that will be sent to the delete helper method
        String fullUrl = ServerCommunication.subUrl + "board/" + boardId + "/pace?code=" + moderatorCode;

        //Send the request to the server and receive the response
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(fullUrl))
            .build();

        HttpResponse<String> response = ServerCommunication.sendRequest(request);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

}
