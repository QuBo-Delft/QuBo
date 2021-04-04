package nl.tudelft.oopp.qubo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgssoft.httpclient.HttpClientMock;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QuestionVoteCommunicationTests {
    private final String successToken = "{token: success}";
    private final String failureToken = "{token: failure}";

    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();
    private final UUID uuid3 = UUID.randomUUID();

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    private static final String subUrl = "http://localhost:8080/";

    private HttpClientMock httpClientMock;

    /**
     * Initialise the httpClientMock object for each test case.
     */
    @BeforeEach
    public void setUp() {
        httpClientMock = new HttpClientMock();
    }

    // Test if the addQuestionVote method returns a non-null response body after being called
    // with a valid questionId and receiving statusCode 200.
    @Test
    public void testAddQuestionVoteThroughValidRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/question/" + uuid1 + "/vote")
                .doReturnStatus(200);
        // Act
        String responseBody = QuestionVoteCommunication.addQuestionVote(uuid1);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/question/" + uuid1 + "/vote").called();
    }

    // Test if the addQuestionVote method returns null after being called with an invalid
    // questionId and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testAddQuestionVoteThroughInvalidRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/question/" + uuid3 + "/vote")
                .doReturnStatus(404).doReturn(failureToken);
        // Act
        String responseBody = QuestionVoteCommunication.addQuestionVote(uuid3);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/question/" + uuid3 + "/vote").called();
    }

    // Test if the addQuestionVote method returns the successToken after being called
    // with a valid questionId and receiving statusCode 200 and the successToken as the response body.
    @Test
    public void testAddQuestionVoteGivingCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/question/" + uuid3 + "/vote")
                .doReturn(successToken);
        // Act
        String responseBody = QuestionVoteCommunication.addQuestionVote(uuid3);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/question/" + uuid3 + "/vote").called();
    }

    // Test if the deleteQuestionVote method returns a non-null response body after being called
    // with a valid questionId and voteId and receiving statusCode 200.
    @Test
    public void testDeleteQuestionVoteThroughValidRequest() {
        // Arrange
        QuestionVoteDetailsDto qd = new QuestionVoteDetailsDto();
        qd.setId(uuid2);
        String qdStr = gson.toJson(qd);

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/question/" + uuid1 + "/vote/" + uuid2)
                .doReturn(qdStr).doReturnStatus(200);
        // Act
        String responseBody = QuestionVoteCommunication.deleteQuestionVote(uuid1, uuid2);

        // Assert
        assertEquals(qdStr, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/question/" + uuid1 + "/vote/" + uuid2).called();
    }

    // Test if the deleteQuestionVote method returns null after being called with an invalid
    // questionId and voteId and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testDeleteQuestionVoteThroughInvalidRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/question/" + uuid1 + "/vote/" + uuid2)
                .doReturnStatus(404).doReturn(failureToken);
        // Act
        String responseBody = QuestionVoteCommunication.deleteQuestionVote(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/question/" + uuid1 + "/vote/" + uuid2).called();
    }

    // Test if the addQuestionVote method returns null after being called with a valid questionId and
    // voteId, and receiving statusCode 200 and a response body with a wrong deletedVote.
    @Test
    public void testDeleteQuestionVoteGivingIncorrectResponseBody() {
        // Arrange
        QuestionVoteDetailsDto qd = new QuestionVoteDetailsDto();
        qd.setId(uuid3);
        String qdStrReturned = gson.toJson(qd);

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/question/" + uuid1 + "/vote/" + uuid2)
                .doReturn(qdStrReturned);
        // Act
        String responseBody = QuestionVoteCommunication.deleteQuestionVote(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/question/" + uuid1 + "/vote/" + uuid2).called();
    }
}
