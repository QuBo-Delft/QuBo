package nl.tudelft.oopp.qubo.entities;

import java.sql.Timestamp;
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

public class QuestionTests {
    @Test
    public void constructor_withNoArguments_initialisesObject() {
        // Arrange

        // Act
        Question question = new Question();

        // Assert
        assertNotNull(question);
    }

    @Test
    public void constructor_withArguments_initialisesObjectCorrectly() {
        // Arrange
        QuestionBoard expectedBoard = new QuestionBoard();
        String expectedText = "Test";
        UUID expectedSecretCode = UUID.randomUUID();
        Timestamp expectedTimestamp = Timestamp.valueOf("2021-01-01 01:00:00");

        // Act
        Question question = new Question(expectedBoard, expectedText, expectedSecretCode, expectedTimestamp);

        // Assert
        assertNotNull(question);
        assertSame(expectedBoard, question.getQuestionBoard());
        assertSame(expectedText, question.getText());
        assertSame(expectedSecretCode, question.getSecretCode());
        assertSame(expectedTimestamp, question.getTimestamp());
    }

    @Test
    public void constructor_withArgumentsOverload_initialisesObjectCorrectly() {
        // Arrange
        QuestionBoard expectedBoard = new QuestionBoard();
        String expectedText = "Test";
        Timestamp expectedTimestamp = Timestamp.valueOf("2021-01-01 01:00:00");

        // Act
        Question question = new Question(expectedBoard, expectedText, expectedTimestamp);

        // Assert
        assertNotNull(question);
        assertSame(expectedBoard, question.getQuestionBoard());
        assertSame(expectedText, question.getText());
        assertSame(expectedTimestamp, question.getTimestamp());
    }

    @Test
    public void getSetId_worksCorrectly() {
        // Arrange
        UUID expected = UUID.randomUUID();
        Question question = new Question();

        // Act
        question.setId(expected);
        UUID actual = question.getId();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetSecretCode_worksCorrectly() {
        // Arrange
        UUID expected = UUID.randomUUID();
        Question question = new Question();

        // Act
        question.setSecretCode(expected);
        UUID actual = question.getSecretCode();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetTimestamp_worksCorrectly() {
        // Arrange
        Timestamp expected = Timestamp.valueOf("2021-01-01 00:00:00");
        Question question = new Question();

        // Act
        question.setTimestamp(expected);
        Timestamp actual = question.getTimestamp();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetText_worksCorrectly() {
        // Arrange
        String expected = "Test";
        Question question = new Question();

        // Act
        question.setText(expected);
        String actual = question.getText();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetAuthorName_worksCorrectly() {
        // Arrange
        String expected = "Test Author";
        Question question = new Question();

        // Act
        question.setAuthorName(expected);
        String actual = question.getAuthorName();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetQuestionBoard_worksCorrectly() {
        // Arrange
        QuestionBoard expected = new QuestionBoard();
        Question question = new Question();

        // Act
        question.setQuestionBoard(expected);
        QuestionBoard actual = question.getQuestionBoard();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetVotes_worksCorrectly() {
        // Arrange
        Set<QuestionVote> expected = new HashSet<>();
        Question question = new Question();

        // Act
        question.setVotes(expected);
        Set<QuestionVote> actual = question.getVotes();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetAnswers_worksCorrectly() {
        // Arrange
        Set<Answer> expected = new HashSet<>();
        Question question = new Question();

        // Act
        question.setAnswers(expected);
        Set<Answer> actual = question.getAnswers();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetAnswered_worksCorrectly() {
        // Arrange
        Timestamp expected = Timestamp.valueOf("2021-01-01 00:00:00");
        Question question = new Question();

        // Act
        question.setAnswered(expected);
        Timestamp actual = question.getAnswered();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetIp_worksCorrectly() {
        // Arrange
        String expected = "1.1.1.1";
        Question question = new Question();

        // Act
        question.setIp(expected);
        String actual = question.getIp();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void hashCode_withSameProperties_worksCorrectly() {
        // Arrange
        Question question1 = new Question();
        question1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setText("Test");
        question1.setAuthorName("Test Author");
        question1.setSecretCode(UUID.fromString("99355983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        question1.setAnswered(Timestamp.valueOf("2021-02-01 00:00:00"));
        question1.setIp("1.1.1.1");
        Question question2 = new Question();
        question2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question2.setText("Test");
        question2.setAuthorName("Test Author");
        question2.setSecretCode(UUID.fromString("99355983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question2.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        question2.setAnswered(Timestamp.valueOf("2021-02-01 00:00:00"));
        question2.setIp("1.1.1.1");

        // Act
        int hashCode1 = question1.hashCode();
        int hashCode2 = question2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void hashCode_withDifferentProperties_worksCorrectly() {
        // Arrange
        Question question1 = new Question();
        question1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setText("Test");
        question1.setAuthorName("Test Author");
        question1.setSecretCode(UUID.fromString("99355983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        question1.setAnswered(Timestamp.valueOf("2021-02-01 00:00:00"));
        question1.setIp("1.1.1.1");
        Question question2 = new Question();
        question2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question2.setText("Test");
        question2.setAuthorName("Test Author");
        question2.setSecretCode(UUID.fromString("77355983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question2.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        question2.setAnswered(Timestamp.valueOf("2021-02-01 00:00:00"));
        question2.setIp("1.1.1.1");
        // Act
        int hashCode1 = question1.hashCode();
        int hashCode2 = question2.hashCode();

        // Assert
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void equals_withSameProperties_worksCorrectly() {
        // Arrange
        Question question1 = new Question();
        question1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setText("Test");
        question1.setAuthorName("Test Author");
        question1.setSecretCode(UUID.fromString("99355983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        question1.setAnswered(Timestamp.valueOf("2021-02-01 00:00:00"));
        question1.setIp("1.1.1.1");
        Question question2 = new Question();
        question2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question2.setText("Test");
        question2.setAuthorName("Test Author");
        question2.setSecretCode(UUID.fromString("99355983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question2.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        question2.setAnswered(Timestamp.valueOf("2021-02-01 00:00:00"));
        question2.setIp("1.1.1.1");

        // Act
        boolean actual = question1.equals(question2);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withDifferentProperties_worksCorrectly() {
        // Arrange
        Question question1 = new Question();
        question1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setText("Test");
        question1.setAuthorName("Test Author");
        question1.setSecretCode(UUID.fromString("99355983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        question1.setAnswered(Timestamp.valueOf("2021-02-01 00:00:00"));
        question1.setIp("1.1.1.1");
        Question question2 = new Question();
        question2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question2.setText("Test");
        question2.setAuthorName("Test Author");
        question2.setSecretCode(UUID.fromString("99355983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question2.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        question2.setAnswered(Timestamp.valueOf("2021-02-01 00:00:00"));
        question2.setIp("1.1.1.1");

        // Act
        boolean actual = question1.equals(question2);

        // Assert
        assertFalse(actual);
    }

    @Test
    public void equals_withSameObject_worksCorrectly() {
        // Arrange
        Question question1 = new Question();
        question1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setText("Test");
        question1.setAuthorName("Test Author");
        question1.setSecretCode(UUID.fromString("99355983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        question1.setAnswered(Timestamp.valueOf("2021-02-01 00:00:00"));
        question1.setIp("1.1.1.1");

        // Act
        boolean actual = question1.equals(question1);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withNull_worksCorrectly() {
        // Arrange
        Question question1 = new Question();
        question1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setText("Test");
        question1.setAuthorName("Test Author");
        question1.setSecretCode(UUID.fromString("99355983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        question1.setTimestamp(Timestamp.valueOf("2021-01-01 00:00:00"));
        question1.setAnswered(Timestamp.valueOf("2021-02-01 00:00:00"));
        question1.setIp("1.1.1.1");

        // Act
        boolean actual = question1.equals(null);

        // Assert
        assertFalse(actual);
    }
}
