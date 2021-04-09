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

public class PollOptionTests {
    @Test
    public void constructor_withNoArguments_initialisesObject() {
        // Arrange

        // Act
        PollOption option = new PollOption();

        // Assert
        assertNotNull(option);
    }

    @Test
    public void constructor_withArguments_initialisesObjectCorrectly() {
        // Arrange
        Poll expectedPoll = new Poll();
        String expectedText = "Test";

        // Act
        PollOption option = new PollOption(expectedPoll, expectedText);

        // Assert
        assertNotNull(option);
        assertSame(expectedPoll, option.getPoll());
        assertSame(expectedText, option.getText());
    }

    @Test
    public void getSetId_worksCorrectly() {
        // Arrange
        UUID expected = UUID.randomUUID();
        PollOption option = new PollOption();

        // Act
        option.setId(expected);
        UUID actual = option.getId();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetPoll_worksCorrectly() {
        // Arrange
        Poll expected = new Poll();
        PollOption option = new PollOption();

        // Act
        option.setPoll(expected);
        Poll actual = option.getPoll();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetText_worksCorrectly() {
        // Arrange
        String expected = "Test";
        PollOption option = new PollOption();

        // Act
        option.setText(expected);
        String actual = option.getText();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetVotes_worksCorrectly() {
        // Arrange
        Set<PollVote> expected = new HashSet<>();
        PollOption option = new PollOption();

        // Act
        option.setVotes(expected);
        Set<PollVote> actual = option.getVotes();

        // Assert
        assertSame(expected, actual);
    }


    @Test
    public void hashCode_withSameProperties_worksCorrectly() {
        // Arrange
        PollOption option1 = new PollOption();
        option1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        option1.setText("Test");
        PollOption option2 = new PollOption();
        option2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        option2.setText("Test");

        // Act
        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void hashCode_withDifferentProperties_worksCorrectly() {
        // Arrange
        PollOption option1 = new PollOption();
        option1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        option1.setText("Test");
        PollOption option2 = new PollOption();
        option2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        option2.setText("Test");

        // Act
        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();

        // Assert
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void equals_withSameProperties_worksCorrectly() {
        // Arrange
        PollOption option1 = new PollOption();
        option1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        option1.setText("Test");
        PollOption option2 = new PollOption();
        option2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        option2.setText("Test");

        // Act
        boolean actual = option1.equals(option2);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withDifferentProperties_worksCorrectly() {
        // Arrange
        PollOption option1 = new PollOption();
        option1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        option1.setText("Test");
        PollOption option2 = new PollOption();
        option2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        option2.setText("Test");

        // Act
        boolean actual = option1.equals(option2);

        // Assert
        assertFalse(actual);
    }

    @Test
    public void equals_withSameObject_worksCorrectly() {
        // Arrange
        PollOption option1 = new PollOption();
        option1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        option1.setText("Test");

        // Act
        boolean actual = option1.equals(option1);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withNull_worksCorrectly() {
        // Arrange
        PollOption option1 = new PollOption();
        option1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        option1.setText("Test");

        // Act
        boolean actual = option1.equals(null);

        // Assert
        assertFalse(actual);
    }
}
