package nl.tudelft.oopp.qubo.controllers.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.UUID;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import nl.tudelft.oopp.qubo.communication.BanUserCommunication;
import nl.tudelft.oopp.qubo.communication.QuestionCommunication;
import nl.tudelft.oopp.qubo.communication.QuestionVoteCommunication;
import nl.tudelft.oopp.qubo.controllers.ModeratorViewController;
import nl.tudelft.oopp.qubo.controllers.StudentViewController;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.qubo.views.AlertDialog;

/**
 * The application action events.
 */
public class QuBoActionEvents {
    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    /**
     * This method is run when the upvote button is clicked.
     * When the ToggleButton is activated: Sends a request to the server to add a vote.
     * When the ToggleButton is deactivated: Sends a request to the server to remove the vote.
     *
     * @param questionId     UUID of the question that the user decides to upvote.
     * @param upvoteMap      HashMap of questionId:upvoteId.
     * @param upvoteTriangle The ToggleButton which the user clicks to add a vote.
     * @param upvoteNumber   Label displaying the number of upvotes for the question.
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
     * @param questionId     The UUID of the question that has been upvoted.
     * @param upvoteMap      HashMap of questionId:upvoteId.
     * @param upvoteTriangle The upvote ToggleButton. (Needs to be deselected if request fails.)
     * @param upvoteNumber   Label displaying the number of upvotes for the question.
     */
    public static void toggleUpvoteTrue(UUID questionId, HashMap<UUID, UUID> upvoteMap,
                                        ToggleButton upvoteTriangle, Label upvoteNumber) {
        String response = QuestionVoteCommunication.addQuestionVote(questionId);

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
     * @param questionId     The UUID of the question that the upvote needs to be removed from.
     * @param upvoteMap      HashMap of questionId:upvoteId.
     * @param upvoteTriangle The upvote ToggleButton. (Needs to be reselected if request fails.)
     * @param upvoteNumber   Label displaying the number of upvotes for the question.
     */
    public static void toggleUpvoteFalse(UUID questionId, HashMap<UUID, UUID> upvoteMap,
                                         ToggleButton upvoteTriangle, Label upvoteNumber) {
        String response = QuestionVoteCommunication.deleteQuestionVote(questionId, upvoteMap.get(questionId));

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
     * Creates a new TextArea with its width bound to the ObservableDoubleValue passed into the method.
     *
     * @param widthBind ObservableDoubleValue to be bound to the width of the Text Area.
     * @return A new TextArea with its width bound.
     */
    public static TextArea newTextArea(ObservableDoubleValue widthBind) {
        //Create a new TextArea and bind its size
        TextArea input = new TextArea();
        input.setWrapText(true);

        input.prefWidthProperty().bind(widthBind);
        input.maxWidthProperty().bind(widthBind);
        input.minWidthProperty().bind(widthBind);
        input.setPrefRowCount(5);

        return input;
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
     * @param questionBody Text node of the question content. (Needs to be hidden when editing.)
     * @param questionVbox VBox containing the questionBody and the authorName.
     *                     (Needed to display the text area in it.)
     * @param options      The options menu node. (Needs to be disabled when editing.)
     * @param questionId   The UUID of the question that is being edited.
     * @param code         Secret code of the question.
     */
    public static void editQuestionOption(Text questionBody, VBox questionVbox, MenuButton options,
                                          UUID questionId, UUID code) {
        //Disable options menu
        options.setDisable(true);

        //Create the warning Label
        Label warning = new Label("Error: Question must be more than 8 characters long.");
        warning.setWrapText(true);
        warning.setTextFill(Color.web("#ef4f4f"));
        warning.setStyle("-fx-font-style: italic");
        warning.managedProperty().bind(warning.visibleProperty());
        warning.setVisible(false);

        //Create the Cancel and Update buttons
        Button cancel = new Button("Cancel");
        Button update = new Button("Update");

        //Add the buttons and label to an HBox, and set the way this HBox is displayed on the screen
        HBox buttons = new HBox(warning, cancel, update);
        warning.prefWidthProperty().bind(questionBody.wrappingWidthProperty()
            .subtract(cancel.widthProperty().add(update.widthProperty().add(30))));
        HBox.setHgrow(warning, Priority.ALWAYS);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setSpacing(15);

        //Create a new TextArea and bind its size
        TextArea input = newTextArea(questionBody.wrappingWidthProperty());

        //Set action listeners for the buttons
        update.setOnAction(event -> updateQuestion(options, questionId, code, input.getText(),
            questionBody, questionVbox, input, buttons, warning));
        cancel.setOnAction(event -> cancelEdit(options, questionVbox, input, buttons, questionBody));

        //Hide question text and display text area
        questionBody.setVisible(false);
        questionVbox.getChildren().add(input);
        questionVbox.getChildren().add(buttons);
        input.setText(questionBody.getText());
    }

    /**
     * This method runs when the Update button (created in editQuestion) is clicked.
     * Sends an editQuestion request to the server.
     * /
     * If the request is successful -> Displays alert, removes the text area and buttons, and updates
     * the question content locally as well.
     * If the request fails -> Displays an alert and prevents the user from losing the edited question.
     *
     * @param options      The options menu node. (Needs to be disabled when editing.)
     * @param questionId   The UUID of the question that is being edited.
     * @param code         Secret code of the question.
     * @param text         Content of the text area. (Edited question.)
     * @param questionBody Text node of the question content. (Needs to be shown after successful edit.)
     * @param questionVbox VBox containing the questionBody and the authorName.
     *                     (Needed to remove the text area in it.)
     * @param input        The Text Area node. (Needs to be removed after a successful edit.)
     * @param buttons      The HBox containing the buttons. (Needs to be removed after a successful edit.)
     * @param warning      Label containing warning that the content inside of the TextArea might be
     *                     less than 8 characters.
     */
    public static void updateQuestion(MenuButton options, UUID questionId, UUID code, String text,
                                      Text questionBody, VBox questionVbox, TextArea input,
                                      HBox buttons, Label warning) {
        if (text.length() <= 8) {
            warning.setVisible(true);
            return;
        } else {
            warning.setVisible(false);
        }

        //Send a request to the server
        String response = QuestionCommunication.editQuestion(questionId, code, text);

        if (response == null) {
            //If request failed
            AlertDialog.display("Unsuccessful Request", "Failed to update your question, please try again.");
        } else {
            //If request successful
            //Remove text area and buttons
            questionVbox.getChildren().remove(input);
            questionVbox.getChildren().remove(buttons);
            //Set edited text to question content area and show
            questionBody.setText(text);
            questionBody.setVisible(true);
            //Enable options menu as editing has been completed successfully
            options.setDisable(false);
        }
    }

    /**
     * This method runs when the Cancel button (created in editQuestion) is clicked.
     * Removes all the nodes that were added when editing the question and displays original question.
     *
     * @param options      The options menu node. (Needs to be disabled when editing.)
     * @param questionVbox VBox containing the questionBody and the authorName.
     *                     (Needed to remove the text area in it.)
     * @param input        The Text Area node. (Needs to be removed after a successful edit.)
     * @param buttons      The HBox containing the buttons. (Needs to be removed after a successful edit.)
     * @param questionBody Text node of the question content. (Needs to be shown after successful edit.)
     */
    public static void cancelEdit(MenuButton options, VBox questionVbox, TextArea input,
                                  HBox buttons, Text questionBody) {
        options.setDisable(false);
        questionVbox.getChildren().remove(input);
        questionVbox.getChildren().remove(buttons);
        questionBody.setVisible(true);
    }

    /**
     * This method runs when the user selects Delete from the options Menu.
     * Displays a confirmation dialogue and two buttons ("Yes" and "Cancel").
     * /
     * Yes -> Sends a request to the server to delete the question.
     * Cancel -> Cancels the action.
     *
     * @param content           GridPane of the cell. (Needs to be hidden after deletion.)
     * @param questionPane      GridPane of the question. (Needed to add a row for the confirmation dialogue.)
     * @param questionBody      Text node of the question content. (Needed for layout purposes.)
     * @param options           The options menu node.
     *                          (Needs to be disabled when confirmation dialogue shows up.)
     * @param questionId        The UUID of the question that is being deleted.
     * @param code              Secret code of the question.
     */
    public static void deleteQuestionOption(GridPane content, GridPane questionPane, Text questionBody,
                                            MenuButton options, UUID questionId, UUID code) {
        options.setDisable(true);

        //Create new InQuestionDialog
        InQuestionDialog dialog = new InQuestionDialog(options, questionBody,
            questionPane, "Are you sure you want to delete this question?");

        //Set action listeners
        dialog.yes.setOnAction(event -> deleteQuestion(options, content, questionId, code));
        dialog.cancel.setOnAction(event -> cancelDialog(options, questionPane, dialog.dialogue));
    }



    /**
     * This method runs when the Delete button (created in deleteQuestion) is clicked.
     * Sends a deleteQuestion request to the server.
     * /
     * If the request is successful -> Displays an alert.
     * If the request fails -> Displays successful removal label and icon.
     *
     * @param content    GridPane of the cell. (Needs to be hidden after deletion.)
     * @param questionId The UUID of the question that is being edited.
     * @param code       Secret code of the question.
     */
    public static void deleteQuestion(MenuButton options, GridPane content, UUID questionId, UUID code) {
        //Send a request to the server
        String response = QuestionCommunication.deleteQuestion(questionId, code);

        if (response == null) {
            //If the request failed
            AlertDialog.display("Unsuccessful Request", "Failed to delete your question, please try again.");
        } else {
            //If the request was successful
            AlertDialog.display("", "Question deletion successful.");
            content.setVisible(false);
            content.setManaged(false);
            options.setDisable(false);
        }
    }

    /**
     * This method runs when the Cancel button in in-question-dialogues is clicked.
     * Removes the confirmation dialogue and enables the options menu.
     *
     * @param options  The options menu node. (Needs to be enabled.)
     * @param gridPane GridPane of the cell. (Needed to remove row of confirmation dialogue.)
     * @param dialogue The confirmation dialogue. (Needs to be removed.)
     */
    public static void cancelDialog(MenuButton options, GridPane gridPane, HBox dialogue) {
        gridPane.getChildren().remove(dialogue);
        options.setDisable(false);
    }

    /**
     * This method runs when the user selects Mark As Answered from the options Menu.
     *
     * @param options       The options menu node. (Needs to be enabled.)
     * @param questionBody  Text node of the question content. (Needed for layout purposes.)
     * @param questionPane  GridPane of the question. (Needed to add a row for the confirmation dialogue.)
     * @param questionId    The UUID of the question that is being marked as answered.
     * @param code          The moderator code of the board.
     */
    public static void markAsAnsOption(MenuButton options, Text questionBody, GridPane questionPane,
                                       UUID questionId, UUID code) {
        options.setDisable(true);

        InQuestionDialog dialog =  new InQuestionDialog(options, questionBody, questionPane,
            "Are you sure you want to mark this question as answered?");
        dialog.yes.setOnAction(event -> markAsAns(options, questionId, code));
        dialog.cancel.setOnAction(event -> cancelDialog(options, questionPane, dialog.dialogue));
    }

    /**
     * This method runs when the Delete button (created in markAsAnsOption) is clicked.
     * Sends a markQuestionAsAnswered request to the server.
     *
     * @param questionId    The UUID of the question that is being marked as answered.
     * @param code          The moderator code of the board.
     */
    private static void markAsAns(MenuButton options, UUID questionId, UUID code) {
        String response = QuestionCommunication.markQuestionAsAnswered(questionId, code);

        if (response == null) {
            //If the request failed
            AlertDialog.display("Unsuccessful Request", "Failed to mark question as answered, "
                + "please try again.");
        } else {
            //If the request was successful
            AlertDialog.display("", "Question has been marked as answered.");
            options.setDisable(false);
        }
    }

    /**
     * This method runs when the user selects Reply from the options Menu.
     *
     * @param content      GridPane of the cell. (Needed to get the row count and display the new reply
     *                     after all other replies.)
     * @param questionPane GridPane of the question. (Needed to add a row for the reply Pane.)
     * @param questionId   The UUID of the question that is being replied to.
     * @param code         The moderator code of the board.
     * @param options      The options menu node. (Needs to be disabled when replying.)
     * @param questionBody Text node of the question content. (Needed to bind the width of the answer to.)
     */
    public static void replyToQuestionOption(GridPane content, GridPane questionPane,
                                             UUID questionId, UUID code, MenuButton options,
                                             Text questionBody) {
        //Disable options menu
        options.setDisable(true);

        //Create a new TextArea with the width bound to the same width as the question body
        //to prevent horizontal overflow
        TextArea input = newTextArea(questionBody.wrappingWidthProperty().add(60));
        input.setPromptText("Enter your reply...");

        //Create the buttons and set their alignments
        Button cancel = new Button("Cancel");
        Button reply = new Button("Reply");
        HBox buttons = new HBox(cancel, reply);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setSpacing(15);

        //Create a VBox to arrange the TextArea and buttons
        VBox answerBox = new VBox(input, buttons);
        answerBox.setPadding(new Insets(10, 20, 10, 20));
        answerBox.setSpacing(10);

        //Set actions events for the buttons
        reply.setOnAction(event -> replyToQuestion(content, questionPane, answerBox, questionBody,
            options, questionId, code, input.getText().trim()));
        cancel.setOnAction(event -> cancelReply(questionPane, answerBox, options));

        //Make the answerBox span the remaining columns so it displays fully
        GridPane.setColumnSpan(answerBox, GridPane.REMAINING);
        questionPane.addRow(1, answerBox);
    }

    /**
     * This method runs when the Reply button (created in replyToQuestionOption) is clicked.
     *
     * @param content      GridPane of the cell. (Needed to get the row count and display the new reply
     *                     after all other replies.)
     * @param questionPane GridPane of the question. (Needed to add a row for the reply Pane.)
     * @param answerBox    VBox containing the TextArea and buttons for the reply interface.
     * @param questionBody Text node of the question content. (Needed to bind the width of the answer to.)
     * @param options      The options menu node. (Needs to be enabled after successful reply.)
     * @param questionId   The UUID of the question that is being replied to.
     * @param modCode      The moderator code of the board.
     * @param text         Text body of the reply.
     */
    private static void replyToQuestion(GridPane content, GridPane questionPane, VBox answerBox,
                                        Text questionBody, MenuButton options,
                                        UUID questionId, UUID modCode, String text) {
        String response = QuestionCommunication.addAnswer(questionId, modCode, text);

        if (response == null) {
            //If the request failed
            AlertDialog.display("Unsuccessful Request", "Failed to reply to question, "
                + "please try again.");
        } else {
            //If the request was successful
            cancelReply(questionPane, answerBox, options);

            //Create Text node to contain the reply
            Text answer = new Text(text);
            BorderPane answerPane = new BorderPane(answer);
            answerPane.setPadding(new Insets(10, 15, 10, 15));
            answer.wrappingWidthProperty().bind(questionBody.wrappingWidthProperty().add(40));

            //Add answer to the VBox containing the questions (and answers)
            content.addRow(content.getRowCount(), answerPane);

            options.setDisable(false);
        }
    }

    /**
     * This method runs when the Cancel button (created in replyToQuestionOption) is clicked.
     *
     * @param questionPane GridPane of the question. (Needed to remove answerBox from.)
     * @param answerBox    VBox containing the TextArea and buttons for the reply interface.
     * @param options      The options menu node. (Needs to be enabled after successful reply.)
     */
    private static void cancelReply(GridPane questionPane, VBox answerBox, MenuButton options) {
        questionPane.getChildren().remove(answerBox);
        options.setDisable(false);
    }

    /**
     * This method runs when the user selects Ban from the options Menu.
     *
     * @param options       The options menu node. (Needs to be enabled after successful ban.)
     * @param questionBody  Text node of the question content. (Needed for layout purposes.)
     * @param questionPane  GridPane of the question. (Needed to add a row for the ban dialogue.)
     * @param questionId    The UUID of the question where a ban is being requested.
     * @param modCode       The moderator code of the board.
     */
    public static void banUserOption(MenuButton options, GridPane questionPane,
                                     Text questionBody, UUID questionId, UUID modCode) {
        options.setDisable(true);

        InQuestionDialog dialog =  new InQuestionDialog(options, questionBody, questionPane,
            "Are you sure you want to ban the user who posted this question?");

        dialog.yes.setOnAction(event -> banUser(
            options, questionPane, dialog.dialogue, questionId, modCode));
        dialog.cancel.setOnAction(event -> cancelDialog(options, questionPane, dialog.dialogue));
    }

    /**
     * This method runs when the Ban button (created in banUserOption) is clicked.
     * Sends a banUser request to the server.
     *
     * @param options       The options menu node. (Needs to be enabled after successful ban.)
     * @param questionPane  GridPane of the question. (Needed to add a row for the ban dialogue.)
     * @param questionVbox  VBox containing the questionBody and the authorName.
     * @param questionId    The UUID of the question where a ban is being requested.
     * @param modCode       The moderator code of the board.
     */
    private static void banUser(MenuButton options, GridPane questionPane, HBox questionVbox,
                                UUID questionId, UUID modCode) {
        boolean successful = BanUserCommunication.banUser(questionId, modCode);

        if (successful) {
            //If the request was successful
            cancelDialog(options, questionPane, questionVbox);
            AlertDialog.display("", "User IP successfully banned.");
            options.setDisable(false);
        } else {
            //If the request failed
            AlertDialog.display("Unsuccessful Request",
                "Failed to ban user IP, please try again.");
        }
    }
}
