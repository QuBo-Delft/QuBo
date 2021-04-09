package nl.tudelft.oopp.qubo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.communication.PaceVoteCommunication;
import nl.tudelft.oopp.qubo.communication.PollCommunication;
import nl.tudelft.oopp.qubo.communication.QuestionCommunication;
import nl.tudelft.oopp.qubo.communication.QuestionVoteCommunication;
import nl.tudelft.oopp.qubo.controllers.helpers.LayoutProperties;
import nl.tudelft.oopp.qubo.controllers.helpers.PollRefresh;
import nl.tudelft.oopp.qubo.controllers.helpers.QuBoInformation;
import nl.tudelft.oopp.qubo.controllers.helpers.QuestionRefresh;
import nl.tudelft.oopp.qubo.controllers.helpers.SideBarControl;
import nl.tudelft.oopp.qubo.controllers.structures.PollItem;
import nl.tudelft.oopp.qubo.dtos.pollvote.PollVoteDetailsDto;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import nl.tudelft.oopp.qubo.views.AlertDialog;
import nl.tudelft.oopp.qubo.views.ConfirmationDialog;
import nl.tudelft.oopp.qubo.views.GetTextDialog;
import nl.tudelft.oopp.qubo.views.QuBoDocumentation;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller for the StudentView.fxml sheet.
 */
public class StudentViewController {
    @FXML
    public Button askBtn;
    @FXML
    private HBox topBar;
    @FXML
    private Button boardInfo;
    @FXML
    private Button helpDoc;
    @FXML
    private StackPane content;
    @FXML
    private BorderPane paceVotePane;

    @FXML
    private Button leaveQuBo;
    @FXML
    private Label boardTitle;
    @FXML
    private Label sideMenuTitle;
    @FXML
    private ImageView boardStatusIcon;
    @FXML
    private MenuItem studentCodeItem;
    @FXML
    private MenuItem studentCodeItemBtn;
    @FXML
    private ScrollPane sideMenuPane;
    @FXML
    private Label boardStatusText;

    //Nodes from the unanswered question list
    @FXML
    private ScrollPane unAnsQuScPane;
    @FXML
    private VBox unAnsQuVbox;

    //Nodes from the side menu
    @FXML
    private ToggleButton hamburger;
    @FXML
    private VBox sideBar;
    @FXML
    private VBox sideMenu;
    @FXML
    private VBox ansQuVbox;
    @FXML
    private VBox pollVbox;

    //Buttons
    @FXML
    private ToggleButton ansQuestions;
    @FXML
    private ToggleButton polls;

    // The VBox pace votes are placed in, their toggle group, and the "All right" button.
    @FXML
    private VBox paceVbox;
    @FXML
    ToggleGroup pace;
    @FXML
    RadioButton justRight;

    /**
    * Records if the side menu was open before hiding.
    */
    private boolean sideMenuOpen;

    /**
     * Stage to be shown when the QuBo details button is clicked.
     */
    Stage popUp = new Stage();
    /**
     * Dto to set upon creation of a pace vote.
     */
    PaceVoteCreationDto paceVoteCreationDto;
    /**
     * Pace vote that was pressed before update method call.
     */
    Toggle previouslyPressed;

    private String authorName;

    /**
     * Variables used to keep track of the student's poll choice.
     */
    private UUID optionVote;
    private RadioButton selectedOption;
    private PollItem pollItem;

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    /**
    * HashMap of questionId:upvoteId, needed when deleting vote.
    */
    private HashMap<UUID, UUID> upvoteMap = new HashMap<>();

    /**
    * HashMap of questionId:secretCode, needed when editing and deleting questions.
    */
    private HashMap<UUID, UUID> secretCodeMap = new HashMap<>();

    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();

    private QuestionBoardDetailsDto quBo;


    private AtomicBoolean refreshing = new AtomicBoolean(true);
    private Timer timer = new Timer();
    private TimerTask refreshQuestions = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(() -> conditionalRefresh(refreshing.get()));
        }
    };


    /**
     * Method that sets the QuestionBoardDetailsDto of the student view.
     *
     * @param quBo The QuestionBoardDetailsDto of the question board that the student joined.
     */
    public void setQuBo(QuestionBoardDetailsDto quBo) {
        this.quBo = quBo;
    }

    /**
     * Method that sets the username of the application user.
     *
     * @param authorName The name of the student that joined the question board.
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * This method is called by the SceneLoader and takes the QuestionBoardDetailsDto, board status icon,
     * text and title. With these parameters it calls the setBoardDetails method in the QuBoInformation class
     * which actually sets their values.
     */
    public void setBoardDetails() {
        QuBoInformation.setBoardDetails(quBo, boardStatusIcon, boardStatusText, boardTitle, askBtn);
    }

    /**
     * Returns the paceVoteCreationDto of a student view object. It is used by the SceneLoader class
     * to check whether a pace vote has been set when attempting to close the student view stage.
     *
     * @return  The paceVoteCreationDto of the object.
     */
    public PaceVoteCreationDto getPaceVoteCreationDto() {
        return paceVoteCreationDto;
    }

    /**
     * This method is called by a PollRefresh method and returns the current PollItem.
     */
    public PollItem getPollItem() {
        return pollItem;
    }

    /**
     * This method is called by a PollRefresh method and sets the current PollItem.
     */
    public void setPollItem(PollItem pollItem) {
        this.pollItem = pollItem;
    }

    /**
     * Returns the selected option. It is used by the PollItem class to re-select the selected poll option
     * when refreshing the polls.
     *
     * @return  The RadioButton associated with the option that the student voted for.
     */
    public RadioButton getSelectedOption() {
        return selectedOption;
    }

    /**
     * This method is called by the PollItem addOptions method and sets the current selected option.
     */
    public void setSelectedOption(RadioButton selectedOption) {
        this.selectedOption = selectedOption;
    }

    /**
     * Code that is run upon loading StudentView.fxml
     */
    @FXML
    private void initialize() {
        // Helps restore the toggle group on failure by setting up a listener for changes made to its
        // radio buttons and storing the previously selected radio button
        pace.selectedToggleProperty().addListener(
            (observable, oldValue, newValue) -> previouslyPressed = oldValue);

        startUpProperties();
        timer.scheduleAtFixedRate(refreshQuestions, 0, 2000);
    }

    /**
     * Refresh the student view by refreshing the question list.
     */
    public void refresh() {
        QuestionRefresh.studentRefresh(this, quBo, unAnsQuVbox, ansQuVbox, upvoteMap, secretCodeMap,
            unAnsQuScPane, sideMenuPane);

        //Add a Just Right vote on the first refresh of the question board after the student joined.
        if (previouslyPressed == null) {
            justRight.setSelected(true);
            previouslyPressed = justRight;
            paceVoteOkay();
        }
        PollRefresh.studentRefresh(quBo, pollVbox, sideMenuPane,this);

        quBo = QuBoInformation.refreshBoardStatus(quBo, boardStatusIcon, boardStatusText, askBtn);
    }

    /**
     * Conditional refresh.
     */
    public void conditionalRefresh(boolean condition) {
        if (condition) {
            refresh();
        }
    }

    public void setRefreshing(Boolean bool) {
        refreshing.set(bool);
    }

    private void startUpProperties() {
        //Hide side menu and sidebar
        LayoutProperties.startupProperties(content, sideBar, sideMenu, pollVbox, ansQuVbox, unAnsQuVbox,
            paceVotePane);
    }

    /**
     * Called by the display question board details button. It either displays the question board details pane,
     * or hides it when it is already open and showing.
     */
    public void displayBoardInfo() {
        if (popUp.isShowing()) {
            popUp.close();
        } else {
            if (popUp != null) {
                new SceneLoader().viewLoader(quBo, popUp, "", "QuBoDetails", null);
            }
        }
    }

    /**
     * Copies the student code to the clipboard when the copy button is clicked.
     */
    public void copyStudentCode() {
        clipboardContent.putString(quBo.getId().toString());
        clipboard.setContent(clipboardContent);
    }

    /**
     * Gets called by the "Too slow" radio button.
     * Calls the paceVoteHandler method with the 'TOO_SLOW' pace type.
     */
    public void paceVoteSlow() {
        paceVoteHandler(PaceType.TOO_SLOW);
    }

    /**
     * Gets called by the "All right" radio button.
     * Calls the paceVoteHandler method with the 'JUST_RIGHT' pace type.
     */
    public void paceVoteOkay() {
        paceVoteHandler(PaceType.JUST_RIGHT);
    }

    /**
     * Gets called by the "Too fast" radio button.
     * Calls the paceVoteHandler method with the 'TOO_FAST' pace type.
     */
    public void paceVoteFast() {
        paceVoteHandler(PaceType.TOO_FAST);
    }

    private void paceVoteHandler(PaceType paceType) {
        // If the question board is closed, block this action and reset the toggle group
        if (QuBoInformation.isQuBoClosed(quBo)) {
            pace.selectToggle(previouslyPressed);
            return;
        }
        // Disables radio button input when processing pace vote by disabling entire VBox
        paceVbox.setDisable(true);
        // Checks whether the user has already made a pace vote. If this is the case we should
        // first remove the old pace vote before creating a new one
        if (paceVoteCreationDto != null) {
            // If deletion fails, reset the radio button to its state before the call to this method and return
            if (!deletePaceVote(true)) {
                pace.selectToggle(previouslyPressed);
                // Allow input again, as processing the pace vote is completed
                paceVbox.setDisable(false);
                return;
            }
        }
        // Add a new pace vote using the input pace type. If addition fails,
        // reset the radio button to its state before the call to this method and return
        if (!addPaceVote(paceType)) {
            pace.selectToggle(previouslyPressed);
        }
        // Allow input again, as processing the pace vote is completed
        paceVbox.setDisable(false);
    }

    /**
     * Gets called by the paceVoteHandler and upon closing the stage, either through the sidebar or by
     * making use of the close button of the stage. It removes a set pace vote.
     *
     * @param dialogOnError  A boolean value that decides whether an error dialog is shown on error.
     * @return          True or false depending on whether the removal was successful.
     */
    public boolean deletePaceVote(boolean dialogOnError) {
        String resBody = PaceVoteCommunication.deletePaceVote(quBo.getId(), paceVoteCreationDto.getId());
        if (resBody == null) {
            if (!dialogOnError) {
                return false;
            }
            AlertDialog.display("Unsuccessful Request",
                "Failed to change your pace vote, please try again.");
            return false;
        }
        paceVoteCreationDto = null;
        return true;
    }

    /**
     * Adds a pace vote to the question board of the student view.
     *
     * @param paceType Either 'too slow', 'just right' or 'too fast'.
     * @return True or false depending on whether the addition was successful.
     */
    private boolean addPaceVote(PaceType paceType) {
        String resBody = PaceVoteCommunication.addPaceVote(quBo.getId(), paceType);
        if (resBody == null) {
            AlertDialog.display("Unsuccessful Request",
                "Failed to add your pace vote, please try again.");
            return false;
        }
        paceVoteCreationDto = gson.fromJson(resBody, PaceVoteCreationDto.class);
        return true;
    }

    /**
     * Displays help documentation.
     */
    public void displayHelpDoc() {
        QuBoDocumentation.display(true);
    }

    /**
     * Add the questions that the user entered to the question board, add the returned question ID
     * to the askedQuestionList, and map the returned secretCode (value) to the question ID (key).
     */
    public void addQuestion() {
        // If the question board is closed, block this action
        if (QuBoInformation.isQuBoClosed(quBo)) {
            return;
        }
        // Display a dialog to extract the user's question text,
        // and ensure it is at least 8 characters long
        String questionText = GetTextDialog.display("Write your question here...",
            "Ask", "Cancel", true);
        // The returned questionText will be null if the user decides to not ask
        if (questionText == null) {
            return;
        }

        String author = authorName == null ? "" : authorName;
        String responseBody = QuestionCommunication.addQuestion(quBo.getId(), questionText, author);
        if (responseBody == null) {
            AlertDialog.display("Unsuccessful Request",
                "Failed to post your question, please try again.");
            return;
        }
        QuestionCreationDto qd = gson.fromJson(responseBody, QuestionCreationDto.class);
        UUID questionId = qd.getId();
        UUID secretCode = qd.getSecretCode();

        // Map the secretCode as the value to the question ID as the key
        secretCodeMap.put(questionId, secretCode);

        //Request automatic upvote
        autoUpvote(questionId);

        refresh();
    }

    /**
     * This method auto-upvotes the question that the user has just asked.
     *
     * @param questionId UUID of the question that was just asked.
     */
    public void autoUpvote(UUID questionId) {
        String response = QuestionVoteCommunication.addQuestionVote(questionId);
        if (response == null) {
            //When the request fails, display alert
            AlertDialog.display("", "Automatic upvote failed.");
        } else {
            //When the request is successful, store the question UUID with the vote UUID in the HashMap
            QuestionVoteDetailsDto vote = gson.fromJson(response, QuestionVoteDetailsDto.class);
            upvoteMap.put(questionId, vote.getId());
        }
    }

    /**
     * Toggles the visibility of the sideBar.
     */
    public void showHideSideBar() {
        if (hamburger.isSelected()) {
            sideMenu.setVisible(sideMenuOpen);
            sideBar.setVisible(true);
        } else {
            sideMenu.setVisible(false);
            sideBar.setVisible(false);
        }
    }

    /**
     * Toggles the visibility of the answered questions menu.
     */
    public void showHideAnsQuestions() {
        sideMenuOpen = sidebarLogic(ansQuestions, polls);
    }

    /**
     * Toggles the visibility of the poll menu.
     */
    public void showHidePolls() {
        sideMenuOpen = sidebarLogic(polls, ansQuestions);
    }

    /**
     * Passes the necessary JavaFX elements to the showHideSelected method in the SideBarControl class.
     * The method in that class handles the showing and hiding of elements in the sideMenu based on the
     * ToggleButton.
     *
     * @param select    The selected ToggleButton.
     * @param deselect  The unselected ToggleButton.
     * @return Boolean of whether or not the sideMenu is still showing.
     */
    public boolean sidebarLogic(ToggleButton select, ToggleButton deselect) {
        return SideBarControl.showHideSelected(select,
            deselect, sideMenu, sideMenuTitle, ansQuVbox, pollVbox, null, null);
    }

    /**
     * This method is called when a radio button of an open poll is pressed. It deletes the current poll vote if
     * there was any.
     */
    public void handlePollChoice(RadioButton optionButton, PollItem poll) {
        //If the question board is closed, show an alert and return.
        if (QuBoInformation.isQuBoClosed(quBo)) {
            return;
        }

        //If there are no previous votes, add the new vote.
        if (optionVote == null) {
            addPollVote(optionButton, poll);

        //If there was a previous vote, handle this vote by deleting it or resetting the variables.
        } else {
            UUID currentOption = poll.findOptionId(selectedOption);

            //If the "current" option was of a past poll, set all values to null and add the new poll vote.
            if (currentOption == null) {
                selectedOption = null;
                optionVote = null;

                addPollVote(optionButton, poll);
                return;
            }

            //Remove the poll vote.
            String response = PollCommunication.removePollVote(quBo.getId(), optionVote);

            //If the request did not fail, attempt to add the new vote.
            if (response != null) {
                boolean failed = addPollVote(optionButton, poll);

                if (!failed) {
                    selectedOption = null;
                    optionVote = null;
                }

            //If the request failed, display an alert and reset the selection.
            } else {
                optionButton.setSelected(false);
                selectedOption.setSelected(true);
                AlertDialog.display("", "The poll vote could not be deleted.\nPlease try again.");
            }
        }
    }

    /**
     * This method handles the addition of a poll vote. If the request to add one fails, it shows an alert and
     * resets the poll option selection.
     *
     * @param optionButton  The selected RadioButton.
     * @param poll          The PollItem that the RadioButton was part of.
     */
    private boolean addPollVote(RadioButton optionButton, PollItem poll) {
        String response = PollCommunication.addPollVote(quBo.getId(), poll.findOptionId(optionButton));

        //If the request did not fail, update the poll variables
        if (response != null) {
            optionVote = gson.fromJson(response, PollVoteDetailsDto.class).getId();
            selectedOption = optionButton;
            return true;
        //If the request did fail, display an alert and reset the selection.
        } else {
            optionButton.setSelected(false);
            selectedOption.setSelected(true);
            AlertDialog.display("", "The poll vote could not be added.\nPlease try again.");
            return false;
        }
    }

    /**
     * Method that runs when the Leave button is clicked.
     * Pops up a confirmation dialogue.
     * If the user clicks yes -> Question board closes and user returns to the JoinQuBo page.
     * If the user clicks no -> Confirmation dialogue closes and user returns to the question board.
     */
    public void leaveQuBo() {
        boolean backHome = ConfirmationDialog.display("Leave Question Board?",
            "You will have to use your code to join again.");
        if (backHome) {
            // If a pace vote is set upon leaving the Question Board, remove the pace vote when the user leaves
            if (paceVoteCreationDto != null) {
                // Deletes set pace vote and do not show an error message on failure
                deletePaceVote(false);
            }
            timer.cancel();
            SceneLoader.defaultLoader((Stage) leaveQuBo.getScene().getWindow(), "JoinQuBo");
        }
    }
}
