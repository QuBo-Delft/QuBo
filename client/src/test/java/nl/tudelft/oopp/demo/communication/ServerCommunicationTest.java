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

    //Test if the closeBoardRequest method returns false if it is called with UUIDs that do not
    //correspond to those of an existing question board nor its moderator code.
    @Test
    public void testCloseBoardRequestInvalidRequest() {
        //Act
//        boolean isClosed = ServerCommunication.closeBoardRequest(UUID.randomUUID(), UUID.randomUUID());

        //Assert

    }

    //Test if the retrieveBoardDetailsThroughModCode method returns a QuestionBoardDetailsDto
    //that corresponds to that of the created board.
    @Test
    public void testRetrieveBoardDetailsThroughModCodeValidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");


    }

    //Test if the retrieveBoardDetailsThroughModCode method returns null when called with a code that does not
    //correspond to a moderator code of a question board.
    @Test
    public void testRetrieveBoardDetailsThroughModCodeInvalidCode() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");


    }

    //Test if the retrieveBoardDetails method returns a QuestionBoardDetailsDto that corresponds to that of the
    //created board.
    @Test
    public void testRetrieveBoardDetailsValidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");


    }

    //Test if the retrieveBoardDetails method returns a QuestionBoardDetailsDto that corresponds to that of the
    //created board when called through the moderator code.
    @Test
    public void testRetrieveBoardDetailsModCodeRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

    }

    //Test if the retrieveBoardDetails method returns null when called with an invalid UUID
    @Test
    public void testRetrieveBoardDetailsInvalidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");


    }

    //Tests if retrieveQuestions returns an empty array when called through a valid board ID without
    //any questions.
    @Test
    public void testRetrieveQuestionsEmptyArray() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");


        //Assert

    }

    //Tests if retrieveQuestions returns an array with all added questions when called through a valid board ID
    //with questions added.
    @Test
    public void testRetrieveQuestionsCorrectArray() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");


        UUID questionId = question.getId();
        UUID question2Id = question2.getId();

        //Act

        //Assert

    }

    //Tests if retrieveQuestions returns null when called through an invalid board ID.
    @Test
    public void testRetrieveQuestions() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");


        //Act


        //Assert

    }

    //Tests if addQuestion returns a QuestionCreationDto that corresponds to the text and author the method was
    //called with.
    @Test
    public void testAddQuestionValidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");

        String text = "Is the universe infinitely large?";
        String author = "Insert Name";

        //Act

        //Assert

    }

    //Tests if addQuestion returns null if the board it was going to be added to did not exist.
    @Test
    public void testAddQuestionInvalidBoard() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");


    }

    //Tests if addQuestion returns null if the caller attempted to post a question before the start time
    //of the board.
    @Test
    public void testAddQuestionInvalidQuestion() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        Timestamp now = Timestamp.from(Instant.now());
        now.setTime(now.getTime() + (40 * 60 * 1000));
        quBoModel.setStartTime(now);
        quBoModel.setTitle("Test Qubo");

    }

    //Tests if addQuestion returns null when called with null values for question text and author.
    @Test
    public void testAddQuestionInvalidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");


    }

    //Tests if editQuestion returns true when called through the moderator code.
    //Tests if the question text has been changed.
    @Test
    public void testEditQuestionValidModRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now()));
        quBoModel.setTitle("Test Qubo");


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
