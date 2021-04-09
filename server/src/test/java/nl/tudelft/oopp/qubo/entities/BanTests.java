package nl.tudelft.oopp.qubo.entities;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BanTests {
    @Test
    public void constructor_withNoArguments_initialisesObject() {
        // Arrange

        // Act
        Ban ban = new Ban();

        // Assert
        assertNotNull(ban);
    }

    @Test
    public void constructor_withArguments_initialisesObjectCorrectly() {
        // Arrange
        QuestionBoard board = new QuestionBoard();
        String ip = "1.1.1.1";

        // Act
        Ban ban = new Ban(board, ip);

        // Assert
        assertNotNull(ban);
        assertSame(board, ban.getQuestionBoard());
        assertEquals(ip, ban.getIp());
    }

    @Test
    public void getSetId_worksCorrectly() {
        // Arrange
        UUID expected = UUID.randomUUID();
        Ban ban = new Ban();

        // Act
        ban.setId(expected);
        UUID actual = ban.getId();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void getSetIp_worksCorrectly() {
        // Arrange
        String expected = "1.1.1.1";
        Ban ban = new Ban();

        // Act
        ban.setIp(expected);
        String actual = ban.getIp();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void getSetQuestionBoard_worksCorrectly() {
        // Arrange
        QuestionBoard expected = new QuestionBoard();
        Ban ban = new Ban();

        // Act
        ban.setQuestionBoard(expected);
        QuestionBoard actual = ban.getQuestionBoard();

        // Assert
        assertSame(expected, actual);
    }

    @Test
    public void hashCode_withSameProperties_worksCorrectly() {
        // Arrange
        Ban ban1 = new Ban();
        ban1.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        ban1.setIp("1.1.1.1");
        Ban ban2 = new Ban();
        ban2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        ban2.setIp("1.1.1.1");

        // Act
        int hashCode1 = ban1.hashCode();
        int hashCode2 = ban2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void hashCode_withDifferentProperties_worksCorrectly() {
        // Arrange
        Ban ban1 = new Ban();
        ban1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        ban1.setIp("1.1.1.1");
        Ban ban2 = new Ban();
        ban2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        ban2.setIp("1.1.1.1");

        // Act
        int hashCode1 = ban1.hashCode();
        int hashCode2 = ban2.hashCode();

        // Assert
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void equals_withSameProperties_worksCorrectly() {
        // Arrange
        Ban ban1 = new Ban();
        ban1.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        ban1.setIp("1.1.1.1");
        Ban ban2 = new Ban();
        ban2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        ban2.setIp("1.1.1.1");

        // Act
        boolean actual = ban1.equals(ban2);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withDifferentProperties_worksCorrectly() {
        // Arrange
        Ban ban1 = new Ban();
        ban1.setId(UUID.fromString("9938e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        ban1.setIp("1.1.1.1");
        Ban ban2 = new Ban();
        ban2.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        ban2.setIp("1.1.1.1");

        // Act
        boolean actual = ban1.equals(ban2);

        // Assert
        assertFalse(actual);
    }

    @Test
    public void equals_withSameObject_worksCorrectly() {
        // Arrange
        Ban ban1 = new Ban();
        ban1.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        ban1.setIp("1.1.1.1");

        // Act
        boolean actual = ban1.equals(ban1);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void equals_withNull_worksCorrectly() {
        // Arrange
        Ban ban1 = new Ban();
        ban1.setId(UUID.fromString("7738e983-0c8f-4ba9-a9ee-c7804f39a7d9"));
        ban1.setIp("1.1.1.1");

        // Act
        boolean actual = ban1.equals(null);

        // Assert
        assertFalse(actual);
    }
}
