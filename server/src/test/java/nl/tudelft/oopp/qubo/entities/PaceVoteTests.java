package nl.tudelft.oopp.qubo.entities;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PaceVoteTests {
    @Test
    public void constructor_withNoArguments_initialisesObject() {
        // Arrange

        // Act
        PaceVote vote = new PaceVote();

        // Assert
        assertNotNull(vote);
    }

    @Test
    public void constructor_withArguments_initialisesObjectCorrectly() {
        // Arrange
        QuestionBoard expectedBoard = new QuestionBoard();
        PaceType expectedPaceType = PaceType.TOO_FAST;

        // Act
        PaceVote vote = new PaceVote(expectedBoard, expectedPaceType);

        // Assert
        assertNotNull(vote);
        assertSame(expectedBoard, vote.getQuestionBoard());
        assertSame(expectedPaceType, vote.getPaceType());
    }

    @Test
    public void getSetId_worksCorrectly() {
        // Arrange
        UUID expected = UUID.randomUUID();
        PaceVote vote = new PaceVote();

        // Act
        vote.setId(expected);
        UUID actual = vote.getId();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetQuestionBoard_worksCorrectly() {
        // Arrange
        QuestionBoard expected = new QuestionBoard();
        PaceVote vote = new PaceVote();

        // Act
        vote.setQuestionBoard(expected);
        QuestionBoard actual = vote.getQuestionBoard();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetPaceType_worksCorrectly() {
        // Arrange
        PaceType expected = PaceType.JUST_RIGHT;
        PaceVote vote = new PaceVote();

        // Act
        vote.setPaceType(expected);
        PaceType actual = vote.getPaceType();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void hashCode_withSameProperties_worksCorrectly() {
        // Arrange
        PaceVote vote1 = new PaceVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        vote1.setPaceType(PaceType.TOO_SLOW);
        PaceVote vote2 = new PaceVote();
        vote2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        vote2.setPaceType(PaceType.TOO_SLOW);

        // Act
        int hashCode1 = vote1.hashCode();
        int hashCode2 = vote2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void hashCode_withDifferentProperties_worksCorrectly() {
        // Arrange
        PaceVote vote1 = new PaceVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        vote1.setPaceType(PaceType.TOO_SLOW);
        PaceVote vote2 = new PaceVote();
        vote2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        vote2.setPaceType(PaceType.TOO_SLOW);

        // Act
        int hashCode1 = vote1.hashCode();
        int hashCode2 = vote2.hashCode();

        // Assert
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void equals_withSameProperties_worksCorrectly() {
        // Arrange
        PaceVote vote1 = new PaceVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        vote1.setPaceType(PaceType.TOO_SLOW);
        PaceVote vote2 = new PaceVote();
        vote2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        vote2.setPaceType(PaceType.TOO_SLOW);

        // Act
        boolean actual = vote1.equals(vote2);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withDifferentProperties_worksCorrectly() {
        // Arrange
        PaceVote vote1 = new PaceVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        vote1.setPaceType(PaceType.TOO_SLOW);
        PaceVote vote2 = new PaceVote();
        vote2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        vote2.setPaceType(PaceType.TOO_SLOW);

        // Act
        boolean actual = vote1.equals(vote2);

        // Assert
        assertFalse(actual);
    }

    @Test
    public void equals_withSameObject_worksCorrectly() {
        // Arrange
        PaceVote vote1 = new PaceVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        vote1.setPaceType(PaceType.TOO_SLOW);

        // Act
        boolean actual = vote1.equals(vote1);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withNull_worksCorrectly() {
        // Arrange
        PaceVote vote1 = new PaceVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        vote1.setPaceType(PaceType.TOO_SLOW);

        // Act
        boolean actual = vote1.equals(null);

        // Assert
        assertFalse(actual);
    }
}
