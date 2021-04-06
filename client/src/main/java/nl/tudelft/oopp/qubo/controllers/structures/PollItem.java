package nl.tudelft.oopp.qubo.controllers.structures;

import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import nl.tudelft.oopp.qubo.controllers.StudentViewController;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionDetailsDto;

import java.util.HashMap;
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
    private VBox pollContainer;
    private HashMap<RadioButton, UUID> optionIds;

    private static StudentViewController sController;

    private ToggleGroup optionGroup;

    /**
     * Constructs a new PollItem.
     *
     * @param text          The text that should be associated with the poll.
     * @param pollOptions   The options that students can select.
     * @param pollContainer The VBox containing the poll.
     * @param scrollPane    ScrollPane containing the VBox that contains the list of questions
     * @param controller    The Student View Controller associated with the client.
     */
    public PollItem(String text, Set<PollOptionDetailsDto> pollOptions, VBox pollContainer,
                        ScrollPane scrollPane, StudentViewController controller, PollItem previous) {
        //Only set the previousPollItem if it did not have a prior value.
        if (previousPollItem == null) {
            previousPollItem = previous;
        }
        //Set the temporary PollItem variable to the previous poll.
        temp = previous;

        this.pollQuestion = new Text(text);
        this.options = pollOptions;
        this.pollContainer = pollContainer;
        pollScPane = scrollPane;

        optionIds = new HashMap<>();
        sController = controller;

        construct();
    }

    /**
     * This method constructs the gridpane that will contain the poll and its options.
     */
    private void construct() {
        //Bind the managed property to the visible property so that the node is not accounted for
        //in the layout when it is not visible.
        pollQuestion.managedProperty().bind(pollQuestion.visibleProperty());

        //Set padding for the cell that will contain the Poll.
        this.setPadding(new Insets(0,10,20,0));

        //Construct a new questionPane to hold the question and add to content pane.
        pollPane = newPollPane();
        this.addRow(0, pollPane);

        //Add the poll options.
        addOptions();

        this.setGridLinesVisible(true);
    }

    /**
     * This method constructs a new pollPane to hold the poll text and poll options.
     *
     * @return A GridPane containing all poll information needed by the student.
     */
    private GridPane newPollPane() {
        GridPane gridpane = new GridPane();

        //Add the poll question to the grid pane and make sure that the poll question text does not overflow.
        gridpane.addColumn(1, pollQuestion);
        pollQuestion.wrappingWidthProperty().bind(pollScPane.widthProperty().subtract(150));

        //Set column constraints.
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        gridpane.getColumnConstraints().addAll(new ColumnConstraints(10), col2,
                new ColumnConstraints(50));

        //Set padding.
        gridpane.setPadding(new Insets(10,3,5,3));

        return gridpane;
    }

    /**
     * This method adds the poll options to the GridPane containing the poll.
     */
    private void addOptions() {
        VBox pollOptionBox = new VBox();

        //Set up the toggle group to which all poll options will be bound
        optionGroup = new ToggleGroup();

        RadioButton selectedOption = sController.getSelectedOption();

        //Add all PollOptionDetailsDtos to the VBox.
        for (PollOptionDetailsDto option : options) {
            RadioButton optionButton = createOption(option, selectedOption, pollOptionBox);
            pollOptionBox.getChildren().add(optionButton);
        }

        //Update the previous poll item variable if the poll has not changed.
        if (temp != null) {
            if (optionIds.values().equals(temp.optionIds.values())) {
                previousPollItem = temp;
            }
        }

        pollOptionBox.setPadding(new Insets(10,15,10,15));

        //Add the option box to the PollItem GridPane
        this.addRow(1, pollOptionBox);
    }

    /**
     * This method creates a poll option radio button, and retains option selection.
     *
     * @param option            The poll option that should be added to the VBox.
     * @param selectedOption    The poll option that was selected in the previous poll.
     * @param pollOptionBox     The VBox to which the option should be added.
     * @return The created poll option RadioButton.
     */
    private RadioButton createOption(PollOptionDetailsDto option, RadioButton selectedOption,
                                     VBox pollOptionBox) {
        //Create a new radio button for the poll option and add it to the menu
        RadioButton optionButton = new RadioButton(option.getOptionText());
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

        return optionButton;
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
