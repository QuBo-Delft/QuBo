package nl.tudelft.oopp.qubo.communication;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteDetailsDto;

/**
 * This class hold methods related to communication for PaceVotes.
 */
public class PaceVoteCommunication {

    /**
     * Adds a pace vote to the question board.
     * Communicates with the /api/board/{boardid}/pace server endpoint.
     *
     * @param boardId  The ID of the question board to which a pace vote should be added.
     * @param paceType The type of pace vote that should be added.
     * @return The PaceVoteCreationDto in JSON String format if the vote was added successfully.
     */
    public static String addPaceVote(UUID boardId, PaceType paceType) {
        //Create a PaceVoteCreationBindingModel with the specified pace type
        PaceVoteCreationBindingModel paceModel = new PaceVoteCreationBindingModel();
        paceModel.setPaceType(paceType);

        //Set up the variables needed to call the post method
        String requestBody = ServerCommunication.gson.toJson(paceModel);
        String fullUrl = ServerCommunication.subUrl + "board/" + boardId + "/pace";

        //Request the pace vote creation, and retrieve the response
        HttpResponse<String> response = ServerCommunication.post(fullUrl, requestBody, "Content-Type",
            "application/json;charset=UTF-8");

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Deletes a pace vote with specified ID from the question board.
     * Communicates with the /api/board/{boardid}/pace/{pacevoteid} server endpoint.
     *
     * @param boardId    The question board from which the pace vote should be deleted.
     * @param paceVoteId The ID of the pace vote that should be deleted.
     * @return The PaceVoteDetailsDto associated with the deleted pace vote in JSON String
     *         format if, and only if, the deletion was successful.
     */
    public static String deletePaceVote(UUID boardId, UUID paceVoteId) {
        //Set up the URL that will be sent to the delete helper method
        String fullUrl = ServerCommunication.subUrl + "board/" + boardId + "/pace/" + paceVoteId;

        //Send the request to the server and receive the response
        HttpResponse<String> response = ServerCommunication.delete(fullUrl);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        //Check if the deleted pace vote had the same ID
        PaceVoteDetailsDto deletedVote = ServerCommunication.gson
            .fromJson(response.body(), PaceVoteDetailsDto.class);
        if (!deletedVote.getId().equals(paceVoteId)) {
            return null;
        }

        return response.body();
    }

    /**
     * Gets aggregated pace votes from the question board with the provided ID.
     *
     * @param boardId       The ID of the question board from which the pace votes should be counted.
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