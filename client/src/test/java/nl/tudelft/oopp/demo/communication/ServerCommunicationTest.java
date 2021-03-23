package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.*;

import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    @Test
    public void testRandomQuote() {

        assertNotNull(ServerCommunication.getQuote());
    }
}
