package nl.tudelft.oopp.qubo.controllers.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.qubo.communication.PollCommunication;
import nl.tudelft.oopp.qubo.controllers.StudentViewController;
import nl.tudelft.oopp.qubo.controllers.structures.PollItem;
import nl.tudelft.oopp.qubo.controllers.structures.PollResult;
import nl.tudelft.oopp.qubo.controllers.structures.PollResultItem;
import nl.tudelft.oopp.qubo.dtos.poll.PollDetailsDto;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionResultDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;

import java.util.List;

public class PollRefresh {
    private static QuestionBoardDetailsDto thisQuBo;

    private static VBox pollsVbox;
    private static ScrollPane pollsScrollPane;
    private static List<PollResult> polls;
    private static StudentViewController sController;

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    /**
     * This method takes in the information and nodes to be able to refresh the list of polls for students.
     *
     * @param quBo              QuestionBoardDetailsDto of the board.
     * @param pollVbox          VBox containing the list of polls.
     * @param pollScrollPane    ScrollPane containing the VBox that contains the list of polls.
     * @param controller        The StudentViewController associated with the client.
     */
    public static void studentRefresh(QuestionBoardDetailsDto quBo, VBox pollVbox, ScrollPane pollScrollPane,
                                      StudentViewController controller) {
        thisQuBo = quBo;
        pollsVbox = pollVbox;
        pollsScrollPane = pollScrollPane;
        sController = controller;

        displayPolls();
    }

    /**
     * Method that displays the current poll of the question board on the screen. This will display a poll with
     * voting functionality if the current poll is open, or a poll with its results if the current poll is
     * closed.
     */
    private static void displayPolls() {
        //If the question board is null, return.
        if (thisQuBo == null) {
            return;
        }

        //Reset the current poll box so that no duplicate polls will be added.
        pollsVbox.getChildren().removeAll(pollsVbox.getChildren());

        //Retrieve the current poll and convert it to a PollDetailsDto.
        String jsonPoll = PollCommunication.retrievePollDetails(thisQuBo.getId());

        if (jsonPoll == null) {
            return;

        //Map the poll to the pollsVbox to display it.
        } else {
            PollDetailsDto poll = gson.fromJson(jsonPoll, PollDetailsDto.class);

            //Map the poll results and current poll to cells in the ScrollPane
            mapPolls(poll);
        }
    }

    /**
     * This method maps the current poll to a PollItem or a PollResult to allow poll display.
     *
     * @param current   The poll details of the current poll.
     */
    private static void mapPolls(PollDetailsDto current) {
        //If the poll is open, display the poll with voting functionality.
        if (current.isOpen()) {
            PollItem previousPoll = sController.getPollItem();

            //Create a new poll item and display it.
            PollItem currentPoll = new PollItem(current.getText(), current.getOptions(), pollsVbox,
                pollsScrollPane, sController, previousPoll);
            pollsVbox.getChildren().add(currentPoll);

            sController.setPollItem(currentPoll);

        //If the poll is closed, display the poll with its results.
        } else {
            //Retrieve the poll results.
            String jsonResults = PollCommunication.retrievePollResults(thisQuBo.getId());
            PollOptionResultDto[] optionResults = gson.fromJson(jsonResults, PollOptionResultDto[].class);
            PollResult closedPoll = new PollResult(current.getText(), optionResults);

            //Create a new poll result item and display it.
            PollResultItem pollResultItem = new PollResultItem(closedPoll, pollsVbox, pollsScrollPane);
            pollsVbox.getChildren().add(pollResultItem);
        }
    }
}
