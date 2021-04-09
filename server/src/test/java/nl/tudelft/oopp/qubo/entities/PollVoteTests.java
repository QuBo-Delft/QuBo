package nl.tudelft.oopp.qubo.entities;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PollVoteTests {
    @Test
    public void constructor_withNoArguments_initialisesObject() {
        // Arrange

        // Act
        PollVote vote = new PollVote();

        // Assert
        assertNotNull(vote);
    }

    @Test
    public void constructor_withArguments_initialisesObjectCorrectly() {
        // Arrange
        PollOption expectedOption = new PollOption();

        // Act
        PollVote vote = new PollVote(expectedOption);

        // Assert
        assertNotNull(vote);
        assertSame(expectedOption, vote.getPollOption());
    }

    @Test
    public void getSetId_worksCorrectly() {
        // Arrange
        UUID expected = UUID.randomUUID();
        PollVote vote = new PollVote();

        // Act
        vote.setId(expected);
        UUID actual = vote.getId();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetOption_worksCorrectly() {
        // Arrange
        PollOption expected = new PollOption();
        PollVote vote = new PollVote();

        // Act
        vote.setPollOption(expected);
        PollOption actual = vote.getPollOption();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void hashCode_withSameProperties_worksCorrectly() {
        // Arrange
        PollVote vote1 = new PollVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        PollVote vote2 = new PollVote();
        vote2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        int hashCode1 = vote1.hashCode();
        int hashCode2 = vote2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void hashCode_withDifferentProperties_worksCorrectly() {
        // Arrange
        PollVote vote1 = new PollVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        PollVote vote2 = new PollVote();
        vote2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        int hashCode1 = vote1.hashCode();
        int hashCode2 = vote2.hashCode();

        // Assert
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void equals_withSameProperties_worksCorrectly() {
        // Arrange
        PollVote vote1 = new PollVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        PollVote vote2 = new PollVote();
        vote2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = vote1.equals(vote2);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withDifferentProperties_worksCorrectly() {
        // Arrange
        PollVote vote1 = new PollVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        PollVote vote2 = new PollVote();
        vote2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = vote1.equals(vote2);

        // Assert
        assertFalse(actual);
    }

    @Test
    public void equals_withSameObject_worksCorrectly() {
        // Arrange
        PollVote vote1 = new PollVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = vote1.equals(vote1);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withNull_worksCorrectly() {
        // Arrange
        PollVote vote1 = new PollVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = vote1.equals(null);

        // Assert
        assertFalse(actual);
    }
}
