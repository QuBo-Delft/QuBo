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

public class ServerCommunicationTest {
    
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

    // Test if the sendRequest method catches the exception caused by a refused connection.
    @Test
    public void testSendRequest() {
        assertNull(ServerCommunication.retrieveBoardDetails(uuid1));
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
        String responseBody = ServerCommunication.createBoardRequest(board);
        
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
        String responseBody = ServerCommunication.createBoardRequest(board);

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
        String responseBody = ServerCommunication.createBoardRequest(board);

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
        String responseBody = ServerCommunication.closeBoardRequest(uuid1, uuid3);

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
        String responseBody = ServerCommunication.closeBoardRequest(uuid1, uuid3);

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
        String responseBody = ServerCommunication.closeBoardRequest(uuid1, uuid3);

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
        String responseBody = ServerCommunication.retrieveBoardDetails(uuid1);
        
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
        String responseBody = ServerCommunication.retrieveBoardDetails(uuid1);

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
        String responseBody = ServerCommunication.retrieveBoardDetails(uuid1);
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
        String responseBody = ServerCommunication.retrieveBoardDetailsThroughModCode(uuid1);
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
        String responseBody = ServerCommunication.retrieveBoardDetailsThroughModCode(uuid1);
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
        String responseBody = ServerCommunication.retrieveBoardDetailsThroughModCode(uuid1);
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
        String responseBody = ServerCommunication.retrieveBoardDetails(uuid1);

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
        String responseBody = ServerCommunication.retrieveQuestions(uuid1);

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
        String responseBody = ServerCommunication.retrieveQuestions(uuid1);

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
        String responseBody = ServerCommunication.retrieveQuestions(uuid1);

        // Assert
        assertEquals(questionsStr, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/questions").called();
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
        String responseBody = ServerCommunication
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
        String responseBody = ServerCommunication
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
        String responseBody = ServerCommunication
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
        String responseBody = ServerCommunication
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
        String responseBody = ServerCommunication
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
        String responseBody = ServerCommunication
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
        String responseBody = ServerCommunication.deleteQuestion(uuid1, uuid2);

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
        String responseBody = ServerCommunication.deleteQuestion(uuid1, uuid2);

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
        String responseBody = ServerCommunication.deleteQuestion(uuid1, uuid2);

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
        String responseBody = ServerCommunication.markQuestionAsAnswered(uuid1, uuid2);

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
        String responseBody = ServerCommunication.markQuestionAsAnswered(uuid1, uuid2);

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
        String responseBody = ServerCommunication.markQuestionAsAnswered(uuid1, uuid2);

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
        String responseBody = ServerCommunication.addQuestionVote(uuid1);

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
        String responseBody = ServerCommunication.addQuestionVote(uuid3);

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
        String responseBody = ServerCommunication.addQuestionVote(uuid3);

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
        String responseBody = ServerCommunication.deleteQuestionVote(uuid1, uuid2);

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
        String responseBody = ServerCommunication.deleteQuestionVote(uuid1, uuid2);

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
        String responseBody = ServerCommunication.deleteQuestionVote(uuid1, uuid2);

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
        String responseBody = ServerCommunication.addPaceVote(uuid1, pt);

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
        String responseBody = ServerCommunication.addPaceVote(uuid1, pt);

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
        String responseBody = ServerCommunication.addPaceVote(uuid1, pt);

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
        String responseBody = ServerCommunication.deletePaceVote(uuid1, uuid2);

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
        String responseBody = ServerCommunication.deletePaceVote(uuid1, uuid2);

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
        String responseBody = ServerCommunication.deletePaceVote(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/board/" + uuid1 + "/pace/" + uuid2).called();
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
        String responseBody = ServerCommunication.addPoll(uuid1, uuid2, "Test Poll", pollOptions);

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
        String responseBody = ServerCommunication.addPoll(uuid1, uuid2, "Test Poll", pollOptions);

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
        String responseBody = ServerCommunication.addPoll(uuid1, uuid2, "Test Poll", pollOptions);

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
        String responseBody = ServerCommunication.retrievePollDetails(uuid1);

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
        String responseBody = ServerCommunication.retrievePollDetails(uuid1);

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
        String responseBody = ServerCommunication.retrievePollDetails(uuid2);

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
        String responseBody = ServerCommunication.retrievePollResults(uuid1);

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
        String responseBody = ServerCommunication.retrievePollResults(uuid1);

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
        String responseBody = ServerCommunication.retrievePollResults(uuid1);

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
        String responseBody = ServerCommunication.retrievePollResults(uuid1);

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
        String responseBody = ServerCommunication.deletePoll(uuid1, uuid2);

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
        String responseBody = ServerCommunication.deletePoll(uuid1, uuid2);

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
        String responseBody = ServerCommunication.deletePoll(uuid1, uuid2);

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
        String responseBody = ServerCommunication.deletePoll(uuid1, uuid2);

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
        String responseBody = ServerCommunication.deletePoll(uuid1, uuid2);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "api/board/" + uuid1 + "/poll?code=" + uuid2).called();
    }
}
