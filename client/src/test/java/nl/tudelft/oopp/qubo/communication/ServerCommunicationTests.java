package nl.tudelft.oopp.qubo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgssoft.httpclient.HttpClientMock;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ServerCommunicationTests {
    
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






    // Test if the addQuestion method returns a non-null response body after being called
    // with a valid board ID, and receiving statusCode 200.
    @Test
    public void testAddQuestionValidBoard() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/question")
                .doReturnStatus(200);
        // Act
        String responseBody = QuestionCommunication
                .addQuestion(uuid1, "Why is CO so confusing?", "Koen");

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .post(subUrl + "api/board/" + uuid1 + "/question").called();
    }

    // Test if the addQuestion method returns null after being called with an invalid
    // questionId and moderator code, and receiving statusCode 404 and failureToken as
    // the response body.
    @Test
    public void testAddQuestionInvalidBoard() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/question")
                .doReturnStatus(404).doReturn(failureToken);
        // Act
        String responseBody = QuestionCommunication
                .addQuestion(uuid1, "Why is CO so confusing?", "Koen");

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .post(subUrl + "api/board/" + uuid1 + "/question").called();
    }

    // Test if the addQuestion method returns the successToken after being called
    // with a valid questionId and moderator code, and receiving statusCode 200 and the successToken
    // as the response body.
    @Test
    public void testAddQuestionGivingCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/question")
                .doReturn(successToken);
        // Act
        String responseBody = QuestionCommunication
                .addQuestion(uuid1, "Why is CO so confusing?", "Koen");

        //Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .post(subUrl + "api/board/" + uuid1 + "/question").called();
    }

    // Test if the editQuestion method returns a non-null response body after being called
    // with a valid questionId and moderator code and receiving statusCode 200.
    @Test
    public void testEditQuestionWithValidCode() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPut(subUrl + "api/question/" + uuid1 + "?code=" + uuid2)
                .doReturnStatus(200);
        // Act
        String responseBody = QuestionCommunication
                .editQuestion(uuid1, uuid2, "Why is CO taught so clear");

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .put(subUrl + "api/question/" + uuid1 + "?code=" + uuid2).called();
    }

    // Test if the editQuestion method returns null after being called with an invalid
    // questionId and moderator code and receiving statusCode 404 and failureToken as the
    // response body.
    @Test
    public void testEditQuestionWithInvalidCode() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPut(subUrl + "api/question/" + uuid1 + "?code=" + uuid3)
                .doReturnStatus(404).doReturn(failureToken);
        // Act
        String responseBody = QuestionCommunication
                .editQuestion(uuid1, uuid3, "Why is CO so clear");

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .put(subUrl + "api/question/" + uuid1 + "?code=" + uuid3).called();
    }

    // Test if the editQuestion method returns the successToken after being called
    // with a valid questionId and moderator code and receiving statusCode 200 and
    // successToken as the response body.
    @Test
    public void testEditQuestionGivingCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPut(subUrl + "api/question/" + uuid1 + "?code=" + uuid2)
                .doReturn(successToken);
        // Act
        String responseBody = QuestionCommunication
                .editQuestion(uuid1, uuid2, "Why is CO so clear");

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .put(subUrl + "api/question/" + uuid1 + "?code=" + uuid2).called();
    }


    // Test if the deleteQuestion method returns a non-null response body after being called
    // with a valid questionId and moderator code and receiving statusCode 200.
    @Test
    public void testDeleteQuestionValidRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/question/" + uuid1 + "?code=" + uuid2)
                .doReturnStatus(200);
        // Act
        String responseBody = QuestionCommunication.deleteQuestion(uuid1, uuid2);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/question/" + uuid1 + "?code=" + uuid2).called();
    }

    // Test if the deleteQuestion method returns null after being called with an invalid
    // questionId and moderator code and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testDeleteQuestionInvalidQuBo() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/question/" + uuid1 + "?code=" + uuid2)
                .doReturnStatus(404).doReturn(failureToken);
        // Act
        String responseBody = QuestionCommunication.deleteQuestion(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/question/" + uuid1 + "?code=" + uuid2).called();
    }

    // Test if the deleteQuestion method returns the successToken after being called
    // with a valid questionId and moderator code and receiving statusCode 200 and the successToken
    // as the response body.
    @Test
    public void testDeleteQuestionGivingCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "api/question/" + uuid1 + "?code=" + uuid2)
                .doReturn(successToken);
        // Act
        String responseBody = QuestionCommunication.deleteQuestion(uuid1, uuid2);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/question/" + uuid1 + "?code=" + uuid2).called();
    }

    // Test if the markQuestionAsAnswered method returns a non-null response body after being called
    // with a valid questionId and moderator code and receiving statusCode 200.
    @Test
    public void testMarkQuestionAsAnsweredThroughValidRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2)
                .doReturnStatus(200);
        // Act
        String responseBody = QuestionCommunication.markQuestionAsAnswered(uuid1, uuid2);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2).called();
    }

    // Test if the markQuestionAsAnswered method returns null after being called with an invalid
    // questionId and moderator code and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testMarkQuestionAsAnsweredThroughInvalidRequest() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2)
                .doReturnStatus(404).doReturn(failureToken);
        // Act
        String responseBody = QuestionCommunication.markQuestionAsAnswered(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2).called();
    }

    // Test if the markQuestionAsAnswered method returns the successToken after being called
    // with a valid questionId and moderator code and receiving statusCode 200 and the successToken
    // as the response body.
    @Test
    public void testMarkQuestionAsAnsweredGivingCorrectResponseBody() {
        // Arrange
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2)
                .doReturn(successToken);
        // Act
        String responseBody = QuestionCommunication.markQuestionAsAnswered(uuid1, uuid2);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "api/question/" + uuid1 + "/answer?code=" + uuid2).called();
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
