package nl.tudelft.oopp.qubo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.communication.QuestionCommunication;
import nl.tudelft.oopp.qubo.communication.QuestionVoteCommunication;
import nl.tudelft.oopp.qubo.controllers.helpers.LayoutProperties;
import nl.tudelft.oopp.qubo.controllers.helpers.QuestionRefresh;
import nl.tudelft.oopp.qubo.controllers.helpers.SideBarControl;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionCreationDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import nl.tudelft.oopp.qubo.views.AlertDialog;
import nl.tudelft.oopp.qubo.views.ConfirmationDialog;
import nl.tudelft.oopp.qubo.communication.ServerCommunication;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.views.GetTextDialog;

import javafx.scene.image.ImageView;
import java.util.HashMap;
import java.util.UUID;

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


    //Records if the side menu was open before hiding
    private boolean sideMenuOpen;

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
     * This method is called by the SceneLoader and sets the title text and board open or closed icon.
     * If the board is open, it also displays its start time.
     */
    public void setBoardDetails() {
        boardTitle.setText(quBo.getTitle());
        if (quBo.isClosed()) {
            boardStatusText.setText("Question board is closed, making changes is no longer possible ");
            boardStatusIcon.setImage(new Image(getClass().getResource(
                "/images/qubo/status_closed.png").toString()));
        } else {
            boardStatusText.setText("board open since " + quBo.getStartTime().toString());
        }
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

    public void refresh() {
        QuestionRefresh.studentRefresh(quBo, unAnsQuVbox, ansQuVbox, upvoteMap, secretCodeMap, unAnsQuScPane,
            sideMenuPane);
    }

    private void startUpProperties() {
        //Hide side menu and sidebar
        LayoutProperties.startupProperties(content, sideBar, sideMenu, pollVbox, ansQuVbox, unAnsQuVbox,
            paceVotePane);
    }

    public void displayBoardInfo() {
    }

    public void copyStudentCode() {
        clipboardContent.putString(quBo.getId().toString());
        clipboard.setContent(clipboardContent);
    }

    public void displayHelpDoc() {
    }

    /**
     *  Add the questions that the user entered to the question board, add the returned question ID
     *  to the askedQuestionList, and map the returned secretCode (value) to the question ID (key).
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
            SceneLoader.defaultLoader((Stage) leaveQuBo.getScene().getWindow(), "JoinQuBo");
        }
    }
}
