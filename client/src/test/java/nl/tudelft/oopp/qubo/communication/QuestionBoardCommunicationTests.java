package nl.tudelft.oopp.qubo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgssoft.httpclient.HttpClientMock;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QuestionBoardCommunicationTests {
    private final String successToken = "{token: success}";
    private final String failureToken = "{token: failure}";

    private final UUID uuid1 = UUID.randomUUID();
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

    // Test if the sendRequest method catches the exception caused by a refused connection.
    @Test
    public void testSendRequest() {
        assertNull(QuestionBoardCommunication.retrieveBoardDetails(uuid1));
    }

    // Tests if the createBoardRequest method returns a non-null response body after receiving
    // statusCode 200.
    @Test
    public void testCreateBoardRequest() {
        // Arrange
        Timestamp startTimeStamp = new Timestamp(new Date().getTime());
        QuestionBoardCreationBindingModel board =
                new QuestionBoardCreationBindingModel("Title", startTimeStamp);

        httpClientMock.onPost(subUrl + "api/board")
                .doReturnStatus(200);
        ServerCommunication.setClient(httpClientMock);

        // Act
        String responseBody = QuestionBoardCommunication.createBoardRequest(board);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/board").called();
    }

    // Test if the createBoardRequest method returns null after receiving statusCode 400
    // and failureToken as the response body.
    @Test
    public void testCreateBoardRequestInValidRequest() {
        // Arrange
        Timestamp startTimeStamp = new Timestamp(new Date().getTime());
        QuestionBoardCreationBindingModel board =
                new QuestionBoardCreationBindingModel("Title", startTimeStamp);

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board")
                .doReturnStatus(400).doReturn(failureToken);

        // Act
        String responseBody = QuestionBoardCommunication.createBoardRequest(board);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/board").called();
    }

    // Test if the createBoardRequest method returns the successToken after receiving
    // statusCode 200 and the successToken as the response body.
    @Test
    public void testCreateBoardRequestGivingCorrectResponseBody() {
        // Arrange
        Timestamp startTimeStamp = new Timestamp(new Date().getTime());
        QuestionBoardCreationBindingModel board =
                new QuestionBoardCreationBindingModel("Title", startTimeStamp);

        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board").doReturn(successToken);

        // Act
        String responseBody = QuestionBoardCommunication.createBoardRequest(board);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/board").called();
    }

    // Test if the closeBoardRequest method returns a non-null response body after being
    // called with a valid board ID and moderator code then receiving
    // statusCode 200.
    @Test
    public void testCloseBoardRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3)
                .doReturnStatus(200);

        // Act
        String responseBody = QuestionBoardCommunication.closeBoardRequest(uuid1, uuid3);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3).called();
    }

    // Test if the closeBoardRequest method returns null after being called with an invalid board ID and
    // moderator code, and receiving statusCode 400 and failureToken as response body.
    @Test
    public void testCloseBoardRequestInvalidCode() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3)
                .doReturnStatus(400).doReturn(failureToken);

        // Act
        String responseBody = QuestionBoardCommunication.closeBoardRequest(uuid1, uuid3);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3).called();
    }

    // Test if the closeBoardRequest method returns the successToken after being called with
    // a valid board ID and moderator code, and then receiving statusCode 200 and the successToken
    // as response body.
    @Test
    public void testCloseBoardRequestGivingCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3)
                .doReturn(successToken);

        // Act
        String responseBody = QuestionBoardCommunication.closeBoardRequest(uuid1, uuid3);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3).called();
    }

    // Test if the retrieveBoardDetails method returns a non-null response body
    // after being called with a valid board ID and receiving statusCode 200.
    @Test
    public void testRetrieveBoardDetails() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1).doReturnStatus(200);
        // Act
        String responseBody = QuestionBoardCommunication.retrieveBoardDetails(uuid1);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1).called();
    }

    // Test if the retrieveBoardDetails method returns null after being called with an invalid board ID,
    // and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testRetrieveBoardDetailsThroughInvalidBoardId() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1)
                .doReturnStatus(400);
        // Act
        String responseBody = QuestionBoardCommunication.retrieveBoardDetails(uuid1);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1).called();
    }

    // Test if the retrieveBoardDetails method returns the successToken after being called
    // with a valid board ID, and receiving statusCode 200 and the successToken as the response body.
    @Test
    public void testRetrieveBoardDetailsGivingCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1).doReturn(successToken);
        // Act
        String responseBody = QuestionBoardCommunication.retrieveBoardDetails(uuid1);
        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1).called();
    }

    // Test if the retrieveBoardDetailsThroughModCode method returns a non-null response body
    // after being called with a valid moderator code and receiving statusCode 200.
    @Test
    public void testRetrieveBoardDetailsThroughModCode() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/moderator?code=" + uuid1)
                .doReturnStatus(200);
        // Act
        String responseBody = QuestionBoardCommunication.retrieveBoardDetailsThroughModCode(uuid1);
        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .get(subUrl + "api/board/moderator?code=" + uuid1).called();
    }

    // Test if the retrieveBoardDetailsThroughModCode method returns null after being called with an
    // invalid moderator code, and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testRetrieveBoardDetailsThroughInValidModCode() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/moderator?code=" + uuid1)
                .doReturnStatus(404).doReturn(failureToken);
        // Act
        String responseBody = QuestionBoardCommunication.retrieveBoardDetailsThroughModCode(uuid1);
        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/moderator?code=" + uuid1).called();
    }

    // Test if the retrieveBoardDetailsThroughModCode method returns the successToken after being called
    // with a valid moderator code, and receiving statusCode 200 and the successToken as the response body.
    @Test
    public void testRetrieveBoardDetailsThroughModCodeGivingCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/moderator?code=" + uuid1).doReturn(successToken);
        // Act
        String responseBody = QuestionBoardCommunication.retrieveBoardDetailsThroughModCode(uuid1);
        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/moderator?code=" + uuid1).called();
    }

    // Test if the retrieveBoardDetails method returns a non-null response body after being called
    // with an invalid board ID and a valid moderator code, and receiving statusCode 404. The invalid
    // board ID will cause a 404 statusCode to be returned in the response, then the retrieveBoardDetails
    // method will call the testRetrieveBoardDetailsThroughModCode method using the same UUID to see if it
    // a valid moderator code. In this case, since it is a valid moderator code, a non-null response body
    // will be returned.
    @Test
    public void testRetrieveBoardDetailsThroughInvalidBoardIdAndValidModCode() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/moderator?code=" + uuid1)
                .doReturnStatus(200);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1).doReturnStatus(404);
        // Act
        String responseBody = QuestionBoardCommunication.retrieveBoardDetails(uuid1);

        // Assert
        assertNotNull(responseBody);
        // Verify if the requests were truly made
        httpClientMock.verify().get(subUrl + "api/board/moderator?code=" + uuid1).called();
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1).called();
    }

    // Test if the retrieveQuestions method returns null after being called with an invalid
    // board ID, and receiving statusCode 400 and failureToken as the response body.
    @Test
    public void testRetrieveQuestionsWithInvalidBoardId() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/questions")
                .doReturnStatus(400).doReturn(failureToken);
        // Act
        String responseBody = QuestionBoardCommunication.retrieveQuestions(uuid1);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/questions").called();
    }

    // Test if the retrieveQuestions method returns null after being called with an invalid
    // board ID, and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testRetrieveQuestionsNotFound() {
        // Act
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/questions")
                .doReturnStatus(404).doReturn(failureToken);
        // Act
        String responseBody = QuestionBoardCommunication.retrieveQuestions(uuid1);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/questions").called();
    }

    // Test if the retrieveQuestions method returns a non-null response body after being
    // called with a valid board ID, and receiving statusCode 200 and questionsStr as the
    // response body.
    @Test
    public void testRetrieveQuestionsGivingCorrectResponseBody() {
        // Arrange
        QuestionDetailsDto[] questions = new QuestionDetailsDto[2];
        questions[0] = new QuestionDetailsDto();
        questions[1] = new QuestionDetailsDto();
        String questionsStr = gson.toJson(questions);
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/questions")
                .doReturn(questionsStr);
        // Act
        String responseBody = QuestionBoardCommunication.retrieveQuestions(uuid1);

        // Assert
        assertEquals(questionsStr, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/questions").called();
    }
}
