package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgssoft.httpclient.HttpClientMock;
import nl.tudelft.oopp.demo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.demo.dtos.pacevote.PaceVoteDetailsDto;
import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionvote.QuestionVoteDetailsDto;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

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

    // Test if the sendRequest method catches the exception caused by not setting up a response.
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
        HttpClientMock httpClientMock = new HttpClientMock();

        // Act
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board")
                .doReturnStatus(200);

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
        HttpClientMock httpClientMock = new HttpClientMock();

        // Act
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board")
                .doReturnStatus(400).doReturn(failureToken);

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
        HttpClientMock httpClientMock = new HttpClientMock();

        // Act
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board").doReturn(successToken);

        String responseBody = ServerCommunication.createBoardRequest(board);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "api/board").called();
    }

    // Test if the closeBoardRequest method returns a non-null response body after being
    // called with valid board Id (boardId) and moderator code (modCode) then receiving
    // statusCode 200.
    @Test
    public void testCloseBoardRequest() {
        // Arrange
        HttpClientMock httpClientMock = new HttpClientMock();
        // Act
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3)
                .doReturnStatus(200);

        String responseBody = ServerCommunication.closeBoardRequest(uuid1, uuid3);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3).called();
    }

    // Test if the closeBoardRequest method returns null after being called with invalid boardId and
    // modCode, and receiving statusCode 400 and failureToken as response body.
    @Test
    public void testCloseBoardRequestInvalidCode() {
        // Arrange
        HttpClientMock httpClientMock = new HttpClientMock();
        // Act
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3)
                .doReturnStatus(400).doReturn(failureToken);

        String responseBody = ServerCommunication.closeBoardRequest(uuid1, uuid3);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3).called();
    }

    // Test if the closeBoardRequest method returns the successToken after being called with
    // valid boardId and modCode, then receiving statusCode 200 and the successToken as response body.
    @Test
    public void testCloseBoardRequestGivingCorrectResponseBody() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3)
                .doReturn(successToken);

        String responseBody = ServerCommunication.closeBoardRequest(uuid1, uuid3);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "api/board/" + uuid1 + "/close?code=" + uuid3).called();
    }

    // Test if the retrieveBoardDetails method returns a non-null response body
    // after being called with a valid boardId and receiving statusCode 200.
    @Test
    public void testRetrieveBoardDetails() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1).doReturnStatus(200);
        String responseBody = ServerCommunication.retrieveBoardDetails(uuid1);
        
        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1).called();
    }

    // Test if the retrieveBoardDetails method returns null after being called with an invalid boardId,
    // and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testRetrieveBoardDetailsThroughInvalidBoardId() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1)
                .doReturnStatus(400);
        String responseBody = ServerCommunication.retrieveBoardDetails(uuid1);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1).called();
    }

    // Test if the retrieveBoardDetails method returns the successToken after being called
    // with a valid BoardId, and receiving statusCode 200 and the successToken as the response body.
    @Test
    public void testRetrieveBoardDetailsGivingCorrectResponseBody() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1).doReturn(successToken);
        String responseBody = ServerCommunication.retrieveBoardDetails(uuid1);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1).called();
    }

    // Test if the retrieveBoardDetailsThroughModCode method returns a non-null response body
    // after being called with a valid ModCode and receiving statusCode 200.
    @Test
    public void testRetrieveBoardDetailsThroughModCode() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "/api/board/moderator?code=" + uuid1)
                .doReturnStatus(200);

        String responseBody = ServerCommunication.retrieveBoardDetailsThroughModCode(uuid1);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .get(subUrl + "/api/board/moderator?code=" + uuid1).called();
    }

    // Test if the retrieveBoardDetailsThroughModCode method returns null after being called with an invalid
    // ModCode, and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testRetrieveBoardDetailsThroughInValidModCode() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "/api/board/moderator?code=" + uuid1)
                .doReturnStatus(404).doReturn(failureToken);

        String responseBody = ServerCommunication.retrieveBoardDetailsThroughModCode(uuid1);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "/api/board/moderator?code=" + uuid1).called();
    }

    // Test if the retrieveBoardDetailsThroughModCode method returns the successToken after being called
    // with a valid ModCode, and receiving statusCode 200 and the successToken as the response body.
    @Test
    public void testRetrieveBoardDetailsThroughModCodeGivingCorrectResponseBody() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "/api/board/moderator?code=" + uuid1).doReturn(successToken);
        String responseBody = ServerCommunication.retrieveBoardDetailsThroughModCode(uuid1);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "/api/board/moderator?code=" + uuid1).called();
    }

    // Test if the retrieveBoardDetails method returns a non-null response body after being called
    // with invalid boardId and valid ModCode, and receiving statusCode 404.
    @Test
    public void testRetrieveBoardDetailsThroughInvalidBoardIdAndValidModCode() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);

        httpClientMock.onGet(subUrl + "/api/board/moderator?code=" + uuid1)
                .doReturnStatus(200);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1).doReturnStatus(404);

        String responseBody = ServerCommunication.retrieveBoardDetails(uuid1);

        // Assert
        assertNotNull(responseBody);
        // Verify if the requests were truly made
        httpClientMock.verify().get(subUrl + "/api/board/moderator?code=" + uuid1).called();
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1).called();
    }

    // Test if the retrieveQuestions method returns null after being called with an invalid
    // boardId, and receiving statusCode 400 and failureToken as the response body.
    @Test
    public void testRetrieveQuestionsWithInvalidBoardId() {
        // Arrange
        HttpClientMock httpClientMock = new HttpClientMock();
        // Act
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/questions")
                .doReturnStatus(400).doReturn(failureToken);

        String responseBody = ServerCommunication.retrieveQuestions(uuid1);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/questions").called();
    }

    // Test if the retrieveQuestions method returns null after being called with an invalid
    // boardId, and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testRetrieveQuestionsNotFound() {
        // Arrange
        HttpClientMock httpClientMock = new HttpClientMock();
        // Act
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/questions")
                .doReturnStatus(404).doReturn(failureToken);
        String responseBody = ServerCommunication.retrieveQuestions(uuid1);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/questions").called();
    }

    // Test if the retrieveQuestions method returns a non-null response body after being
    // called with a valid boardId, and receiving statusCode 200 and questionsStr as the
    // response body.
    @Test
    public void testRetrieveQuestionsGivingCorrectResponseBody() {
        // Arrange
        QuestionDetailsDto[] questions =
                {new QuestionDetailsDto(), new QuestionDetailsDto()};
        String questionsStr = gson.toJson(questions);
        HttpClientMock httpClientMock = new HttpClientMock();

        // Act
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onGet(subUrl + "api/board/" + uuid1 + "/questions")
                .doReturn(questionsStr);
        String responseBody = ServerCommunication.retrieveQuestions(uuid1);

        // Assert
        assertEquals(questionsStr, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().get(subUrl + "api/board/" + uuid1 + "/questions").called();
    }

    // Test if the addQuestion method returns a non-null response body after being called
    // with a valid boardId, and receiving statusCode 200.
    @Test
    public void testAddQuestionValidBoard() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/question")
                .doReturnStatus(200);

        String responseBody = ServerCommunication.
                addQuestion(uuid1, "Why is CO so confusing?", "Koen");

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .post(subUrl + "api/board/" + uuid1 + "/question").called();
    }

    // Test if the addQuestion method returns null after being called with invalid
    // questionId and modCode, and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testAddQuestionInvalidBoard() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/question")
                .doReturnStatus(404).doReturn(failureToken);

        String responseBody = ServerCommunication
                .addQuestion(uuid1, "Why is CO so confusing?", "Koen");

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .post(subUrl + "api/board/" + uuid1 + "/question").called();
    }

    // Test if the addQuestion method returns the successToken after being called
    // with valid questionId and modCode, and receiving statusCode 200 and the successToken
    // as the response body.
    @Test
    public void testAddQuestionGivingCorrectResponseBody() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "api/board/" + uuid1 + "/question")
                .doReturn(successToken);

        String responseBody = ServerCommunication
                .addQuestion(uuid1, "Why is CO so confusing?", "Koen");

        //Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .post(subUrl + "api/board/" + uuid1 + "/question").called();
    }

    // Test if the editQuestion method returns a non-null response body after being called
    // with valid questionId and modCode and receiving statusCode 200.
    @Test
    public void testEditQuestionWithValidCode() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPut(subUrl + "/api/question/" + uuid1 + "?code=" + uuid2)
                .doReturnStatus(200);

        String responseBody = ServerCommunication
                .editQuestion(uuid1, uuid2, "Why is CO taught so clear");

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .put(subUrl + "/api/question/" + uuid1 + "?code=" + uuid2).called();
    }

    // Test if the editQuestion method returns null after being called with invalid
    // questionId and modCode and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testEditQuestionWithInvalidCode() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPut(subUrl + "/api/question/" + uuid1 + "?code=" + uuid3)
                .doReturnStatus(404).doReturn(failureToken);

        String responseBody = ServerCommunication
                .editQuestion(uuid1, uuid3, "Why is CO so clear");

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .put(subUrl + "/api/question/" + uuid1 + "?code=" + uuid3).called();
    }

    // Test if the editQuestion method returns the successToken after being called
    // with valid questionId and modCode and receiving statusCode 200 and
    // successToken as the response body.
    @Test
    public void testEditQuestionGivingCorrectResponseBody() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPut(subUrl + "/api/question/" + uuid1 + "?code=" + uuid2)
                .doReturn(successToken);

        String responseBody = ServerCommunication
                .editQuestion(uuid1, uuid2, "Why is CO so clear");

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .put(subUrl + "/api/question/" + uuid1 + "?code=" + uuid2).called();
    }


    // Test if the deleteQuestion method returns a non-null response body after being called
    // with valid questionId and modCode and receiving statusCode 200.
    @Test
    public void testDeleteQuestionValidRequest() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "/api/question/" + uuid1 + "?code=" + uuid2)
                .doReturnStatus(200);

        String responseBody = ServerCommunication.deleteQuestion(uuid1, uuid2);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "/api/question/" + uuid1 + "?code=" + uuid2).called();
    }

    // Test if the deleteQuestion method returns null after being called with invalid
    // questionId and modCode and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testDeleteQuestionInvalidQuBo() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "/api/question/" + uuid1 + "?code=" + uuid2)
                .doReturnStatus(404).doReturn(failureToken);

        String responseBody = ServerCommunication.deleteQuestion(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "/api/question/" + uuid1 + "?code=" + uuid2).called();
    }

    // Test if the deleteQuestion method returns the successToken after being called
    // with valid questionId and modCode and receiving statusCode 200 and the successToken as the response body.
    @Test
    public void testDeleteQuestionGivingCorrectResponseBody() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onDelete(subUrl + "/api/question/" + uuid1 + "?code=" + uuid2)
                .doReturn(successToken);

        String responseBody = ServerCommunication.deleteQuestion(uuid1, uuid2);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .delete(subUrl + "/api/question/" + uuid1 + "?code=" + uuid2).called();
    }

    // Test if the markQuestionAsAnswered method returns a non-null response body after being called
    // with valid questionId and modCode and receiving statusCode 200.
    @Test
    public void testMarkQuestionAsAnsweredThroughValidRequest() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "/api/question/" + uuid1 + "/answer?code=" + uuid2)
                .doReturnStatus(200);

        String responseBody = ServerCommunication.markQuestionAsAnswered(uuid1, uuid2);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "/api/question/" + uuid1 + "/answer?code=" + uuid2).called();
    }

    // Test if the markQuestionAsAnswered method returns null after being called with invalid
    // questionId and modCode and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testMarkQuestionAsAnsweredThroughInvalidRequest() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "/api/question/" + uuid1 + "/answer?code=" + uuid2)
                .doReturnStatus(404).doReturn(failureToken);

        String responseBody = ServerCommunication.markQuestionAsAnswered(uuid1, uuid2);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "/api/question/" + uuid1 + "/answer?code=" + uuid2).called();
    }

    // Test if the markQuestionAsAnswered method returns the successToken after being called
    // with valid questionId and modCode and receiving statusCode 200 and the successToken as the response body.
    @Test
    public void testMarkQuestionAsAnsweredGivingCorrectResponseBody() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPatch(subUrl + "/api/question/" + uuid1 + "/answer?code=" + uuid2)
                .doReturn(successToken).doReturnStatus(200);

        String responseBody = ServerCommunication.markQuestionAsAnswered(uuid1, uuid2);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify()
                .patch(subUrl + "/api/question/" + uuid1 + "/answer?code=" + uuid2).called();
    }

    // Test if the addQuestionVote method returns a non-null response body after being called
    // with a valid questionId and receiving statusCode 200.
    @Test
    public void testAddQuestionVoteThroughValidRequest() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "/api/question/" + uuid1 + "/vote")
                .doReturnStatus(200);

        String responseBody = ServerCommunication.addQuestionVote(uuid1);

        // Assert
        assertNotNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "/api/question/" + uuid1 + "/vote").called();
    }

    // Test if the addQuestionVote method returns null after being called with an invalid
    // questionId and receiving statusCode 404 and failureToken as the response body.
    @Test
    public void testAddQuestionVoteThroughInvalidRequest() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "/api/question/" + uuid3 + "/vote")
                .doReturnStatus(404).doReturn(failureToken);

        String responseBody = ServerCommunication.addQuestionVote(uuid3);

        // Assert
        assertNull(responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "/api/question/" + uuid3 + "/vote").called();
    }

    // Test if the addQuestionVote method returns the successToken after being called
    // with a valid questionId and receiving statusCode 200 and the successToken as the response body.
    @Test
    public void testAddQuestionVoteGivingCorrectResponseBody() {
        // Arrange and Act
        HttpClientMock httpClientMock = new HttpClientMock();
        ServerCommunication.setClient(httpClientMock);
        httpClientMock.onPost(subUrl + "/api/question/" + uuid3 + "/vote")
                .doReturn(successToken);

        String responseBody = ServerCommunication.addQuestionVote(uuid3);

        // Assert
        assertEquals(successToken, responseBody);
        // Verify if the request was truly made
        httpClientMock.verify().post(subUrl + "/api/question/" + uuid3 + "/vote").called();
    }

    //Tests if editQuestion returns true when called through the question secret code.
    //Tests if the question text has been changed.
    @Test
    public void testEditQuestionValidCodeRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");

    }

    //Tests if editQuestion returns false when called through an invalid code.
    //Tests if the question text has remained the same.
    @Test
    public void testEditQuestionInvalidCode() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");


    }

    //Tests if editQuestion returns false when called through a non-existent question ID.
    //Tests if the question text has remained the same.
    @Test
    public void testEditQuestionInvalidQuestion() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");

    }

    //Tests if deleteQuestion returns true when called with an existing question and question board.
    //Tests if the question has been deleted from the list of questions.
    @Test
    public void testDeleteQuestionValidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");


    }

    //Tests if deleteQuestion returns false when called with an existing question, but a non-existent question board.
    //Tests if the question has not been deleted from the list of questions.
    @Test
    public void testDeleteQuestionInvalidQuBo() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");


    }

    //Tests if deleteQuestion returns false when called with an existing question board, but with a non-existent
    //question.
    //Tests if the question has not been deleted from the list of questions.
    @Test
    public void testDeleteQuestionInvalidQuestion() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");


    }

    @Test
    public void testRandomQuote() {

        assertNotNull(ServerCommunication.getQuote());
    }
}
