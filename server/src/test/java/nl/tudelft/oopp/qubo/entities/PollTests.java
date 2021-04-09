package nl.tudelft.oopp.qubo.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PollTests {
    @Test
    public void constructor_withNoArguments_initialisesObject() {
        // Arrange

        // Act
        Poll poll = new Poll();

        // Assert
        assertNotNull(poll);
    }

    @Test
    public void constructor_withArguments_initialisesObjectCorrectly() {
        // Arrange
        String expectedText = "Test";
        QuestionBoard expectedBoard = new QuestionBoard();
        Set<PollOption> expectedOptions = new HashSet<>();

        // Act
        Poll poll = new Poll(expectedText, expectedBoard, expectedOptions);

        // Assert
        assertNotNull(poll);
        assertSame(expectedText, poll.getText());
        assertSame(expectedBoard, poll.getQuestionBoard());
        assertSame(expectedOptions, poll.getPollOptions());
    }

    @Test
    public void getSetId_worksCorrectly() {
        // Arrange
        UUID expected = UUID.randomUUID();
        Poll poll = new Poll();

        // Act
        poll.setId(expected);
        UUID actual = poll.getId();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetQuestionBoard_worksCorrectly() {
        // Arrange
        QuestionBoard expected = new QuestionBoard();
        Poll poll = new Poll();

        // Act
        poll.setQuestionBoard(expected);
        QuestionBoard actual = poll.getQuestionBoard();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetText_worksCorrectly() {
        // Arrange
        String expected = "Test";
        Poll poll = new Poll();

        // Act
        poll.setText(expected);
        String actual = poll.getText();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetOpen_worksCorrectly() {
        // Arrange
        boolean expected = true;
        Poll poll = new Poll();

        // Act
        poll.setOpen(expected);
        boolean actual = poll.isOpen();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetPollOptions_worksCorrectly() {
        // Arrange
        Set<PollOption> expected = new HashSet<>();
        Poll poll = new Poll();

        // Act
        poll.setPollOptions(expected);

        Set<PollOption> actual = poll.getPollOptions();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void hashCode_withSameProperties_worksCorrectly() {
        // Arrange
        Poll poll1 = new Poll();
        poll1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        poll1.setOpen(true);
        poll1.setText("Test");
        Poll poll2 = new Poll();
        poll2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        poll2.setOpen(true);
        poll2.setText("Test");

        // Act
        int hashCode1 = poll1.hashCode();
        int hashCode2 = poll2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void hashCode_withDifferentProperties_worksCorrectly() {
        // Arrange
        Poll poll1 = new Poll();
        poll1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        poll1.setOpen(true);
        poll1.setText("Test");
        Poll poll2 = new Poll();
        poll2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        poll2.setOpen(true);
        poll2.setText("Test");

        // Act
        int hashCode1 = poll1.hashCode();
        int hashCode2 = poll2.hashCode();

        // Assert
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void equals_withSameProperties_worksCorrectly() {
        // Arrange
        Poll poll1 = new Poll();
        poll1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        poll1.setOpen(true);
        poll1.setText("Test");
        Poll poll2 = new Poll();
        poll2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        poll2.setOpen(true);
        poll2.setText("Test");

        // Act
        boolean actual = poll1.equals(poll2);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withDifferentProperties_worksCorrectly() {
        // Arrange
        Poll poll1 = new Poll();
        poll1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        poll1.setOpen(true);
        poll1.setText("Test");
        Poll poll2 = new Poll();
        poll2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        poll2.setOpen(true);
        poll2.setText("Test");

        // Act
        boolean actual = poll1.equals(poll2);

        // Assert
        assertFalse(actual);
    }

    @Test
    public void equals_withSameObject_worksCorrectly() {
        // Arrange
        Poll poll1 = new Poll();
        poll1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        poll1.setOpen(true);
        poll1.setText("Test");

        // Act
        boolean actual = poll1.equals(poll1);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withNull_worksCorrectly() {
        // Arrange
        Poll poll1 = new Poll();
        poll1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        poll1.setOpen(true);
        poll1.setText("Test");

        // Act
        boolean actual = poll1.equals(null);

        // Assert
        assertFalse(actual);
    }
}
