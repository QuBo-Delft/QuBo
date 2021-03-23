package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.*;

import nl.tudelft.oopp.demo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.question.QuestionCreationDto;
import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.UUID;

//To run these tests, one must first run DemoApplication.java in the server folder as these tests require
//a connection to the server.
public class ServerCommunicationTest {

    //Tests if the createBoardRequest method returns a QuestionBoardCreationDto with the same
    //attribute values as the QuestionBoardCreationBindingModel used to call the method with.
    @Test
    public void testCreateBoardRequestValidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        //Act
        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        //Assert
        assertEquals(quBoModel.getStartTime(), quBo.getStartTime());
        assertEquals(quBoModel.getTitle(), quBo.getTitle());
    }

    //Tests if the createBoardRequest method returns null when called without a correct
    //QuestionBoardCreationBindingModel.
    @Test
    public void testCreateBoardRequestInvalidRequest() {
        //Act
        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(null);

        //Assert
        assertNull(quBo);
    }

    //Test if the closeBoardRequest method returns true if it is called with the ID and moderator
    //code of an existing question board.
    @Test
    public void testCloseBoardRequestValidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        //Act
        boolean isClosed = ServerCommunication.closeBoardRequest(quBo.getId(), quBo.getModeratorCode());

        //Assert
        assertTrue(isClosed);
    }

    //Test if the closeBoardRequest method returns false if it is called with the ID of an existing
    //question board, but with an invalid code.
    @Test
    public void testCloseBoardRequestInvalidCode() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        //Act
        boolean isClosed = ServerCommunication.closeBoardRequest(quBo.getId(), UUID.randomUUID());

        //Assert
        assertFalse(isClosed);
    }

    //Test if the closeBoardRequest method returns false if it is called with UUIDs that do not
    //correspond to those of an existing question board nor its moderator code.
    @Test
    public void testCloseBoardRequestInvalidRequest() {
        //Act
        boolean isClosed = ServerCommunication.closeBoardRequest(UUID.randomUUID(), UUID.randomUUID());

        //Assert
        assertFalse(isClosed);
    }

    //Test if the retrieveBoardDetailsThroughModCode method returns a QuestionBoardDetailsDto
    //that corresponds to that of the created board.
    @Test
    public void testRetrieveBoardDetailsThroughModCodeValidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        //Act
        QuestionBoardDetailsDto quBoDetails = ServerCommunication
                .retrieveBoardDetailsThroughModCode(quBo.getModeratorCode());

        //Assert
        assertEquals(quBo.getId(), quBoDetails.getId());
        assertEquals(quBo.getStartTime(), quBoDetails.getStartTime());
        assertEquals(quBo.getTitle(), quBoDetails.getTitle());
    }

    //Test if the retrieveBoardDetailsThroughModCode method returns null when called with a code that does not
    //correspond to a moderator code of a question board.
    @Test
    public void testRetrieveBoardDetailsThroughModCodeInvalidCode() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        //Act
        QuestionBoardDetailsDto quBoDetails = ServerCommunication
                .retrieveBoardDetailsThroughModCode(UUID.randomUUID());

        //Assert
        assertNull(quBoDetails);
    }

    //Test if the retrieveBoardDetails method returns a QuestionBoardDetailsDto that corresponds to that of the
    //created board.
    @Test
    public void testRetrieveBoardDetailsValidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        //Act
        QuestionBoardDetailsDto quBoDetails = ServerCommunication
                .retrieveBoardDetails(quBo.getId());

        //Assert
        assertEquals(quBo.getId(), quBoDetails.getId());
        assertEquals(quBo.getStartTime(), quBoDetails.getStartTime());
        assertEquals(quBo.getTitle(), quBoDetails.getTitle());
    }

    //Test if the retrieveBoardDetails method returns a QuestionBoardDetailsDto that corresponds to that of the
    //created board when called through the moderator code.
    @Test
    public void testRetrieveBoardDetailsModCodeRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        //Act
        QuestionBoardDetailsDto quBoDetails = ServerCommunication
                .retrieveBoardDetails(quBo.getModeratorCode());

        //Assert
        assertEquals(quBo.getId(), quBoDetails.getId());
        assertEquals(quBo.getStartTime(), quBoDetails.getStartTime());
        assertEquals(quBo.getTitle(), quBoDetails.getTitle());
    }

    //Test if the retrieveBoardDetails method returns null when called with an invalid UUID
    @Test
    public void testRetrieveBoardDetailsInvalidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        //Act
        QuestionBoardDetailsDto quBoDetails = ServerCommunication
                .retrieveBoardDetails(UUID.randomUUID());

        //Assert
        assertNull(quBoDetails);
    }

    //Tests if retrieveQuestions returns an empty array when called through a valid board ID without
    //any questions.
    @Test
    public void testRetrieveQuestionsEmptyArray() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        //Act
        QuestionDetailsDto[] questions = ServerCommunication.retrieveQuestions(quBo.getId());

        //Assert
        assertNotNull(questions);
        assertTrue(questions.length == 0);
    }

    //Tests if retrieveQuestions returns an array with all added questions when called through a valid board ID
    //with questions added.
    @Test
    public void testRetrieveQuestionsCorrectArray() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);
        QuestionCreationDto question = ServerCommunication
                .addQuestion(quBo.getId(), "Test Question?", "author");
        QuestionCreationDto question2 = ServerCommunication
                .addQuestion(quBo.getId(), "Test Question?", "author2");

        UUID questionId = question.getId();
        UUID secretCode = question.getSecretCode();
        UUID question2Id = question2.getId();
        UUID secretCode2 = question2.getSecretCode();

        //Act
        QuestionDetailsDto[] questions = ServerCommunication.retrieveQuestions(quBo.getId());

        //Assert
        assertNotNull(questions);
        assertTrue(questions.length == 2);
        assertTrue(questions[0] != questions[1]);
        assertTrue(questions[0].getId() == questionId ^ questions[0].getId() == question2Id);
        assertTrue(questions[1].getId() == questionId ^ questions[1].getId() == question2Id);
        assertTrue(Arrays.asList(questions).contains(question2));
    }

    //Tests if retrieveQuestions returns null when called through an invalid board ID.
    @Test
    public void testRetrieveQuestions() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        //Act
        QuestionDetailsDto[] questions = ServerCommunication.retrieveQuestions(UUID.randomUUID());

        //Assert
        assertNull(questions);
    }

    //Tests if addQuestion returns a QuestionCreationDto that corresponds to the text and author the method was
    //called with.
    @Test
    public void testAddQuestionValidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        String text = "Is the universe infinitely large?";
        String author = "Insert Name";

        //Act
        QuestionCreationDto questionCreated = ServerCommunication.addQuestion(quBo.getId(), text, author);

        //Assert
        QuestionDetailsDto[] questionDetails = ServerCommunication.retrieveQuestions(quBo.getId());
        assertTrue(questionDetails[0].getId().equals(questionCreated.getId()));
        assertTrue(questionDetails[0].getAuthorName().equals(author));
        assertTrue(questionDetails[0].getText().equals(text));
    }

    //Tests if addQuestion returns null if the board it was going to be added to did not exist.
    @Test
    public void testAddQuestionInvalidBoard() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        String text = "Is the universe infinitely large?";
        String author = "Insert Name";

        //Act
        QuestionCreationDto questionCreated = ServerCommunication.addQuestion(UUID.randomUUID(), text, author);

        //Assert
        assertNull(questionCreated);
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

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        String text = "Is the universe infinitely large?";
        String author = "Insert Name";

        //Act
        QuestionCreationDto questionCreated = ServerCommunication.addQuestion(quBo.getId(), text, author);

        //Assert
        assertNull(questionCreated);
    }

    //Tests if addQuestion returns null when called with null values for question text and author
    @Test
    public void testAddQuestionInvalidRequest() {
        //Arrange
        QuestionBoardCreationBindingModel quBoModel = new QuestionBoardCreationBindingModel();
        quBoModel.setStartTime(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        quBoModel.setTitle("Test Qubo");

        QuestionBoardCreationDto quBo = ServerCommunication.createBoardRequest(quBoModel);

        //Act
        QuestionCreationDto questionCreated = ServerCommunication.addQuestion(UUID.randomUUID(), null, null);

        //Assert
        assertNull(questionCreated);
    }

    @Test
    public void testRandomQuote() {

        assertNotNull(ServerCommunication.getQuote());
    }
}
