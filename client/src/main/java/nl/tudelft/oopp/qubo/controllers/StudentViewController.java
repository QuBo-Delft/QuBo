package nl.tudelft.oopp.qubo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.communication.PaceVoteCommunication;
import nl.tudelft.oopp.qubo.communication.QuestionCommunication;
import nl.tudelft.oopp.qubo.communication.QuestionVoteCommunication;
import nl.tudelft.oopp.qubo.controllers.helpers.LayoutProperties;
import nl.tudelft.oopp.qubo.controllers.helpers.QuBoInformation;
import nl.tudelft.oopp.qubo.controllers.helpers.QuestionRefresh;
import nl.tudelft.oopp.qubo.controllers.helpers.SideBarControl;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import nl.tudelft.oopp.qubo.views.AlertDialog;
import nl.tudelft.oopp.qubo.views.ConfirmationDialog;
import nl.tudelft.oopp.qubo.views.GetTextDialog;

import java.util.HashMap;
import java.util.UUID;

/**
 * Controller for the StudentView.fxml sheet
 */
public class StudentViewController {
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

    // Pace votes
    @FXML
    private RadioButton paceVoteFast;
    @FXML
    private RadioButton paceVoteOkay;
    @FXML
    private RadioButton paceVoteSlow;


    //Records if the side menu was open before hiding
    private boolean sideMenuOpen;
    // Stage to be shown when the QuBo details button is clicked
    Stage popUp = new Stage();
    // Dto set upon creation of a pace vote
    PaceVoteCreationDto paceVoteCreationDto;

    private String authorName;

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    //HashMap of questionId:upvoteId, needed when deleting vote
    private HashMap<UUID, UUID> upvoteMap = new HashMap<>();
    //HashMap of questionId:secretCode, needed when editing and deleting questions
    private HashMap<UUID, UUID> secretCodeMap = new HashMap<>();

    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();

    private QuestionBoardDetailsDto quBo;

    /**
     * Method that sets the QuestionBoardDetailsDto of the student view.
     *
     * @param quBo  The QuestionBoardDetailsDto of the question board that the student joined.
     */
    public void setQuBo(QuestionBoardDetailsDto quBo) {
        this.quBo = quBo;
    }

    /**
     * Method that sets the username of the application user.
     *
     * @param authorName    The name of the student that joined the question board.
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
        new QuBoInformation().setBoardDetails(quBo, boardStatusIcon, boardStatusText, boardTitle);
    }

    /**
     * Code that is run upon loading StudentView.fxml
     */
    @FXML
    private void initialize() {
        startUpProperties();
        //Display the questions
        refresh();
    }

    /**
     * Passes the data necessary for refreshing the view to the QuestionRefresh class.
     */
    public void refresh() {
        QuestionRefresh.studentRefresh(quBo, unAnsQuVbox, ansQuVbox, upvoteMap, secretCodeMap, unAnsQuScPane,
            sideMenuPane);
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
     * Calls the paceVoteHandler method with the 'JUST_RIGHT' pace type.Pace vote okay.
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
        // Disable pace vote input to prevent double pace vote placement during method execution.
        // The radio buttons are bound together, so changing the disable property of one changes all of them
        paceVoteOkay.disableProperty().bindBidirectional(paceVoteFast.disableProperty());
        paceVoteFast.disableProperty().bindBidirectional(paceVoteSlow.disableProperty());
        paceVoteOkay.setDisable(true);
        // Checks whether the user has already made a pace vote. If this is the case we should
        // first remove the old pace vote before creating a new one
        if (paceVoteCreationDto != null) {
            // If deletion fails, we stop code execution using the return statement
            if (deletePaceVote()) {
                paceVoteOkay.setDisable(false);
                return;
            }
        }
        // Add a new pace vote using the input pace type
        addPaceVote(paceType);
        paceVoteOkay.setDisable(false);
    }

    private boolean deletePaceVote() {
        String resBody = PaceVoteCommunication.deletePaceVote(quBo.getId(), paceVoteCreationDto.getId());
        if (resBody == null) {
            AlertDialog.display("Unsuccessful Request",
                "Failed to change your pace vote, please try again.");
            // Return true if deletion fails
            return true;
        }
        // Return false if deletion succeeds
        return false;
    }

    private void addPaceVote(PaceType paceType) {
        String resBody = PaceVoteCommunication.addPaceVote(quBo.getId(), paceType);
        if (resBody == null) {
            AlertDialog.display("Unsuccessful Request",
                "Failed to add your pace vote, please try again.");
            return;
        }
        paceVoteCreationDto = gson.fromJson(resBody, PaceVoteCreationDto.class);
    }

    /**
     * Displays help documentation.
     */
    public void displayHelpDoc() {
    }

    /**
     * Add the questions that the user entered to the question board, add the returned question ID
     * to the askedQuestionList, and map the returned secretCode (value) to the question ID (key).
     */
    public void addQuestion() {
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
     * @param questionId    UUID of the question that was just asked.
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
     * @param select    The selected ToggleButton
     * @param deselect  The unselected ToggleButton
     * @return Boolean of whether or not the sideMenu is still showing
     */
    public boolean sidebarLogic(ToggleButton select, ToggleButton deselect) {
        return SideBarControl.showHideSelected(select, deselect, sideMenu, sideMenuTitle, ansQuVbox, pollVbox);
    }

    /**
     * Method that runs when the Leave button is clicked.
     * Pops up a confirmation dialogue.
     * If the user clicks yes -> Question board closes and user returns to the JoinQuBo page
     * If the user clicks no -> Confirmation dialogue closes and user returns to the question board
     */
    public void leaveQuBo() {
        boolean backHome = ConfirmationDialog.display("Leave Question Board?",
            "You will have to use your code to join again.");
        if (backHome) {
            // If a pace vote is set upon leaving the Question Board, remove the pace vote when the user leaves
            if (paceVoteCreationDto != null) {
                deletePaceVote();
            }
            SceneLoader.defaultLoader((Stage) leaveQuBo.getScene().getWindow(), "JoinQuBo");
        }
    }
}
