package nl.tudelft.oopp.qubo.controllers.structures;

import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.scene.text.TextAlignment;
import nl.tudelft.oopp.qubo.controllers.ModeratorViewController;
import nl.tudelft.oopp.qubo.controllers.StudentViewController;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionDetailsDto;
import nl.tudelft.oopp.qubo.utilities.sorting.Sorting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PollItem extends GridPane {
    private PollItem previousPollItem;
    private PollItem temp;
    private GridPane pollPane;
    private Text pollQuestion;
    private VBox pollVbox;

    private Set<PollOptionDetailsDto> options;

    private ScrollPane pollScPane;
    private HashMap<RadioButton, UUID> optionIds;

    private static StudentViewController sController;
    private static ModeratorViewController mController;

    private ToggleGroup optionGroup;

    /**
     * Constructs a new PollItem.
     *
     * @param text          The text that should be associated with the poll.
     * @param pollOptions   The options that students can select.
     * @param scrollPane    ScrollPane containing the VBox that will contain the poll.
     * @param controller    The Student View Controller associated with the client.
     */
    public PollItem(String text, Set<PollOptionDetailsDto> pollOptions, ScrollPane scrollPane,
                    StudentViewController controller, PollItem previous) {
        //Only set the previousPollItem if it did not have a prior value.
        if (previousPollItem == null) {
            previousPollItem = previous;
        }
        //Set the temporary PollItem variable to the previous poll.
        temp = previous;

        this.pollQuestion = new Text(text);
        this.options = pollOptions;
        pollScPane = scrollPane;

        optionIds = new HashMap<>();
        sController = controller;

        construct(false);
    }

    /**
     * Constructs a new PollItem.
     *
     * @param text          The text that should be associated with the poll.
     * @param pollOptions   The options that students can select.
     * @param scrollPane    ScrollPane containing the VBox that will contain the poll.
     * @param controller    The Moderator View Controller associated with the client.
     */
    public PollItem(String text, Set<PollOptionDetailsDto> pollOptions, ScrollPane scrollPane,
                    ModeratorViewController controller, PollItem previous) {
        //Only set the previousPollItem if it did not have a prior value.
        if (previousPollItem == null) {
            previousPollItem = previous;
        }
        //Set the temporary PollItem variable to the previous poll.
        temp = previous;

        this.pollQuestion = new Text(text);
        this.options = pollOptions;
        pollScPane = scrollPane;

        optionIds = new HashMap<>();
        mController = controller;

        construct(true);
    }

    /**
     * This method constructs the grid pane that will contain the poll and its options.
     *
     * @param moderator The boolean that represents the type of user.
     */
    private void construct(boolean moderator) {
        //Bind the managed property to the visible property so that the node is not accounted for
        //in the layout when it is not visible.
        pollQuestion.managedProperty().bind(pollQuestion.visibleProperty());

        //Set padding for the cell that will contain the Poll.
        this.setPadding(new Insets(3,0,20,3));

        //Construct a new pollPane to hold the poll, and add it to the content pane.
        pollPane = newPollPane();
        this.addRow(0, pollPane);

        //Add the poll options.
        addOptions(moderator);

        this.setGridLinesVisible(true);
    }

    /**
     * This method constructs a new pollPane to hold the poll text and poll options.
     *
     * @return A GridPane containing all poll information needed by the student.
     */
    private GridPane newPollPane() {
        GridPane gridpane = new GridPane();
        pollVbox = newPollVbox(pollQuestion, pollScPane);

        //Set padding for individual cells.
        gridpane.setPadding(new Insets(3,3,3,3));

        //Add the poll question to the grid pane and make sure that the poll question text does not overflow.
        gridpane.addColumn(1, pollVbox);

        //Set the column constraints.
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(new ColumnConstraints(5), col2);

        return gridpane;
    }

    /**
     * Constructs and returns a new VBox containing the poll text. This method is shared between PollItem
     * and PollResultItem.
     *
     * @return  Returns a new VBox that contains the poll text.
     */
    protected static VBox newPollVbox(Text pollText, ScrollPane pollScrollPane) {
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
        pollText.wrappingWidthProperty().bind(pollScrollPane.widthProperty()
                .subtract(pollScrollPane.getPadding().getLeft() + pollScrollPane.getPadding().getRight() + 19));

        return vbox;
    }

    /**
     * This method adds the poll options to the GridPane containing the poll.
     *
     * @param moderator The boolean that represents the type of user.
     */
    private void addOptions(boolean moderator) {
        //Sort the options on the length of their poll text.
        List<PollOptionDetailsDto> options = new ArrayList<>(this.options);
        Sorting.sortPollOptionsOnId(options);

        //Set up the toggle group to which all poll option radio buttons will be bound.
        if (!moderator) {
            optionGroup = new ToggleGroup();

            RadioButton selectedOption = sController.getSelectedOption();

            int i = 1;
            //Add all PollOptionDetailsDtos to the grid pane.
            for (PollOptionDetailsDto option : options) {
                HBox optionBox = createOption(option, selectedOption);
                optionBox.setPadding(new Insets(5, 5, 5, 5));
                this.addRow((i + 1), optionBox);
                i++;
            }
        } else {
            int i = 1;
            HBox optionBox = null;

            //TODO: Add labels for moderators and align them
        }

        //Update the previous poll item variable if the poll has not changed.
        if (temp != null) {
            if (optionIds.values().equals(temp.optionIds.values())) {
                previousPollItem = temp;
            }
        }
    }

    /**
     * This method creates a poll option radio button, and retains the option selection.
     *
     * @param option            The poll option that should be added to the VBox.
     * @param selectedOption    The poll option that was selected in the previous poll.
     * @return An HBox containing the radio button and a label with its associated text.
     */
    private HBox createOption(PollOptionDetailsDto option, RadioButton selectedOption) {
        //Create a new radio button for the poll option and set its toggle group.
        RadioButton optionButton = new RadioButton();
        optionButton.setToggleGroup(optionGroup);

        optionButton.setOnAction(e -> sController.handlePollChoice((RadioButton) optionGroup
                .getSelectedToggle(), PollItem.this));

        //If there was a previous PollItem, check if the added option was selected in that PollItem.
        //Set the option as selected if this is true.
        if (previousPollItem != null) {
            if (option.getOptionId().equals(previousPollItem.findOptionId(selectedOption))) {
                optionButton.setSelected(true);
                sController.setSelectedOption(optionButton);
            }
        }

        //Store the option ID associated with it.
        optionIds.put(optionButton, option.getOptionId());

        //Add the radio button to the left of the label with its option text.
        Label optionText = new Label(option.getOptionText());
        optionText.setGraphic(optionButton);
        optionText.setContentDisplay(ContentDisplay.LEFT);
        optionText.setWrapText(true);

        //Add the button and its associated text to an HBox and return this.
        HBox optionBox = new HBox(optionButton, optionText);
        optionBox.maxWidthProperty().bind(pollScPane.widthProperty().subtract(9));
        optionBox.minWidthProperty().bind(pollScPane.widthProperty().subtract(9));

        return optionBox;
    }

    /**
     * Returns the ID of the option associated with the radio button.
     *
     * @param button    The RadioButton that was selected and whose associated poll option should be retrieved.
     * @return The ID of the option associated with the radio button.
     */
    public UUID findOptionId(RadioButton button) {
        if (optionIds.containsKey(button)) {
            return optionIds.get(button);
        } else {
            return null;
        }
    }

}
