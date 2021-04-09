package nl.tudelft.oopp.qubo.controllers.structures;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.qubo.controllers.ModeratorViewController;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionResultDto;
import nl.tudelft.oopp.qubo.utilities.sorting.Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PollResultItem extends GridPane {
    private GridPane pollResultPane;
    private VBox pollVbox;
    private Text pollText;

    private ModeratorViewController modController;

    private PollOptionResultDto[] pollOptionResults;

    private ScrollPane pollScPane;
    private VBox pollContainer;

    /**
     * Constructs a new PollResultItem.
     *
     * @param poll          The poll results including the text of the poll and the option results.
     * @param pollContainer VBox containing the list of polls
     * @param scrollPane    ScrollPane containing the VBox that contains the poll results.
     */
    public PollResultItem(PollResult poll, VBox pollContainer,
                        ScrollPane scrollPane, ModeratorViewController modController) {
        this.pollText = new Text(poll.getText());
        this.pollOptionResults = poll.getPollOptionResults();

        this.pollContainer = pollContainer;
        pollScPane = scrollPane;

        if (modController != null) {
            this.modController = modController;
        }
        construct();
    }

    /**
     * Constructs the grid pane used to display the poll and its results.
     */
    private void construct() {
        //Bind the managed property to the visible property so that the node is not accounted for
        //in the layout when it is not visible.
        pollText.managedProperty().bind(pollText.visibleProperty());

        //Set padding for the cell that will contain the Poll.
        this.setPadding(new Insets(3,0,20,3));

        //Construct a new pollPane to hold the poll, and add it to the content pane.
        pollResultPane = newPollPane();
        this.addRow(0, pollResultPane);

        //Add the poll results.
        addOptions();

        this.setGridLinesVisible(true);

        if (modController != null) {
            addButtons();
        }
    }

    /**
     * This method constructs a new pollPane to hold the poll text and its options.
     *
     * @return A GridPane containing the poll text and its options.
     */
    private GridPane newPollPane() {
        GridPane gridpane = new GridPane();
        pollVbox = PollItem.newPollVbox(pollText, pollScPane);

        //Set padding for individual cells.
        this.setPadding(new Insets(3, 0, 3, 2));

        //Add the pollVbox to the grid pane.
        gridpane.addColumn(1, pollVbox);

        //Set the column constraints.
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(col2);

        return gridpane;
    }

    /**
     * This method adds the poll options to the poll container.
     */
    private void addOptions() {
        int i = 1;

        //Sort the options on lexicographical order.
        List<PollOptionResultDto> pollOptionResults = new ArrayList<>(Arrays.asList(this.pollOptionResults));
        Sorting.sortPollOptionResultsOnId(pollOptionResults);

        for (PollOptionResultDto option : pollOptionResults) {
            //Add the option text to an HBox.
            Text optionText = new Text(option.getText());
            HBox optionBox = new HBox();

            //Instantiate the percentage label.
            double relativeVote = calculateRelativeVotes(option);
            String labelText = (int)(relativeVote * 100) + "%";
            Label votePercentageLabel = new Label(labelText);

            //Set the coloured bar to display the relative amount of votes for the option.
            setOptionBar(optionBox, relativeVote);

            //Add the option text and percentage label to the horizontal box.
            optionBox.getChildren().addAll(optionText, votePercentageLabel);
            votePercentageLabel.setAlignment(Pos.CENTER_RIGHT);

            //Set the wrapping width,and padding properties for layout purposes.
            optionText.wrappingWidthProperty().bind(pollScPane.widthProperty().subtract(
                votePercentageLabel.getWidth() + 70));
            optionBox.setPadding(new Insets(5, 3, 5, 2));
            HBox.setHgrow(optionText, Priority.SOMETIMES);

            //Add the horizontal box to the grid pane.
            this.addRow((i++ + 1), optionBox);
        }
    }

    /**
     * Returns the relative amount of votes that the poll option received.
     *
     * @param option    The option whose relative votes should be returned.
     * @return The amount of votes of the option divided by the total number of votes for the poll.
     */
    private double calculateRelativeVotes(PollOptionResultDto option) {
        //If the option does not have any votes, return 0.
        if (option.getVotes() == 0) {
            return 0;
        }

        //Add all votes.
        int totalVoteNumber = 0;
        for (PollOptionResultDto optionResult : pollOptionResults) {
            totalVoteNumber += optionResult.getVotes();
        }

        //Return 0 if there were no votes.
        if (totalVoteNumber == 0) {
            return 0;
        }

        //Return the amount of votes of the option divided by the total number of votes
        return (double) option.getVotes() / (double) totalVoteNumber;
    }

    /**
     * This method sets the background of the poll option result to display the amount of votes it received.
     *
     * @param optionBox     The container for the poll option result.
     * @param relativeVote  A number between 0 and 1 that represents the relative amount of votes for the
     *                      option.
     */
    private void setOptionBar(HBox optionBox, double relativeVote) {
        //Scale the relativeVote to a percentage and round it down to the nearest integer.
        int vote = (int) (relativeVote * 100);

        if (vote == 0) {
            optionBox.setStyle("-fx-background-color: white");
        } else {
            //Add the linear gradient to colour the VBox
            optionBox.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #e97c28, #e97c28 "
                + vote + "%, white " + (vote + 1) + "%, white)");
        }
    }

    /**
     * Adds the moderator buttons to a poll item.
     */
    public void addButtons() {
        HBox buttonBox = new HBox();
        Button delete = new Button("Delete");
        delete.setOnAction(event -> modController.deletePoll());
        Button redo = new Button("Redo");
        redo.setOnAction(event -> modController.redoPoll());
        buttonBox.getChildren().addAll(delete, redo);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        this.addRow(2 + pollOptionResults.length, buttonBox);
    }
}
