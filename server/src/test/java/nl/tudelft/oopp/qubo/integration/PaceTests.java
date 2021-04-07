package nl.tudelft.oopp.qubo.integration;

import java.sql.Timestamp;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationDto;
import nl.tudelft.oopp.qubo.entities.Ban;
import nl.tudelft.oopp.qubo.entities.PaceVote;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import static nl.tudelft.oopp.qubo.integration.utils.JsonUtil.deserialize;
import static nl.tudelft.oopp.qubo.integration.utils.JsonUtil.serialize;
import nl.tudelft.oopp.qubo.repositories.BanRepository;
import nl.tudelft.oopp.qubo.repositories.PaceVoteRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockCurrentTimeProvider"})
@AutoConfigureMockMvc
public class PaceTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private BanRepository banRepository;

    @Autowired
    private PaceVoteRepository paceVoteRepository;

    @Autowired
    private CurrentTimeProvider mockCurrentTimeProvider;

    @BeforeEach
    public void setup() {
        Timestamp testTime = Timestamp.valueOf("2021-04-01 00:00:00");
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(testTime.toInstant());
    }

    @Test
    public void registerPaceVote_withValidBoard_insertsVote() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
        model.setPaceType(PaceType.TOO_FAST);

        // Act, Assert
        MvcResult result = mockMvc.perform(post("/api/board/{id}/pace", qb.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .contentType(MediaType.APPLICATION_JSON)
            .content(serialize(model)))
            .andExpect(status().isOk())
            .andReturn();

        PaceVoteCreationDto dto = deserialize(result.getResponse().getContentAsString(),
            PaceVoteCreationDto.class);

        assertNotNull(dto.getId());
        PaceVote voteInDatabase = paceVoteRepository.getById(dto.getId());
        assertNotNull(result);
        assertEquals(model.getPaceType().toString(), voteInDatabase.getPaceType().toString());
        assertEquals(qb.getId(), voteInDatabase.getQuestionBoard().getId());
    }

    @Test
    public void registerPaceVote_withInvalidBoardId_returns404() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
        model.setPaceType(PaceType.TOO_FAST);

        // Act, Assert
        MvcResult result = mockMvc.perform(post("/api/board/{id}/pace", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content(serialize(model)))
            .andExpect(status().isNotFound())
            .andReturn();

        assertEquals("Unable to find resource", result.getResponse().getErrorMessage());
    }

    @Test
    public void registerPaceVote_withClosedBoard_returns403() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
        model.setPaceType(PaceType.TOO_FAST);

        // Act, Assert
        MvcResult result = mockMvc.perform(post("/api/board/{id}/pace", qb.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(serialize(model)))
            .andExpect(status().isForbidden())
            .andReturn();

        assertNotNull(result.getResolvedException());
        assertEquals("Question board is not active", result.getResolvedException().getMessage());
    }

    @Test
    public void registerPaceVote_withNotStartedBoard_returns403() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-04-01 00:00:01"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
        model.setPaceType(PaceType.TOO_FAST);

        // Act, Assert
        MvcResult result = mockMvc.perform(post("/api/board/{id}/pace", qb.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(serialize(model)))
            .andExpect(status().isForbidden())
            .andReturn();

        assertNotNull(result.getResolvedException());
        assertEquals("Question board is not active", result.getResolvedException().getMessage());
    }

    @Test
    public void registerPaceVote_withBannedIp_returns403() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        PaceVoteCreationBindingModel model = new PaceVoteCreationBindingModel();
        model.setPaceType(PaceType.TOO_FAST);

        // Act, Assert
        MvcResult result = mockMvc.perform(post("/api/board/{id}/pace", qb.getId())
            .with(req -> {
                req.setRemoteAddr(ban.getIp());
                return req;
            })
            .contentType(MediaType.APPLICATION_JSON)
            .content(serialize(model)))
            .andExpect(status().isForbidden())
            .andReturn();

        assertNotNull(result.getResolvedException());
        assertEquals("You are banned from this question board.",
            result.getResolvedException().getMessage());
    }
}
