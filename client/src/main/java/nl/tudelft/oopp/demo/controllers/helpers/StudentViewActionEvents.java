package nl.tudelft.oopp.demo.controllers.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.demo.views.AlertDialog;

import java.util.HashMap;
import java.util.UUID;

public class StudentViewActionEvents {
    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    /**
     * This method is run when the upvote button is clicked.
     * When the ToggleButton is activated: Sends a request to the server to add a vote.
     * When the ToggleButton is deactivated: Sends a request to the server to remove the vote.
     *
     * @param questionId        UUID of the question that the user decides to upvote.
     * @param upvoteTriangle    The ToggleButton which the user clicks to add a vote.
     */
    public static void upvoteQuestion(UUID questionId, HashMap<UUID, UUID> upvoteMap,
                                      ToggleButton upvoteTriangle, Label upvoteNumber) {
        if (upvoteTriangle.isSelected()) {
            //Code that runs when the button is activated
            //Check if the question has already been upvoted
            if (upvoteMap.containsKey(questionId)) {
                AlertDialog.display("", "You have already upvoted this question!");
                return;
            }
            toggleUpvoteTrue(questionId, upvoteMap, upvoteTriangle, upvoteNumber);
        } else {
            //Code that runs when the button is deactivated
            toggleUpvoteFalse(questionId, upvoteMap, upvoteTriangle, upvoteNumber);
        }
    }

    /**
     * This method is called when the upvote button is toggled on/selected.
     * Sends a request to add a vote to the server, and executes different behaviour depending
     * on the received response.
     *
     * @param questionId        The UUID of the question that has been upvoted.
     * @param upvoteTriangle    The upvote ToggleButton (Needs to be deselected if request fails).
     */
    public static void toggleUpvoteTrue(UUID questionId, HashMap<UUID, UUID> upvoteMap,
                                        ToggleButton upvoteTriangle, Label upvoteNumber) {
        String response = ServerCommunication.addQuestionVote(questionId);

        if (response == null) {
            AlertDialog.display("", "Upvote failed.");
            //Unselect the button as the upvote action failed
            upvoteTriangle.setSelected(false);
        } else {
            //When the request is successful, store the question UUID with the vote UUID in the HashMap
            QuestionVoteDetailsDto dto = gson.fromJson(response, QuestionVoteDetailsDto.class);
            upvoteMap.put(questionId, dto.getId());
            //Update upvote number locally
            int number = Integer.parseInt(upvoteNumber.getText());
            number++;
            upvoteNumber.setText(Integer.toString(number));
        }
    }

    /**
     * This method is called when the upvote button is toggled off/deselected.
     * Sends a request to delete a vote from the server, and executes different behaviour depending
     * on the received response.
     *
     * @param questionId        The UUID of the question that the upvote needs to be removed from.
     * @param upvoteTriangle    The upvote ToggleButton (Needs to be reselected if request fails).
     */
    public static void toggleUpvoteFalse(UUID questionId, HashMap<UUID, UUID> upvoteMap,
                                         ToggleButton upvoteTriangle, Label upvoteNumber) {
        String response = ServerCommunication.deleteQuestionVote(questionId, upvoteMap.get(questionId));

        if (response == null) {
            AlertDialog.display("", "Canceling upvote failed.");
            //Reselect the button as the un-upvote action failed
            upvoteTriangle.setSelected(true);
        } else {
            //When the request is successful, remove upvote from the HashMap
            upvoteMap.remove(questionId);
            //Update upvote number locally
            int number = Integer.parseInt(upvoteNumber.getText());
            number--;
            upvoteNumber.setText(Integer.toString(number));
        }
    }

    /**
     * This method runs when the user selects Edit from the options Menu.
     * Hides the original Text node and displays a Text Area node with the content of the
     * original question set in the Text Area for easier editing.
     * /
     * Displays two buttons ("Cancel" and "Update").
     * Update -> Sends a request to the server to update question content
     * Cancel -> Cancels the action
     *
     * @param questionContent   Text node of the question content (Needs to be hidden when editing)
     * @param questionVbox      VBox containing question content (Needed to display the text area in it)
     * @param options           The options menu node (Needs to be disabled when editing)
     * @param questionId        The UUID of the question that is being edited
     * @param code              Secret code of the question
     */
    public static void editQuestionOption(Text questionContent, VBox questionVbox, MenuButton options,
                                          UUID questionId, UUID code) {
        //Disable options menu
        options.setDisable(true);

        //Create a new TextArea and bind its size
        TextArea input = new TextArea();
        input.setWrapText(true);
        input.prefWidthProperty().bind(questionContent.wrappingWidthProperty());
        input.minWidthProperty().bind(questionContent.wrappingWidthProperty());
        input.maxWidthProperty().bind(questionContent.wrappingWidthProperty());
        input.setPrefRowCount(5);

        //Create the buttons and set their alignments
        Button cancel = new Button("Cancel");
        Button update = new Button("Update");
        HBox buttons = new HBox(cancel, update);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setSpacing(15);

        //Set action listeners for the buttons
        update.setOnAction(event -> updateQuestion(options, questionId, code, input.getText(),
            questionContent, questionVbox, input, buttons));
        cancel.setOnAction(event -> cancelEdit(options, questionVbox, input, buttons, questionContent));

        //Hide question text and display text area
        questionContent.setVisible(false);
        questionVbox.getChildren().add(input);
        questionVbox.getChildren().add(buttons);
        input.setText(questionContent.getText());
    }

    /**
     * This method runs when the Update button (created in editQuestion) is clicked.
     * Sends an editQuestion request to the server.
     * /
     * If the request is successful -> Displays alert, removes the text area and buttons, and updates
     * the question content locally as well.
     * If the request fails -> Displays an alert and prevents the user from losing the edited question.
     *
     * @param options           The options menu node (Needs to be disabled when editing)
     * @param questionId        The UUID of the question that is being edited
     * @param code              Secret code of the question
     * @param text              Content of the text area (Edited question)
     * @param questionContent   Text node of the question content (Needs to be shown after successful edit)
     * @param questionVbox      VBox containing question content (Needed to remove the text area in it)
     * @param input             The Text Area node (Needs to be removed after a successful edit)
     * @param buttons           The HBox containing the buttons (Needs to be removed after a successful edit)
     */
    public static void updateQuestion(MenuButton options, UUID questionId, UUID code, String text,
                               Text questionContent, VBox questionVbox, TextArea input,
                               HBox buttons) {
        //Send a request to the server
        String response = ServerCommunication.editQuestion(questionId, code, text);

        if (response == null) {
            //If request failed
            AlertDialog.display("Unsuccessful Request", "Failed to update your question, please try again.");
        } else {
            //If request successful
            //Remove text area and buttons
            questionVbox.getChildren().remove(input);
            questionVbox.getChildren().remove(buttons);
            //Set edited text to question content area and show
            questionContent.setText(text);
            questionContent.setVisible(true);
            //Enable options menu as editing has been completed successfully
            options.setDisable(false);
        }
    }

    /**
     * This method runs when the Cancel button (created in editQuestion) is clicked.
     * Removes all the nodes that were added when editing the question and displays original question.
     *
     * @param options           The options menu node (Needs to be disabled when editing)
     * @param questionVbox      VBox containing question content (Needed to remove the text area in it)
     * @param input             The Text Area node (Needs to be removed after a successful edit)
     * @param buttons           The HBox containing the buttons (Needs to be removed after a successful edit)
     * @param questionContent   Text node of the question content (Needs to be shown after successful edit)
     */
    public static void cancelEdit(MenuButton options, VBox questionVbox, TextArea input,
                           HBox buttons, Text questionContent) {
        options.setDisable(false);
        questionVbox.getChildren().remove(input);
        questionVbox.getChildren().remove(buttons);
        questionContent.setVisible(true);
    }

    /**
     * This method runs when the user selects Delete from the options Menu.
     * Displays a confirmation dialogue and two buttons ("Yes" and "Cancel").
     * /
     * Yes -> Sends a request to the server to delete the question.
     * Cancel -> Cancels the action.
     *
     * @param gridpane      GridPane of the cell (Needed to add a row for the confirmation dialogue)
     * @param options       The options menu node (Needs to be disabled when confirmation dialogue shows up)
     * @param questionId    The UUID of the question that is being deleted
     * @param code          Secret code of the question
     */
    public static void deleteQuestionOption(GridPane gridpane, ListView<Question> questionList,
                                            MenuButton options, UUID questionId, UUID code) {
        //Disable options menu
        options.setDisable(true);

        //Create new label
        Label confirmation = new Label("Are you sure you want to delete this question?");
        confirmation.setPadding(new Insets(0,5,0,0));
        confirmation.setWrapText(true);
        //Set width bounds to the dialogue so it doesn't overflow the question box
        double widthBind = questionList.getPadding().getLeft()
            +  questionList.getPadding().getRight() + 200;
        confirmation.prefWidthProperty().bind(questionList.widthProperty().subtract(widthBind));

        //Create buttons
        Button yes = new Button("Yes");
        Button cancel = new Button("Cancel");
        HBox dialogue = new HBox(confirmation, yes, cancel);

        //Set layouts
        dialogue.setPadding(new Insets(5,10,5,10));
        dialogue.setSpacing(15);
        dialogue.setAlignment(Pos.CENTER);

        //Show confirmation dialogue
        gridpane.addRow(1, dialogue);
        GridPane.setColumnSpan(dialogue, GridPane.REMAINING);

        //Set action listeners
        yes.setOnAction(event -> deleteQuestion(gridpane, questionId, code));
        cancel.setOnAction(event -> cancelDeletion(options, gridpane, dialogue));
    }

    /**
     * This method runs when the Delete button (created in deleteQuestion) is clicked.
     * Sends a deleteQuestion request to the server.
     * /
     * If the request is successful -> Displays an alert.
     * If the request fails -> Displays successful removal label and icon.
     *
     * @param gridPane      GridPane of the cell (Needed to add a row for the confirmation dialogue)
     * @param questionId    The UUID of the question that is being edited
     * @param code          Secret code of the question
     */
    public static void deleteQuestion(GridPane gridPane, UUID questionId, UUID code) {
        //Send a request to the server
        String response = ServerCommunication.deleteQuestion(questionId, code);

        if (response == null) {
            //If the request failed
            AlertDialog.display("Unsuccessful Request", "Failed to delete your question, please try again.");
        } else {
            //If the request was successful
            AlertDialog.display("", "Question deletion successful.");
            //TODO: Display successful removal label and icon
            gridPane.setVisible(false);
            gridPane.setManaged(false);
        }
    }

    /**
     * This method runs when the Cancel button (created in deleteQuestion) is clicked.
     * Removes the confirmation dialogue and enables the options menu.
     *
     * @param options   The options menu node (Needs to be enabled)
     * @param gridPane  GridPane of the cell (Needed to remove row of confirmation dialogue)
     * @param dialogue  The confirmation dialogue (Needs to be removed)
     */
    public static void cancelDeletion(MenuButton options, GridPane gridPane, HBox dialogue) {
        gridPane.getChildren().remove(dialogue);
        options.setDisable(false);
    }
}
