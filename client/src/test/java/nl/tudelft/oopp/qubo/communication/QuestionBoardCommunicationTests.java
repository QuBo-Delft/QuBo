package nl.tudelft.oopp.qubo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgssoft.httpclient.HttpClientMock;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

public class QuestionBoardCommunicationTests {
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
}
