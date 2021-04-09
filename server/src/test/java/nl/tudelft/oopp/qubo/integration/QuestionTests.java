package nl.tudelft.oopp.qubo.integration;

import nl.tudelft.oopp.qubo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerCreationDto;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionEditingBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.qubo.entities.Answer;
import nl.tudelft.oopp.qubo.entities.Ban;
import nl.tudelft.oopp.qubo.entities.PaceVote;
import nl.tudelft.oopp.qubo.entities.Question;
import nl.tudelft.oopp.qubo.entities.QuestionBoard;
import nl.tudelft.oopp.qubo.entities.QuestionVote;
import nl.tudelft.oopp.qubo.repositories.AnswerRepository;
import nl.tudelft.oopp.qubo.repositories.BanRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionBoardRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionRepository;
import nl.tudelft.oopp.qubo.repositories.QuestionVoteRepository;
import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import static nl.tudelft.oopp.qubo.integration.utils.JsonUtil.deserialize;
import static nl.tudelft.oopp.qubo.integration.utils.JsonUtil.serialize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockCurrentTimeProvider"})
@AutoConfigureMockMvc
public class QuestionTests {
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

    private final Timestamp pastTime = Timestamp.valueOf("2021-03-01 00:00:00");
    private final Timestamp testTime = Timestamp.valueOf("2021-04-01 00:00:00");

    @BeforeEach
    public void setup() {
        Mockito.when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(testTime.toInstant());
    }

    // addAnswerToQuestion
    @Test
    public void addAnswerToQuestion_withValidModel_addsQuestionCorrectly() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        AnswerCreationBindingModel model = new AnswerCreationBindingModel();
        model.setText("Yes, this is correct.");

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/question/{questionid}/answer", question.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .contentType(MediaType.APPLICATION_JSON)
            .content(serialize(model))
            .queryParam("code", qb.getModeratorCode().toString())
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isOk())
            .andReturn();
        assertNotNull(result);

        AnswerCreationDto dto = deserialize(result.getResponse().getContentAsString(),
            AnswerCreationDto.class);

        assertNotNull(dto.getId());
        Optional<Answer> inDbOptional = answerRepository.findById(dto.getId());
        Answer inDb = inDbOptional.get();
        assertEquals(model.getText(), inDb.getText());
    }

    @Test
    public void addAnswerToQuestion_withInvalidQuestionId_returns404() throws Exception {
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        AnswerCreationBindingModel model = new AnswerCreationBindingModel();
        model.setText("Yes, this is correct.");

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/question/{questionid}/answer", UUID.randomUUID())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .contentType(MediaType.APPLICATION_JSON)
            .queryParam("code", qb.getModeratorCode().toString())
            .content(serialize(model)));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isNotFound())
            .andReturn();

        assertEquals("Question does not exist", result.getResponse().getErrorMessage());
    }

    @Test
    public void addAnswerToQuestion_withInvalidModeratorCode_returns403() throws Exception {
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        AnswerCreationBindingModel model = new AnswerCreationBindingModel();
        model.setText("Yes, this is correct.");

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/question/{questionid}/answer", question.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .contentType(MediaType.APPLICATION_JSON)
            .queryParam("code", UUID.randomUUID().toString())
            .content(serialize(model)));

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isForbidden())
            .andReturn();

        assertEquals("Incorrect moderatorCode for Question", result.getResponse().getErrorMessage());
    }

    // deleteQuestion
    @Test
    public void deleteQuestion_withQuestionId_removesQuestionSucesfully() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        Question question2 = new Question();
        question2.setQuestionBoard(qb);
        question2.setAuthorName("Test");
        question2.setSecretCode(UUID.randomUUID());
        question2.setIp("1.1.1.3");
        question2.setTimestamp(testTime);
        question2.setText("Will this be covered on the midterm?");
        questionRepository.save(question2);


        // Act
        ResultActions resultActions = mockMvc.perform(delete("/api/question/{questionid}", question.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .queryParam("code", question.getSecretCode().toString())
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isOk())
            .andReturn();
        assertNotNull(result);
        Question inDb = questionRepository.getQuestionById(question.getId());
        assertNull(inDb);
    }

    @Test
    public void deleteQuestion_withInvalidQuestionId_throws404() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        // Act
        ResultActions resultActions = mockMvc.perform(delete("/api/question/{questionid}", UUID.randomUUID())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .queryParam("code", question.getSecretCode().toString())
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isNotFound())
            .andReturn();

        assertEquals("Question does not exist", result.getResponse().getErrorMessage());
    }

    @Test
    public void deleteQuestion_withInvalidSecretCode_returns403() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        // Act
        ResultActions resultActions = mockMvc.perform(delete("/api/question/{questionid}", question.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .queryParam("code", UUID.randomUUID().toString())
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isForbidden())
            .andReturn();

        assertEquals("The provided code is neither the "
            + "secret code of this question nor the moderator code of its board."
            , result.getResponse().getErrorMessage());
    }

    // editQuestion
    @Test
    public void editQuestion_withCorrectData_returnsUpdatedQuestionDto() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        QuestionEditingBindingModel model = new QuestionEditingBindingModel();
        model.setText("Will this be covered on the midterm?");

        // Act
        ResultActions resultActions = mockMvc.perform(put("/api/question/{questionid}", question.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .contentType(MediaType.APPLICATION_JSON)
            .content(serialize(model))
            .queryParam("code", question.getSecretCode().toString())
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isOk())
            .andReturn();
        assertNotNull(result);

        QuestionDetailsDto dto = deserialize(result.getResponse().getContentAsString(),
            QuestionDetailsDto.class);
        assertEquals(model.getText(), dto.getText());

        Question inDb = questionRepository.getQuestionById(dto.getId());
        assertEquals(model.getText(), inDb.getText());
    }
    @Test
    public void editQuestion_withInvalidQuestionId_returns404() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        QuestionEditingBindingModel model = new QuestionEditingBindingModel();
        model.setText("Will this be covered on the midterm?");

        // Act
        ResultActions resultActions = mockMvc.perform(put("/api/question/{questionid}", UUID.randomUUID())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .contentType(MediaType.APPLICATION_JSON)
            .content(serialize(model))
            .queryParam("code", question.getSecretCode().toString())
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isNotFound())
            .andReturn();
        assertEquals("Question does not exist", result.getResponse().getErrorMessage());

    }
    @Test
    public void editQuestion_withInvalidSecretCode_returns403() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        QuestionEditingBindingModel model = new QuestionEditingBindingModel();
        model.setText("Will this be covered on the midterm?");

        // Act
        ResultActions resultActions = mockMvc.perform(put("/api/question/{questionid}", question.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .contentType(MediaType.APPLICATION_JSON)
            .content(serialize(model))
            .queryParam("code", UUID.randomUUID().toString())
        );
        // Assert
        MvcResult result = resultActions
            .andExpect(status().isForbidden())
            .andReturn();
        assertEquals("The provided code is neither the "
            + "secret code of this question nor the moderator code of its board."
            , result.getResponse().getErrorMessage());
    }

    // registerQuestionVote
    @Test
    public void registerQuestionVote_withCorrectData_returnCorrectCreationDto() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        Question question2 = new Question();
        question2.setQuestionBoard(qb);
        question2.setAuthorName("Test");
        question2.setSecretCode(UUID.randomUUID());
        question2.setIp("1.1.1.3");
        question2.setTimestamp(testTime);
        question2.setText("Will this be covered on the midterm?");
        questionRepository.save(question2);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/question/{questionid}/vote", question.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isOk())
            .andReturn();
        assertNotNull(result);
        QuestionVoteCreationDto dto = deserialize(result.getResponse().getContentAsString(),
            QuestionVoteCreationDto.class);
        assertNotNull(dto);
        int votesInDb = questionRepository.getQuestionById(question.getId()).getVotes().size();
        int votesInDb2 = questionRepository.getQuestionById(question2.getId()).getVotes().size();
        assertEquals(1, votesInDb);
        assertEquals(0, votesInDb2);
    }

    // deleteQuestionVote
    @Test
    public void deleteQuestionVote() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        QuestionVote vote = new QuestionVote();
        vote.setQuestion(question);
        questionVoteRepository.save(vote);

        // Act
        ResultActions resultActions = mockMvc.perform(delete("/api/question/{questionid}/vote/{voteid}",
            question.getId(), vote.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isOk())
            .andReturn();
        assertNotNull(result);
        QuestionVoteDetailsDto dto = deserialize(result.getResponse().getContentAsString(),
            QuestionVoteDetailsDto.class);
        assertEquals(vote.getId(), dto.getId());
        QuestionVote inDb = questionVoteRepository.getQuestionVoteById(vote.getId());
        assertNull(inDb);
    }

    @Test
    public void deleteQuestionVote_withInvalidQuestionId_returns404() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        QuestionVote vote = new QuestionVote();
        vote.setQuestion(question);
        questionVoteRepository.save(vote);

        // Act
        ResultActions resultActions = mockMvc.perform(delete("/api/question/{questionid}/vote/{voteid}",
            UUID.randomUUID(), vote.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
        );
        // Assert
        MvcResult result = resultActions
            .andExpect(status().isNotFound())
            .andReturn();
        assertEquals("This Question was not voted on"
            + " with this QuestionVote", result.getResponse().getErrorMessage());
    }
    @Test
    public void deleteQuestionVote_withInvalidVoteId_returns404() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        QuestionVote vote = new QuestionVote();
        vote.setQuestion(question);
        questionVoteRepository.save(vote);

        // Act
        ResultActions resultActions = mockMvc.perform(delete("/api/question/{questionid}/vote/{voteid}",
            question.getId(), UUID.randomUUID())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isNotFound())
            .andReturn();
        assertEquals("Unable to find QuestionVote", result.getResponse().getErrorMessage());
    }

    // markQuestionAsAnswered
    @Test
    public void markQuestionAsAnswered() throws Exception {
        // Patch /api/question/{questionid}/answer
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        // Act
        ResultActions resultActions = mockMvc.perform(patch("/api/question/{questionid}/answer",
            question.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .queryParam("code", qb.getModeratorCode().toString())
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isOk())
            .andReturn();

        QuestionDetailsDto dto = deserialize(result.getResponse().getContentAsString(),
            QuestionDetailsDto.class);
        assertNotNull(dto);
        Question inDb = questionRepository.getQuestionById(question.getId());
        assertNotNull(inDb.getAnswered());
    }
    @Test
    public void markQuestionAsAnswered_withInvalidQuestionId_returns404() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        // Act
        ResultActions resultActions = mockMvc.perform(patch("/api/question/{questionid}/answer",
            UUID.randomUUID())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .queryParam("code", qb.getModeratorCode().toString())
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isNotFound())
            .andReturn();

        assertEquals("Question does not exist", result.getResponse().getErrorMessage());
    }
    @Test
    public void markQuestionAsAnswered_withInvalidModCode_returns403() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("Will this be covered on the endterm?");
        questionRepository.save(question);

        // Act
        ResultActions resultActions = mockMvc.perform(patch("/api/question/{questionid}/answer",
            question.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .queryParam("code", UUID.randomUUID().toString())
        );
        // Assert
        MvcResult result = resultActions
            .andExpect(status().isForbidden())
            .andReturn();

        assertEquals("The provided moderatorCode is not valid "
                + "for this Question", result.getResponse().getErrorMessage());
    }

    // banUserByIp
    @Test
    public void banUserByIp_withValidData_bansUser() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("*insert profanity*?");
        questionRepository.save(question);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/question/{questionid}/ban",
            question.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .queryParam("code", qb.getModeratorCode().toString())
        );

        // Assert
        resultActions.andExpect(status().isOk());
        Ban inDb = banRepository.getBanByQuestionBoardAndIp(qb ,question.getIp());
        assertEquals(question.getIp(), inDb.getIp());
        assertEquals(qb.getId(), inDb.getQuestionBoard().getId());
    }
    @Test
    public void banUserByIp_withInvalidQuestionId_returns404() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("*insert profanity*?");
        questionRepository.save(question);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/question/{questionid}/ban",
            UUID.randomUUID())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .queryParam("code", qb.getModeratorCode().toString())
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isNotFound())
            .andReturn();

        assertEquals("Question does not exist", result.getResponse().getErrorMessage());
    }
    @Test
    public void banUserByIp_withInvalidModeCode_returns403() throws Exception {
        // Arrange
        QuestionBoard qb = new QuestionBoard();
        qb.setModeratorCode(UUID.randomUUID());
        qb.setStartTime(pastTime);
        qb.setTitle("Test board");
        qb.setClosed(false);
        questionBoardRepository.save(qb);

        Ban ban = new Ban();
        ban.setIp("1.1.1.1");
        ban.setQuestionBoard(qb);
        banRepository.save(ban);

        Question question = new Question();
        question.setQuestionBoard(qb);
        question.setAuthorName("Test");
        question.setSecretCode(UUID.randomUUID());
        question.setIp("1.1.1.3");
        question.setTimestamp(testTime);
        question.setText("*insert profanity*?");
        questionRepository.save(question);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/question/{questionid}/ban",
            question.getId())
            .with(req -> {
                req.setRemoteAddr("1.1.1.2");
                return req;
            })
            .queryParam("code", UUID.randomUUID().toString())
        );

        // Assert
        MvcResult result = resultActions
            .andExpect(status().isForbidden())
            .andReturn();

        assertEquals("The provided moderatorCode is not valid "
            + "for this Question", result.getResponse().getErrorMessage());
    }

}