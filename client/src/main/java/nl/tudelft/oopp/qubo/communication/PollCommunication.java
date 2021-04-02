package nl.tudelft.oopp.qubo.communication;

import nl.tudelft.oopp.qubo.dtos.poll.PollCreationBindingModel;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.UUID;

/**
 * This class hold methods related to communication for Polls.
 */
public class PollCommunication {

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
        String requestBody = ServerCommunication.gson.toJson(pollModel);
        String fullUrl = ServerCommunication.subUrl + "board/" + boardId + "/poll?code=" + moderatorCode;

        //Request the poll creation, and retrieve the response
        HttpResponse<String> response = ServerCommunication.post(fullUrl, requestBody, "Content-Type",
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
                .uri(URI.create(ServerCommunication.subUrl + "board/" + boardId + "/poll")).build();
        HttpResponse<String> response = ServerCommunication.sendRequest(request);

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
        String fullUrl = ServerCommunication.subUrl + "board/" + boardId + "/poll?code=" + moderatorCode;

        //Send the request to delete the question from the board and retrieve the response
        HttpResponse<String> response = ServerCommunication.delete(fullUrl);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

}