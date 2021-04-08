package nl.tudelft.oopp.qubo.integration;

import java.sql.Timestamp;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import static nl.tudelft.oopp.qubo.integration.utils.JsonUtil.deserialize;
import static nl.tudelft.oopp.qubo.integration.utils.JsonUtil.serialize;
import nl.tudelft.oopp.qubo.repositories.BanRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockCurrentTimeProvider"})
@AutoConfigureMockMvc
public class QuestionBoardTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private BanRepository banRepository;

    @Autowired
    private CurrentTimeProvider mockCurrentTimeProvider;

    @BeforeEach
    public void setup() {
        Timestamp testTime = Timestamp.valueOf("2021-04-01 00:00:00");
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(testTime.toInstant());
    }

    @Test
    public void createBoard_withValidModel_insertsBoardCorrectly() throws Exception {
        // Arrange
        QuestionBoardCreationBindingModel model = new QuestionBoardCreationBindingModel();
        model.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        model.setTitle("Test board");

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/board")
            .contentType(MediaType.APPLICATION_JSON)
            .content(serialize(model)));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isOk())
            .andReturn();

        QuestionBoardCreationDto dto = deserialize(result.getResponse().getContentAsString(),
            QuestionBoardCreationDto.class);

        QuestionBoard inDb = questionBoardRepository.getById(dto.getId());

        assertEquals(dto.getModeratorCode(), inDb.getModeratorCode());
        assertEquals(model.getTitle(), dto.getTitle());
        assertEquals(model.getStartTime(), dto.getStartTime());

        assertEquals(model.getTitle(), inDb.getTitle());
        assertEquals(model.getStartTime(), inDb.getStartTime());
        assertNotNull(inDb.getModeratorCode());
        assertNotEquals(inDb.getId(), inDb.getModeratorCode());
        assertFalse(inDb.isClosed());
    }

    @Test
    public void retrieveQuestionBoardDetails_withValidBoardId_returnsCorrectBoard() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        QuestionBoard qb2 = new QuestionBoard();
        qb2.setModeratorCode(UUID.randomUUID());
        qb2.setStartTime(Timestamp.valueOf("2021-03-02 00:00:00"));
        qb2.setTitle("Test board 2");
        qb2.setClosed(false);
        questionBoardRepository.save(qb2);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/board/{id}", qb.getId())
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isOk())
            .andReturn();

        QuestionBoardDetailsDto dto = deserialize(result.getResponse().getContentAsString(),
            QuestionBoardDetailsDto.class);

        assertEquals(qb.getId(), dto.getId());
        assertEquals(qb.getTitle(), dto.getTitle());
        assertEquals(qb.getStartTime(), dto.getStartTime());
        assertEquals(qb.isClosed(), dto.isClosed());
    }

    @Test
    public void retrieveQuestionBoardDetails_withNonexistentBoardId_returns404() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/board/{id}", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isNotFound())
            .andReturn();

        assertEquals("Unable to find resource", result.getResponse().getErrorMessage());
    }

    @Test
    public void closeQuestionBoard_withValidBoard_closesBoard() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        ResultActions resultActions = mockMvc.perform(patch("/api/board/{id}/close", qb.getId())
            .queryParam("code", qb.getModeratorCode().toString())
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isOk())
            .andReturn();

        QuestionBoardDetailsDto dto = deserialize(result.getResponse().getContentAsString(),
            QuestionBoardDetailsDto.class);

        assertEquals(qb.getId(), dto.getId());
        assertEquals(qb.getTitle(), dto.getTitle());
        assertEquals(qb.getStartTime(), dto.getStartTime());
        assertTrue(dto.isClosed());

        QuestionBoard inDb = questionBoardRepository.getById(dto.getId());
        assertTrue(inDb.isClosed());
    }

    @Test
    public void closeQuestionBoard_withNonexistentBoardId_returns404() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        ResultActions resultActions = mockMvc.perform(patch("/api/board/{id}/close", UUID.randomUUID())
            .queryParam("code", qb.getModeratorCode().toString())
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isNotFound())
            .andReturn();

        assertEquals("Unable to find resource", result.getResponse().getErrorMessage());
    }

    @Test
    public void closeQuestionBoard_withInvalidModeratorCode_returns403() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        // Act
        ResultActions resultActions = mockMvc.perform(patch("/api/board/{id}/close", qb.getId())
            .queryParam("code", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isForbidden())
            .andReturn();

        assertEquals("Invalid moderator code", result.getResponse().getErrorMessage());
    }

    @Test
    public void closeQuestionBoard_withBoardAlreadyClosed_returns409() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        // Act
        ResultActions resultActions = mockMvc.perform(patch("/api/board/{id}/close", qb.getId())
            .queryParam("code", qb.getModeratorCode().toString())
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isConflict())
            .andReturn();

        assertNotNull(result.getResolvedException());
        assertEquals("This QuestionBoard is already closed", result.getResolvedException().getMessage());
    }

    @Test
    public void retrieveQuestionBoardByModeratorCode_withValidCode_returnsCorrectBoard() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        QuestionBoard qb2 = new QuestionBoard();
        qb2.setModeratorCode(UUID.randomUUID());
        qb2.setStartTime(Timestamp.valueOf("2021-03-02 00:00:00"));
        qb2.setTitle("Test board 2");
        qb2.setClosed(false);
        questionBoardRepository.save(qb2);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/board/moderator")
            .queryParam("code", qb.getModeratorCode().toString())
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isOk())
            .andReturn();

        QuestionBoardDetailsDto dto = deserialize(result.getResponse().getContentAsString(),
            QuestionBoardDetailsDto.class);

        assertEquals(qb.getId(), dto.getId());
        assertEquals(qb.getTitle(), dto.getTitle());
        assertEquals(qb.getStartTime(), dto.getStartTime());
        assertEquals(qb.isClosed(), dto.isClosed());
    }

    @Test
    public void retrieveQuestionBoardByModeratorCode_withNonexistentCode_returns404() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/board/moderator")
            .queryParam("code", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isNotFound())
            .andReturn();

        assertEquals("Unable to find resource", result.getResponse().getErrorMessage());
    }

}
