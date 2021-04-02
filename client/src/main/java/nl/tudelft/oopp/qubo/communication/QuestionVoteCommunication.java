package nl.tudelft.oopp.qubo.communication;

import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteDetailsDto;

import java.net.http.HttpResponse;
import java.util.UUID;

/**
 * This class hold methods related to communication for QuestionVotes.
 */
public class QuestionVoteCommunication {
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

}