package nl.tudelft.oopp.qubo.controllers.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.qubo.communication.PollCommunication;
import nl.tudelft.oopp.qubo.controllers.structures.PollItem;
import nl.tudelft.oopp.qubo.controllers.structures.PollResult;
import nl.tudelft.oopp.qubo.dtos.poll.PollDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;

import java.util.List;

public class PollRefresh {
    private static QuestionBoardDetailsDto thisQuBoId;

    private static VBox pollsVbox;
    private static ScrollPane pollsScrollPane;
    private static List<PollResult> polls;

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    /**
     * This method takes in the information and nodes to be able to refresh the list of polls for students.
     *
     * @param quBo              QuestionBoardDetailsDto of the board.
     * @param pollVbox          VBox containing the list of polls.
     * @param pollScrollPane    ScrollPane containing the VBox that contains the list of polls.
     * @param pollList          A list containing all polls that have already been closed and should thus
     *                          be displayed as well.
     */
    public static void studentRefresh(QuestionBoardDetailsDto quBo, VBox pollVbox, ScrollPane pollScrollPane,
                                      List<PollResult> pollList) {
        thisQuBoId = quBo;
        pollsVbox = pollVbox;
        pollsScrollPane = pollScrollPane;
        polls = pollList;

        displayPolls();
    }

    /**
     * Method that displays the questions that are in the question board on the screen. Answered questions
     * will be sorted by the time at which they were answered, and unanswered questions will be sorted by
     * the number of upvotes they have received.
     */
    private static void displayPolls() {
        //If the question board is null, return.
        if (thisQuBoId == null) {
            return;
        }

        //Retrieve the current poll and convert it to a PollDetailsDto.
        String jsonPoll = PollCommunication.retrievePollDetails(thisQuBoId.getId());

        if (jsonPoll == null) {
            //If there is no current poll, map the remaining poll results
            mapPolls(null);
        } else {
            PollDetailsDto poll = gson.fromJson(jsonPoll, PollDetailsDto.class);

            //Map the poll results and current poll to cells in the ScrollPane
            mapPolls(poll);
        }
    }

    private static void mapPolls(PollDetailsDto current) {
        //If there is a current poll, put this at the top of the VBox
        if (current != null) {
            PollItem currentPoll = new PollItem(current.getText(), current.getOptions(), pollsVbox,
                    pollsScrollPane);
            pollsVbox.getChildren().add(currentPoll);
        }

        //TODO: Add the PollResultItems to the VBOX
    }
}
