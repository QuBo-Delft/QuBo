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
     * Method that displays the questions that are in the question board on the screen. Answered questions
     * will be sorted by the time at which they were answered, and unanswered questions will be sorted by
     * the number of upvotes they have received.
     */
    private static void displayPolls() {
        //If the question board is null, return.
        if (thisQuBo == null) {
            return;
        }

        //Retrieve the current poll and convert it to a PollDetailsDto.
        String jsonPoll = PollCommunication.retrievePollDetails(thisQuBo.getId());

        //Remove all previous displayed polls if there is no current poll.
        if (jsonPoll == null) {
            pollsVbox.getChildren().removeAll(pollsVbox.getChildren());

        //Map the poll to the pollsVbox to display it.
        } else {
            PollDetailsDto poll = gson.fromJson(jsonPoll, PollDetailsDto.class);

            //Map the poll results and current poll to cells in the ScrollPane
            mapPolls(poll);
        }
    }

    private static void mapPolls(PollDetailsDto current) {
        //If the poll is open, display the poll with voting functionality.
        if (current.isOpen()) {
            PollItem currentPoll = new PollItem(current.getText(), current.getOptions(), pollsVbox,
                pollsScrollPane, sController);
            pollsVbox.getChildren().add(currentPoll);

        //If the poll is closed, display the poll with its results.
        } else {
            String jsonResults = PollCommunication.retrievePollResults(thisQuBo.getId());

            PollOptionResultDto[] optionResults = gson.fromJson(jsonResults, PollOptionResultDto[].class);

            PollResult closedPoll = new PollResult(current.getText(), optionResults);
            PollResultItem pollResultItem = new PollResultItem(closedPoll, pollsVbox, pollsScrollPane);
            pollsVbox.getChildren().add(pollResultItem);
        }
    }
}
