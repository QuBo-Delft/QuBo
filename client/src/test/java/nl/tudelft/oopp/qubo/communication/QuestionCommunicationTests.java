package nl.tudelft.oopp.qubo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgssoft.httpclient.HttpClientMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QuestionCommunicationTests {
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

    // Test if the addAnswer method returns a non-null response body after being called with a
    // valid question ID and moderator code then receiving status code 200.
    @Test
    public void testAddAnswerValidRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2)
            .doReturnStatus(200);

        // Act
        String responseBody = QuestionCommunication.addAnswer(uuid1, uuid2,
            "The answer to the universe is 42");

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
            .post(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2).called();
    }

    // Test if the addAnswer method returns a null response after being called with an invalid
    // question ID and moderator code then receiving status code 404.
    @Test
    public void testAddAnswerNonExistentQuestion() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2)
            .doReturnStatus(404).doReturn(failureToken);

        // Act
        String responseBody = QuestionCommunication.addAnswer(uuid1, uuid2,
            "The answer to the universe is 42");

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
            .post(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2).called();
    }

    // Test if the addAnswer method returns a null response after being called with a valid
    // question ID and invalid moderator code then receiving status code 403.
    @Test
    public void testAddAnswerInvalidModCode() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2)
            .doReturnStatus(404).doReturn(failureToken);

        // Act
        String responseBody = QuestionCommunication.addAnswer(uuid1, uuid2,
            "The answer to the universe is 42");

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
            .post(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2).called();
    }

    // Test if the addAnswer method returns the successToken after being called with a valid
    // question ID and moderator code then receiving a response with the successToken as its body.
    @Test
    public void testAddAnswerCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2)
            .doReturn(successToken);

        // Act
        String responseBody = QuestionCommunication.addAnswer(uuid1, uuid2,
            "The answer to the universe is 42");

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
            .post(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2).called();
    }
}
