package nl.tudelft.oopp.qubo.communication;

import java.net.http.HttpResponse;
import java.util.UUID;

/**
 * This class hold methods related to communication for banning users.
 */
public class BanUserCommunication {

    /**
     * The method sends a request to the server to ban a user from a question board.
     *
     * @param questionId        The question ID of the question posted by the user to be banned.
     * @param moderatorCode     The moderator code of the question board.
     * @return True if and only the user is banned successfully.
     */
    public static boolean banUser(UUID questionId, UUID moderatorCode) {
        // Set up the variables required by the post helper method
        String fullUrl = ServerCommunication.subUrl
                + "question/" + questionId + "/ban?code=" + moderatorCode;

        // Request to ban a user on the server-side
        HttpResponse<String> response = ServerCommunication.post(fullUrl);

        // If the request was unsuccessful, return false
        if (response == null || response.statusCode() != 200) {
            return false;
        }

        return true;
    }

}
