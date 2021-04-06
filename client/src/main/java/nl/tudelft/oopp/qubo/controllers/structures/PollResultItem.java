package nl.tudelft.oopp.qubo.controllers.structures;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionResultDto;

public class PollResultItem extends GridPane {
    private GridPane pollResultPane;
    private VBox pollVbox;
    private Text pollText;

    private PollOptionResultDto[] pollOptionResults;

    private ScrollPane pollScPane;
    private VBox pollContainer;

    /**
     * Constructs a new QuestionItem.
     *
     * @param poll          The poll results including the text of the poll and the option results.
     * @param pollContainer VBox containing the list of polls
     * @param scrollPane    ScrollPane containing the VBox that contains the poll results.
     */
    public PollResultItem(PollResult poll, VBox pollContainer,
                        ScrollPane scrollPane) {
        this.pollText = new Text(poll.getText());
        this.pollOptionResults = poll.getPollOptionResults();

        this.pollContainer = pollContainer;
        pollScPane = scrollPane;

        construct();
    }

    /**
     * Constructs the grid pane used to display the poll and its results.
     */
    private void construct() {
        //Bind the managed property to the visible property so that the node is not accounted for
        //in the layout when it is not visible.
        pollText.managedProperty().bind(pollText.visibleProperty());

        //Set padding for individual cell (needed to prevent horizontal overflow)
        this.setPadding(new Insets(0,0,20,0));

        //Construct a new questionPane to hold the question and add to content pane
        pollResultPane = newPollPane();
        this.addRow(0, pollResultPane);

        addOptions();

        this.setGridLinesVisible(true);
    }

    /**
     * This method constructs a new pollPane to hold the poll text and its options.
     *
     * @return A GridPane containing the poll text and its options.
     */
    private GridPane newPollPane() {
        GridPane gridpane = new GridPane();
        pollVbox = newPollVbox();

        //Set padding for individual cells.
        this.setPadding(new Insets(3, 3, 3, 3));

        //Add the pollVbox to the grid pane.
        gridpane.addColumn(1, pollVbox);

        //Set the column constraints.
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(new ColumnConstraints(5), col2);

        return gridpane;
    }

    /**
     * Constructs and returns a new VBox containing the question body and the author name.
     *
     * @return  Returns a new VBox containing the question body and the
     *          author name to be displayed.
     */
    private VBox newPollVbox() {
        //Set pane to fixed height
        Pane space = new Pane();
        Pane bot = new Pane();
        space.setMinHeight(10);
        space.setMaxHeight(10);
        bot.setMinHeight(10);
        bot.setMaxHeight(10);

        VBox vbox = new VBox(space, pollText, bot);
        pollText.setTextAlignment(TextAlignment.CENTER);

        //Set the title layout properties to fill the grid pane and prevent overflow.
        pollText.wrappingWidthProperty().bind(pollScPane.widthProperty()
                .subtract(pollScPane.getPadding().getLeft() + pollScPane.getPadding().getRight() + 15));

        return vbox;
    }

    /**
     * This method adds the poll options to the poll container.
     */
    private void addOptions() {
        int i = 1;
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
                votePercentageLabel.getWidth() + 53));
            optionBox.setPadding(new Insets(5, 5, 5, 5));
            HBox.setHgrow(optionText, Priority.ALWAYS);

            //Add the horizontal box to the gridpane.
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
        double relativeVotes = (double) option.getVotes() / (double) totalVoteNumber;

        return relativeVotes;
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
            optionBox.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #3690ad, #3690ad "
                + vote + "%, white " + (vote + 1) + "%, white)");
        }
    }
}
