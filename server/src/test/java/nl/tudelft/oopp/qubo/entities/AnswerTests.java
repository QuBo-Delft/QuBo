package nl.tudelft.oopp.qubo.entities;

import java.sql.Timestamp;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class AnswerTests {
    @Test
    public void constructor_withNoArguments_initialisesObject() {
        // Arrange

        // Act
        Answer answer = new Answer();

        // Assert
        assertNotNull(answer);
    }

    @Test
    public void constructor_withArguments_initialisesObjectCorrectly() {
        // Arrange
        Question question = new Question();
        String text = "Test";
        Timestamp timestamp = Timestamp.valueOf("2021-01-01 00:00:00");

        // Act
        Answer answer = new Answer(question, text, timestamp);

        // Assert
        assertNotNull(answer);
        assertEquals(question, answer.getQuestion());
        assertEquals(text, answer.getText());
        assertEquals(timestamp, answer.getTimestamp());
    }

    @Test
    public void getSetId_worksCorrectly() {
        // Arrange
        UUID expected = UUID.randomUUID();
        Answer answer = new Answer();

        // Act
        answer.setId(expected);
        UUID actual = answer.getId();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetQuestion_worksCorrectly() {
        // Arrange
        Question expected = new Question();
        Answer answer = new Answer();

        // Act
        answer.setQuestion(expected);
        Question actual = answer.getQuestion();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetTimestamp_worksCorrectly() {
        // Arrange
        Timestamp expected = Timestamp.valueOf("2021-01-01 00:00:00");
        Answer answer = new Answer();

        // Act
        answer.setTimestamp(expected);
        Timestamp actual = answer.getTimestamp();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetText_worksCorrectly() {
        // Arrange
        String expected = "Test";
        Answer answer = new Answer();

        // Act
        answer.setText(expected);
        String actual = answer.getText();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void hashCode_withSameProperties_worksCorrectly() {
        // Arrange
        Answer answer1 = new Answer();
        answer1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        answer1.setText("Test");
        answer1.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        Answer answer2 = new Answer();
        answer2.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        answer2.setText("Test");
        answer2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        int hashCode1 = answer1.hashCode();
        int hashCode2 = answer2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void hashCode_withDifferentProperties_worksCorrectly() {
        // Arrange
        Answer answer1 = new Answer();
        answer1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        answer1.setText("Test");
        answer1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        Answer answer2 = new Answer();
        answer2.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        answer2.setText("Test");
        answer2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        int hashCode1 = answer1.hashCode();
        int hashCode2 = answer2.hashCode();

        // Assert
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void equals_withSameProperties_worksCorrectly() {
        // Arrange
        Answer answer1 = new Answer();
        answer1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        answer1.setText("Test");
        answer1.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        Answer answer2 = new Answer();
        answer2.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        answer2.setText("Test");
        answer2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = answer1.equals(answer2);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withDifferentProperties_worksCorrectly() {
        // Arrange
        Answer answer1 = new Answer();
        answer1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        answer1.setText("Test");
        answer1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        Answer answer2 = new Answer();
        answer2.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        answer2.setText("Test");
        answer2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = answer1.equals(answer2);

        // Assert
        assertFalse(actual);
    }

    @Test
    public void equals_withSameObject_worksCorrectly() {
        // Arrange
        Answer answer1 = new Answer();
        answer1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        answer1.setText("Test");
        answer1.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = answer1.equals(answer1);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withNull_worksCorrectly() {
        // Arrange
        Answer answer1 = new Answer();
        answer1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        answer1.setText("Test");
        answer1.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));

        // Act
        boolean actual = answer1.equals(null);

        // Assert
        assertFalse(actual);
    }
}
