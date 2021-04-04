package nl.tudelft.oopp.qubo.controllers.structures;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionResultDto;

import java.util.Set;

public class PollResultItem {
    private GridPane pollResultPane;
    private VBox pollVbox;
    private Text pollText;

    private Set<PollOptionResultDto> pollOptionResults;

    private ScrollPane quScPane;
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
        quScPane = scrollPane;

    }



    /**
     * This method sets the background of the poll option result to display the amount of votes it received.
     *
     * @param optionBar     The container for the poll option result.
     * @param relativeVote  A number between 0 and 1 that represents the relative amount of votes for the
     *                      option.
     */
    private void setAnswerBar(Label optionBar, int relativeVote) {
        double arbitraryDifference = 0.00000000001;
        Color colour = new Color(11.8, 59.8, 60.8, 0);

        //Set the stops for the gradient. It should be coloured until the relative vote value, after which it
        //will appear as white.
        Stop[] stops = new Stop[] { new Stop(0, colour),
            new Stop(relativeVote - arbitraryDifference, colour),
            new Stop(relativeVote + arbitraryDifference, Color.WHITE)};

        //Set the start and end coordinates
        Bounds labelBounds = optionBar.getBoundsInParent();
        double startHori = labelBounds.getMinX();
        double verti = labelBounds.getCenterY();
        double endHori = labelBounds.getMaxX();

        //Use the gradient to colour the option bar.
        LinearGradient gradient = new LinearGradient(startHori, verti, endHori, verti, true,
                CycleMethod.NO_CYCLE, stops);
        optionBar.setStyle("-fx-background-color: " + gradient);
    }
}
