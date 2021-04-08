package nl.tudelft.oopp.qubo.integration;

import java.sql.Timestamp;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
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
}
