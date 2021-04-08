package nl.tudelft.oopp.qubo.integration;

import java.sql.Timestamp;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.entities.Answer;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.entities.QuestionVote;
import static nl.tudelft.oopp.qubo.integration.utils.JsonUtil.deserialize;
import static nl.tudelft.oopp.qubo.integration.utils.JsonUtil.serialize;
import nl.tudelft.oopp.qubo.repositories.AnswerRepository;
import nl.tudelft.oopp.qubo.repositories.BanRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionVoteRepository;
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
    private QuestionVoteRepository questionVoteRepository;

    @Autowired
    private AnswerRepository answerRepository;

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

    @Test
    public void retrieveQuestionListByBoardId_withValidId_returnsQuestions() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        Question question1 = new Question();
        question1.setSecretCode(UUID.fromString("e6f316f7-9000-4a29-bcbd-8b517ced267c"));
        question1.setText("Question Text 1");
        question1.setTimestamp(Timestamp.valueOf("2021-03-01 00:02:00"));
        question1.setAnswered(null);
        question1.setAuthorName("Question Author");
        question1.setQuestionBoard(qb);
        questionRepository.save(question1);

        Answer answer1 = new Answer();
        answer1.setQuestion(question1);
        answer1.setText("Test answer");
        answer1.setTimestamp(Timestamp.valueOf("2021-03-01 00:02:05"));
        answerRepository.save(answer1);

        Answer answer2 = new Answer();
        answer2.setQuestion(question1);
        answer2.setText("Test answer 2");
        answer2.setTimestamp(Timestamp.valueOf("2021-03-01 00:02:15"));
        answerRepository.save(answer2);

        Question question2 = new Question();
        question2.setSecretCode(UUID.fromString("e6f316f7-9000-4a29-bcbd-8b517ced267d"));
        question2.setText("Question Text 2");
        question2.setTimestamp(Timestamp.valueOf("2021-03-02 00:02:00"));
        question2.setAnswered(Timestamp.valueOf("2021-03-02 00:05:00"));
        question2.setAuthorName("Question Author 2");
        question2.setQuestionBoard(qb);
        questionRepository.save(question2);

        QuestionVote vote = new QuestionVote();
        vote.setQuestion(question2);
        questionVoteRepository.save(vote);

        QuestionVote vote2 = new QuestionVote();
        vote2.setQuestion(question2);
        questionVoteRepository.save(vote2);

        QuestionBoard qb2 = new QuestionBoard();
        qb2.setModeratorCode(UUID.randomUUID());
        qb2.setStartTime(Timestamp.valueOf("2021-03-02 00:00:00"));
        qb2.setTitle("Test board 2");
        qb2.setClosed(false);
        questionBoardRepository.save(qb2);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/board/{id}/questions", qb.getId())
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isOk())
            .andReturn();

        QuestionDetailsDto[] dtos = deserialize(result.getResponse().getContentAsString(),
            QuestionDetailsDto[].class);

        assertEquals(2, dtos.length);

        QuestionDetailsDto actualQ1, actualQ2;
        if (dtos[0].getId().equals(question1.getId())) {
            actualQ1 = dtos[0];
            actualQ2 = dtos[1];
        } else {
            actualQ1 = dtos[1];
            actualQ2 = dtos[0];
        }

        assertEquals(question1.getId(), actualQ1.getId());
        assertEquals(question1.getText(), actualQ1.getText());
        assertEquals(question1.getAuthorName(), actualQ1.getAuthorName());
        assertEquals(question1.getTimestamp(), actualQ1.getTimestamp());
        assertEquals(question1.getAnswered(), actualQ1.getAnswered());
        assertEquals(0, actualQ1.getUpvotes());

        AnswerDetailsDto[] actualAnswers = actualQ1.getAnswers().toArray(new AnswerDetailsDto[0]);

        assertEquals(2, actualAnswers.length);
        AnswerDetailsDto actualA1, actualA2;
        if (actualAnswers[0].getId().equals(answer1.getId())) {
            actualA1 = actualAnswers[0];
            actualA2 = actualAnswers[1];
        } else {
            actualA1 = actualAnswers[1];
            actualA2 = actualAnswers[0];
        }

        assertEquals(answer1.getId(), actualA1.getId());
        assertEquals(answer1.getTimestamp(), actualA1.getTimestamp());
        assertEquals(answer1.getText(), actualA1.getText());
        assertEquals(answer2.getId(), actualA2.getId());
        assertEquals(answer2.getTimestamp(), actualA2.getTimestamp());
        assertEquals(answer2.getText(), actualA2.getText());

        assertEquals(question2.getId(), actualQ2.getId());
        assertEquals(question2.getText(), actualQ2.getText());
        assertEquals(question2.getAuthorName(), actualQ2.getAuthorName());
        assertEquals(question2.getTimestamp(), actualQ2.getTimestamp());
        assertEquals(question2.getAnswered(), actualQ2.getAnswered());
        assertEquals(2, actualQ2.getUpvotes());
        assertEquals(0, actualQ2.getAnswers().size());
    }

    @Test
    public void retrieveQuestionListByBoardId_withNonexistentId_returns404() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(Timestamp.valueOf("2021-03-01 00:00:00"));
        qb.setTitle("Test board");
        qb.setClosed(true);
        questionBoardRepository.save(qb);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/board/{id}/questions",
            UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isNotFound())
            .andReturn();

        assertEquals("Unable to find resource", result.getResponse().getErrorMessage());
    }
}
