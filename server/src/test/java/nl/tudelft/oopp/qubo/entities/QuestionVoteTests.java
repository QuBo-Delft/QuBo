package nl.tudelft.oopp.qubo.entities;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class QuestionVoteTests {
    @Test
    public void constructor_withNoArguments_initialisesObject() {
        // Arrange

        // Act
        QuestionVote vote = new QuestionVote();

        // Assert
        assertNotNull(vote);
    }

    @Test
    public void constructor_withArguments_initialisesObjectCorrectly() {
        // Arrange
        Question expectedQuestion = new Question();

        // Act
        QuestionVote vote = new QuestionVote(expectedQuestion);

        // Assert
        assertNotNull(vote);
        assertSame(expectedQuestion, vote.getQuestion());
    }

    @Test
    public void getSetId_worksCorrectly() {
        // Arrange
        UUID expected = UUID.randomUUID();
        QuestionVote vote = new QuestionVote();

        // Act
        vote.setId(expected);
        UUID actual = vote.getId();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetQuestion_worksCorrectly() {
        // Arrange
        Question expected = new Question();
        QuestionVote vote = new QuestionVote();

        // Act
        vote.setQuestion(expected);
        Question actual = vote.getQuestion();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void hashCode_withSameProperties_worksCorrectly() {
        // Arrange
        QuestionVote vote1 = new QuestionVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        QuestionVote vote2 = new QuestionVote();
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
        QuestionVote vote1 = new QuestionVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        QuestionVote vote2 = new QuestionVote();
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
        QuestionVote vote1 = new QuestionVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        QuestionVote vote2 = new QuestionVote();
        vote2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = vote1.equals(vote2);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withDifferentProperties_worksCorrectly() {
        // Arrange
        QuestionVote vote1 = new QuestionVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        QuestionVote vote2 = new QuestionVote();
        vote2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = vote1.equals(vote2);

        // Assert
        assertFalse(actual);
    }

    @Test
    public void equals_withSameObject_worksCorrectly() {
        // Arrange
        QuestionVote vote1 = new QuestionVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = vote1.equals(vote1);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withNull_worksCorrectly() {
        // Arrange
        QuestionVote vote1 = new QuestionVote();
        vote1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = vote1.equals(null);

        // Assert
        assertFalse(actual);
    }
}
