package nl.tudelft.oopp.qubo.communication;

import com.pgssoft.httpclient.HttpClientMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PollCommunicationTests {
    private final String successToken = "{token: success}";
    private final String failureToken = "{token: failure}";

    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();

    private static final String subUrl = "http://localhost:8080/";

    private HttpClientMock httpClientMock;

    /**
     * Initialise the httpClientMock object for each test case.
     */
    @BeforeEach
    public void setUp() {
        httpClientMock = new HttpClientMock();
    }

    // Test if the addPoll method returns a non-null response body after being called
    // with a valid boardId and moderator code, and receiving a response with status code 200.
    @Test
    public void testAddPollThroughValidRequest() {
        // Arrange
        Set<String> pollOptions = new HashSet<>();
        pollOptions.add("Option A");
        pollOptions.add("Option B");

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2)
                .doReturnStatus(200);
        // Act
        String responseBody = PollCommunication.addPoll(uuid1, uuid2, "Test Poll", pollOptions);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2).called();
    }

    // Test if the addPoll method returns null after being called with an invalid boardId and
    // an invalid moderator code, and receiving a response with status code 404 with a failureToken
    // as its body.
    @Test
    public void testAddPollThroughInvalidRequest() {
        // Arrange
        Set<String> pollOptions = new HashSet<>();
        pollOptions.add("Option A");
        pollOptions.add("Option B");

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2)
                .doReturnStatus(404).doReturn(failureToken);

        // Act
        String responseBody = PollCommunication.addPoll(uuid1, uuid2, "Test Poll", pollOptions);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2).called();
    }

    // Test if the addPoll method returns the successtoken after being called with a valid boardId and
    // moderator code, and receiving a response with status code 200 with a successToken as its body.
    @Test
    public void testAddPollVoteGivingCorrectResponseBody() {
        // Arrange
        Set<String> pollOptions = new HashSet<>();
        pollOptions.add("Option A");
        pollOptions.add("Option B");

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2)
                .doReturn(successToken);

        // Act
        String responseBody = PollCommunication.addPoll(uuid1, uuid2, "Test Poll", pollOptions);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2).called();
    }

    // Test if the retrievePollDetails method returns a non-null response body
    // after being called with a valid board ID, and receiving a response with status code 200.
    @Test
    public void testRetrievePollDetails() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/poll").doReturnStatus(200);

        // Act
        String responseBody = PollCommunication.retrievePollDetails(uuid1);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/poll").called();
    }

    // Test if the retrievePollDetails method returns null after being called with an invalid board ID,
    // and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testRetrievePollDetailsThroughInvalidBoardId() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/poll")
                .doReturnStatus(404).doReturn(failureToken);

        // Act
        String responseBody = PollCommunication.retrievePollDetails(uuid1);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/poll").called();
    }

    // Test if the retrievePollDetails method returns the successToken after being called with a
    // valid board ID, and receiving a response with status code 200 with the successToken as its body.
    @Test
    public void testRetrievePollDetailsGivingCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid2 + "/poll")
                .doReturn(successToken);

        // Act
        String responseBody = PollCommunication.retrievePollDetails(uuid2);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid2 + "/poll").called();
    }

    // Test if the retrievePollResults method returns a non-null response body
    // after being called with a valid board ID, and receiving a response with status code 200.
    @Test
    public void testRetrievePollResults() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/poll/results").doReturnStatus(200);

        // Act
        String responseBody = PollCommunication.retrievePollResults(uuid1);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/poll/results").called();
    }

    // Test if the retrievePollResults method returns null after being called with an invalid board ID,
    // and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testRetrievePollResultsThroughInvalidBoardId() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/poll/results")
                .doReturnStatus(404).doReturn(failureToken);

        // Act
        String responseBody = PollCommunication.retrievePollResults(uuid1);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/poll/results").called();
    }

    // Test if the retrievePollResults method returns null after being called when the poll was still open,
    // and receiving statusCode 403 and failureToken as the response body.
    @Test
    public void testRetrievePollResultsWithOpenPoll() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/poll/results")
                .doReturnStatus(403).doReturn(failureToken);

        // Act
        String responseBody = PollCommunication.retrievePollResults(uuid1);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/poll/results").called();
    }

    // Test if the retrievePollResults method returns the successToken after being called with a
    // valid board ID, and receiving a response with status code 200 with the successToken as its body.
    @Test
    public void testRetrievePollResultsGivingCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/poll/results")
                .doReturn(successToken);

        // Act
        String responseBody = PollCommunication.retrievePollResults(uuid1);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/poll/results").called();
    }

    // Test if the deletePoll method returns a non-null response body after being called
    // with a valid question board ID and moderator code and receiving a response with status code 200.
    @Test
    public void testDeletePollValidRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2)
                .doReturnStatus(200);

        // Act
        String responseBody = PollCommunication.deletePoll(uuid1, uuid2);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2).called();
    }

    // Test if the deletePoll method returns null after being called with an invalid board ID and moderator
    // code, and receiving a response with status code 400 and a failure token as its body.
    @Test
    public void testDeletePollInvalidRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2)
                .doReturnStatus(400).doReturn(failureToken);

        // Act
        String responseBody = PollCommunication.deletePoll(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2).called();
    }

    // Test if the deletePoll method returns null after being called with an invalid board ID and moderator
    // code, and receiving a response with status code 403 and a failure token as its body.
    @Test
    public void testDeletePollInvalidModCode() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2)
                .doReturnStatus(403).doReturn(failureToken);

        // Act
        String responseBody = PollCommunication.deletePoll(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2).called();
    }

    // Test if the deletePoll method returns null after being called with an invalid board ID and moderator
    // code, and receiving a response with status code 404 and a failure token as its body.
    @Test
    public void testDeletePollInvalidQuBo() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2)
                .doReturnStatus(404).doReturn(failureToken);

        // Act
        String responseBody = PollCommunication.deletePoll(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2).called();
    }

    // Test if the deletePoll method returns the success token after being called with a valid question board
    // ID and moderator code and receiving a response with status code 200 and a success token as its body.
    @Test
    public void testDeletePollGivingCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2)
                .doReturn(successToken);

        // Act
        String responseBody = PollCommunication.deletePoll(uuid1, uuid2);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2).called();
    }

    @Test
    public void testPollVoteRegistrationResponse() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        String fullUrl = subUrl + "api/board/" + uuid1 + "/poll/" + uuid2 + "/vote";
        httpClientMock.onPost(fullUrl)
            .doReturnStatus(200);

        // Act
        String responseBody = PollCommunication.addPollVote(uuid1, uuid2);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(fullUrl).called();
    }

    @Test
    public void testPollVoteRegistrationInvalidQuBo() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        String fullUrl = subUrl + "api/board/" + uuid1 + "/poll/" + uuid2 + "/vote";
        httpClientMock.onPost(fullUrl).doReturnStatus(404).doReturn(failureToken);

        // Act
        String responseBody = PollCommunication.addPollVote(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(fullUrl).called();
    }

    @Test
    public void testPollVoteRegistrationResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        String fullUrl = subUrl + "api/board/" + uuid1 + "/poll/" + uuid2 + "/vote";
        httpClientMock.onPost(fullUrl).doReturn(successToken);

        // Act
        String responseBody = PollCommunication.addPollVote(uuid1, uuid2);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(fullUrl).called();
    }

    @Test
    public void testPollVoteRemovalResponse() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        String fullUrl = subUrl + "api/board/" + uuid1 + "/poll/vote/" + uuid2;
        httpClientMock.onDelete(fullUrl).doReturnStatus(200);

        // Act
        String responseBody = PollCommunication.removePollVote(uuid1, uuid2);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().delete(fullUrl).called();
    }

    @Test
    public void testPollVoteRemovalInvalidQuBo() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        String fullUrl = subUrl + "api/board/" + uuid1 + "/poll/vote/" + uuid2;
        httpClientMock.onPost(fullUrl).doReturnStatus(404).doReturn(failureToken);

        // Act
        String responseBody = PollCommunication.removePollVote(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().delete(fullUrl).called();
    }

    @Test
    public void testPollVoteRemovalResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        String fullUrl = subUrl + "api/board/" + uuid1 + "/poll/vote/" + uuid2;
        httpClientMock.onDelete(fullUrl).doReturn(successToken);

        // Act
        String responseBody = PollCommunication.removePollVote(uuid1, uuid2);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().delete(fullUrl).called();
    }

    @Test
    public void testGetAggregatedPaceVotesRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        String url = subUrl + "api/board/" + uuid1 + "/pace?code=" + uuid2;
        httpClientMock.onGet(url).doReturnStatus(200);

        // Act
        String responseBody = PaceVoteCommunication.getAggregatedPaceVotes(uuid1, uuid2);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(url).called();
    }

    @Test
    public void testGetAggregatedPaceVotesInvalidQuBo() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        String url = subUrl + "api/board/" + uuid1 + "/pace?code=" + uuid2;
        httpClientMock.onGet(url).doReturnStatus(404).doReturn(failureToken);

        // Act
        String responseBody = PaceVoteCommunication.getAggregatedPaceVotes(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(url).called();
    }

    @Test
    public void testGetAggregatedPaceVotesInvalidVoteId() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        String url = subUrl + "api/board/" + uuid1 + "/pace?code=" + uuid2;
        httpClientMock.onGet(url).doReturnStatus(403).doReturn(failureToken);

        // Act
        String responseBody = PaceVoteCommunication.getAggregatedPaceVotes(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(url).called();
    }

    @Test
    public void testGetAggregatedPaceVotesCorrectRequestBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        String url = subUrl + "api/board/" + uuid1 + "/pace?code=" + uuid2;
        httpClientMock.onGet(url).doReturn(successToken);

        // Act
        String responseBody = PaceVoteCommunication.getAggregatedPaceVotes(uuid1, uuid2);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(url).called();
    }
}
