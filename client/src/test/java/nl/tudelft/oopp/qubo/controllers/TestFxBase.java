package nl.tudelft.oopp.qubo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.qubo.communication.QuestionBoardCommunication;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This is the base class used for all JavaFX testing by using the TestFX library. To run JavaFX tests,
 * first run the server-side QuBo.class. After having started the server-side application,
 * run any of the controller tests. By default they are in headless mode, to set this to head full mode,
 * navigate to the global gradle.build and set headless in the jvmArgs to false.
 */
@ExtendWith(ApplicationExtension.class)
public abstract class TestFxBase {

    static {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("java.awt.headless", "true");
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
        }
    }

    private static UUID modCodeOpen;
    private static UUID modCodeClosed;

    public static UUID getModCodeOpen() {
        return modCodeOpen;
    }

    public static UUID getModCodeClosed() {
        return modCodeClosed;
    }

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();

    /**
     * This method takes the time at which to schedule a question board, schedules it
     * and returns its creation details.
     *
     * @param startTime Time for which to schedule the question board.
     * @return          question board creation details.
     */
    private static QuestionBoardDetailsDto quBoBuilder(Timestamp startTime) {
        QuestionBoardCreationBindingModel board = new QuestionBoardCreationBindingModel("QuBo", startTime);

        // Send the request and retrieve the string body of QuestionBoardCreationDto
        String qcBody = QuestionBoardCommunication.createBoardRequest(board);

        // Convert the response body to QuestionBoardCreationDto
        QuestionBoardCreationDto qc = gson.fromJson(qcBody, QuestionBoardCreationDto.class);
        if (startTime.after(Timestamp.valueOf(LocalDateTime.now()))) {
            modCodeClosed = qc.getModeratorCode();
        } else {
            modCodeOpen = qc.getModeratorCode();
        }
        String qdBody = QuestionBoardCommunication.retrieveBoardDetails(qc.getId());

        return gson.fromJson(qdBody, QuestionBoardDetailsDto.class);
    }

    /**
     * Creates a QuestionBoardCreationDto for an open Question Board.
     * @return The QuestionBoardCreationDto of the created Question Board.
     */
    public static QuestionBoardCreationDto createQuBoCreation() {
        // Models an open Question Board
        QuestionBoardCreationBindingModel board =
            new QuestionBoardCreationBindingModel("QuBo", Timestamp.valueOf(LocalDateTime.now()));

        // Send the request and retrieve the string body of QuestionBoardCreationDto
        String qcBody = QuestionBoardCommunication.createBoardRequest(board);

        // Convert the response body to QuestionBoardCreationDto
        return gson.fromJson(qcBody, QuestionBoardCreationDto.class);
    }

    /**
     * Creates an open question board and sets it's details.
     * Primarily used for easy generation, as this method is not strictly necessary.
     */
    public static QuestionBoardDetailsDto createOpenQuBo() {
        Timestamp startTimeNow = Timestamp.valueOf(LocalDateTime.now());
        return quBoBuilder(startTimeNow);
    }

    /**
     * Create a closed question board and sets it's details.
     * Primarily used for easy generation, as this method is not strictly necessary.
     */
    public static QuestionBoardDetailsDto createClosedQuBo() {
        Timestamp startTimeTomorrow = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        return quBoBuilder(startTimeTomorrow);
    }

    /**
     * This method checks whether the clipboard actually contains the correct code after copying it.
     * When this method fails, an IOException or UnsupportedFlavorException shall be thrown.
     * Either of these exceptions will make the test calling this method fail automatically.
     */
    public void clipboardTest() {
        if (!Boolean.getBoolean("headless")) {
            try {
                String clipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor);
                UUID clipboardUuid = UUID.fromString(clipboard);
            } catch (IOException | UnsupportedFlavorException e) {
                e.printStackTrace();
            }
        }
    }
}
