package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.*;

import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


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

    @Test
    public void testRandomQuote() {

        assertNotNull(ServerCommunication.getQuote());
    }
}
