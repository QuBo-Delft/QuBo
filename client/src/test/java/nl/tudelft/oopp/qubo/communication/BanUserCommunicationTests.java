package nl.tudelft.oopp.qubo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgssoft.httpclient.HttpClientMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BanUserCommunicationTests {

    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();
    private final UUID uuid3 = UUID.randomUUID();

    private static final String subUrl = "http://localhost:8080/api/";

    private HttpClientMock httpClientMock;

    /**
     * Initialise the httpClientMock object for each test case.
     */
    @BeforeEach
    public void setUp() {
        httpClientMock = new HttpClientMock();
    }

    // Test if the banUser method returns true after being called
    // with a valid question ID and moderator code, and receiving statusCode 200.
    @Test
    public void testBanUserThroughValidQuestionIdModCode() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "question/" + uuid1 + "/ban?code=" + uuid2)
                .doReturnStatus(200);

        // Act
        boolean isBanned = BanUserCommunication.banUser(uuid1, uuid2);

        // Assert
        assertTrue(isBanned);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "question/" + uuid1 + "/ban?code=" + uuid2).called();
    }

    // Test if the banUser method returns false after being called with an invalid
    // question ID and moderator code, and receiving statusCode 404.
    @Test
    public void testBanUserThroughInvalidQuestionIdModCode() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "question/" + uuid1 + "/ban?code=" + uuid3)
                .doReturnStatus(404);

        // Act
        boolean isBanned = BanUserCommunication.banUser(uuid1, uuid3);

        // Assert
        assertFalse(isBanned);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "question/" + uuid1 + "/ban?code=" + uuid3).called();
    }

}
