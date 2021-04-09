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

public class QuestionBoardTests {
    @Test
    public void constructor_withNoArguments_initialisesObject() {
        // Arrange

        // Act
        QuestionBoard board = new QuestionBoard();

        // Assert
        assertNotNull(board);
    }

    @Test
    public void constructor_withArguments_initialisesObjectCorrectly() {
        // Arrange
        UUID expectedModeratorCode = UUID.randomUUID();
        String expectedTitle = "Test";
        Timestamp expectedStartTime = Timestamp.valueOf("2021-01-01 00:00:00");

        // Act
        QuestionBoard board = new QuestionBoard(expectedModeratorCode, expectedTitle, expectedStartTime);

        // Assert
        assertNotNull(board);
        assertSame(expectedModeratorCode, board.getModeratorCode());
        assertSame(expectedTitle, board.getTitle());
        assertSame(expectedStartTime, board.getStartTime());
    }

    @Test
    public void constructor_withArgumentsOverload_initialisesObjectCorrectly() {
        // Arrange
        String expectedTitle = "Test";
        Timestamp expectedStartTime = Timestamp.valueOf("2021-01-01 00:00:00");

        // Act
        QuestionBoard board = new QuestionBoard(expectedTitle, expectedStartTime);

        // Assert
        assertNotNull(board);
        assertSame(expectedTitle, board.getTitle());
        assertSame(expectedStartTime, board.getStartTime());
    }

    @Test
    public void getSetId_worksCorrectly() {
        // Arrange
        UUID expected = UUID.randomUUID();
        QuestionBoard board = new QuestionBoard();

        // Act
        board.setId(expected);
        UUID actual = board.getId();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetModeratorCode_worksCorrectly() {
        // Arrange
        UUID expected = UUID.randomUUID();
        QuestionBoard board = new QuestionBoard();

        // Act
        board.setModeratorCode(expected);
        UUID actual = board.getModeratorCode();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetTitle_worksCorrectly() {
        // Arrange
        String expected = "Test";
        QuestionBoard board = new QuestionBoard();

        // Act
        board.setTitle(expected);
        String actual = board.getTitle();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetStartTime_worksCorrectly() {
        // Arrange
        Timestamp expected = Timestamp.valueOf("2021-01-01 00:00:00");
        QuestionBoard board = new QuestionBoard();

        // Act
        board.setStartTime(expected);
        Timestamp actual = board.getStartTime();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetClosed_worksCorrectly() {
        // Arrange
        boolean expected = true;
        QuestionBoard board = new QuestionBoard();

        // Act
        board.setClosed(expected);
        boolean actual = board.isClosed();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetQuestions_worksCorrectly() {
        // Arrange
        Set<Question> expected = new HashSet<>();
        QuestionBoard board = new QuestionBoard();

        // Act
        board.setQuestions(expected);
        Set<Question> actual = board.getQuestions();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetPaceVotes_worksCorrectly() {
        // Arrange
        Set<PaceVote> expected = new HashSet<>();
        QuestionBoard board = new QuestionBoard();

        // Act
        board.setPaceVotes(expected);
        Set<PaceVote> actual = board.getPaceVotes();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetPoll_worksCorrectly() {
        // Arrange
        Poll expected = new Poll();
        QuestionBoard board = new QuestionBoard();

        // Act
        board.setPoll(expected);
        Poll actual = board.getPoll();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetBans_worksCorrectly() {
        // Arrange
        Set<Ban> expected = new HashSet<>();
        QuestionBoard board = new QuestionBoard();

        // Act
        board.setBans(expected);
        Set<Ban> actual = board.getBans();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void hashCode_withSameProperties_worksCorrectly() {
        // Arrange
        QuestionBoard board1 = new QuestionBoard();
        board1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        board1.setModeratorCode(UUID.fromString("9975e983-0c8f-4ba9-a9aa-c7804f39a7d9"));
        board1.setClosed(true);
        board1.setTitle("Test");
        board1.setStartTime(Timestamp.valueOf("2021-01-01 00:00:00"));
        QuestionBoard board2 = new QuestionBoard();
        board2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        board2.setModeratorCode(UUID.fromString("9975e983-0c8f-4ba9-a9aa-c7804f39a7d9"));
        board2.setClosed(true);
        board2.setTitle("Test");
        board2.setStartTime(Timestamp.valueOf("2021-01-01 00:00:00"));

        // Act
        int hashCode1 = board1.hashCode();
        int hashCode2 = board2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void hashCode_withDifferentProperties_worksCorrectly() {
        // Arrange
        QuestionBoard board1 = new QuestionBoard();
        board1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        board1.setModeratorCode(UUID.fromString("9975e983-0c8f-4ba9-a9aa-c7804f39a7d9"));
        board1.setClosed(true);
        board1.setTitle("Test");
        board1.setStartTime(Timestamp.valueOf("2021-01-01 00:00:00"));
        QuestionBoard board2 = new QuestionBoard();
        board2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        board2.setModeratorCode(UUID.fromString("9975e983-0c8f-4ba9-a9aa-c7804f39a7d9"));
        board2.setClosed(true);
        board2.setTitle("Test");
        board2.setStartTime(Timestamp.valueOf("2021-01-01 00:00:00"));

        // Act
        int hashCode1 = board1.hashCode();
        int hashCode2 = board2.hashCode();

        // Assert
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void equals_withSameProperties_worksCorrectly() {
        // Arrange
        QuestionBoard board1 = new QuestionBoard();
        board1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        board1.setModeratorCode(UUID.fromString("9975e983-0c8f-4ba9-a9aa-c7804f39a7d9"));
        board1.setClosed(true);
        board1.setTitle("Test");
        board1.setStartTime(Timestamp.valueOf("2021-01-01 00:00:00"));
        QuestionBoard board2 = new QuestionBoard();
        board2.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        board2.setModeratorCode(UUID.fromString("9975e983-0c8f-4ba9-a9aa-c7804f39a7d9"));
        board2.setClosed(true);
        board2.setTitle("Test");
        board2.setStartTime(Timestamp.valueOf("2021-01-01 00:00:00"));

        // Act
        boolean actual = board1.equals(board2);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withDifferentProperties_worksCorrectly() {
        // Arrange
        QuestionBoard board1 = new QuestionBoard();
        board1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        board1.setModeratorCode(UUID.fromString("9975e983-0c8f-4ba9-a9aa-c7804f39a7d9"));
        board1.setClosed(true);
        board1.setTitle("Test");
        board1.setStartTime(Timestamp.valueOf("2021-01-01 00:00:00"));
        QuestionBoard board2 = new QuestionBoard();
        board2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        board2.setModeratorCode(UUID.fromString("9975e983-0c8f-4ba9-a9aa-c7804f39a7d9"));
        board2.setClosed(true);
        board2.setTitle("Test");
        board2.setStartTime(Timestamp.valueOf("2021-01-01 00:00:00"));

        // Act
        boolean actual = board1.equals(board2);

        // Assert
        assertFalse(actual);
    }

    @Test
    public void equals_withSameObject_worksCorrectly() {
        // Arrange
        QuestionBoard board1 = new QuestionBoard();
        board1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        board1.setModeratorCode(UUID.fromString("9975e983-0c8f-4ba9-a9aa-c7804f39a7d9"));
        board1.setClosed(true);
        board1.setTitle("Test");
        board1.setStartTime(Timestamp.valueOf("2021-01-01 00:00:00"));

        // Act
        boolean actual = board1.equals(board1);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withNull_worksCorrectly() {
        // Arrange
        QuestionBoard board1 = new QuestionBoard();
        board1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        board1.setModeratorCode(UUID.fromString("9975e983-0c8f-4ba9-a9aa-c7804f39a7d9"));
        board1.setClosed(true);
        board1.setTitle("Test");
        board1.setStartTime(Timestamp.valueOf("2021-01-01 00:00:00"));

        // Act
        boolean actual = board1.equals(null);

        // Assert
        assertFalse(actual);
    }
}
