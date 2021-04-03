package nl.tudelft.oopp.qubo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgssoft.httpclient.HttpClientMock;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PaceVoteCommunicationTests {
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

    // Test if the addPaceVote method returns a non-null response body after being called
    // with a valid boardId and receiving statusCode 200.
    @Test
    public void testAddPaceVoteThroughValidRequest() {
        // Arrange
        PaceType pt = PaceType.TOO_FAST;

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/pace")
                .doReturnStatus(200);
        // Act
        String responseBody = PaceVoteCommunication.addPaceVote(uuid1, pt);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/board/" + uuid1 + "/pace").called();
    }

    // Test if the addPaceVote method returns null after being called with an invalid
    // boardId and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testAddPaceVoteThroughInvalidRequest() {
        // Arrange
        PaceType pt = PaceType.TOO_FAST;

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/pace")
                .doReturnStatus(404).doReturn(failureToken);
        // Act
        String responseBody = PaceVoteCommunication.addPaceVote(uuid1, pt);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/board/" + uuid1 + "/pace").called();
    }

    // Test if the addPaceVote method returns the successToken after being called with a
    // valid boardId and receiving statusCode 200 and the successToken as the response body.
    @Test
    public void testAddPaceVoteGivingCorrectResponseBody() {
        // Arrange
        PaceType pt = PaceType.TOO_FAST;

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/pace")
                .doReturn(successToken);
        // Act
        String responseBody = PaceVoteCommunication.addPaceVote(uuid1, pt);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/board/" + uuid1 + "/pace").called();
    }

    // Test if the deletePaceVote method returns a non-null response body after being called
    // with a valid boardId and paceVoteId, and receiving statusCode 200.
    @Test
    public void testDeletePaceVoteThroughValidRequest() {
        // Arrange
        PaceVoteDetailsDto pd = new PaceVoteDetailsDto();
        pd.setId(uuid2);
        String pdStringReturned = gson.toJson(pd);

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/board/" + uuid1 + "/pace/" + uuid2)
                .doReturn(pdStringReturned).doReturnStatus(200);
        // Act
        String responseBody = PaceVoteCommunication.deletePaceVote(uuid1, uuid2);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/board/" + uuid1 + "/pace/" + uuid2).called();
    }

    // Test if the deletePaceVote method returns null after being called with an invalid boardId and
    // paceVoteId and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testDeletePaceVoteThroughInvalidRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/board/" + uuid1 + "/pace/" + uuid2)
                .doReturnStatus(404);
        // Act
        String responseBody = PaceVoteCommunication.deletePaceVote(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/board/" + uuid1 + "/pace/" + uuid2).called();
    }

    // Test if the deletePaceVote method returns null after being called with a valid boardId
    // and paceVoteId and receiving statusCode 200 and a response body with the wrong deletedVote.
    @Test
    public void testDeletePaceVoteGivingIncorrectResponseBody() {
        // Arrange
        PaceVoteDetailsDto pd = new PaceVoteDetailsDto();
        pd.setId(uuid3);
        String pdStringReturned = gson.toJson(pd);

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/board/" + uuid1 + "/pace/" + uuid2)
                .doReturn(pdStringReturned).doReturnStatus(200);
        // Act
        String responseBody = PaceVoteCommunication.deletePaceVote(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/board/" + uuid1 + "/pace/" + uuid2).called();
    }

}
