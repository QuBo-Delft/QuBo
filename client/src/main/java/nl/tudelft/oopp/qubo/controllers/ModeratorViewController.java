package nl.tudelft.oopp.qubo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.stage.Window;
import nl.tudelft.oopp.qubo.communication.PaceVoteCommunication;
import nl.tudelft.oopp.qubo.controllers.helpers.LayoutProperties;
import nl.tudelft.oopp.qubo.controllers.helpers.QuestionRefresh;
import nl.tudelft.oopp.qubo.controllers.helpers.SideBarControl;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import nl.tudelft.oopp.qubo.views.ConfirmationDialog;
import nl.tudelft.oopp.qubo.dtos.pace.PaceDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;

import javafx.scene.image.ImageView;
import java.util.HashMap;
import java.util.UUID;

public class ModeratorViewController {
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

    //Nodes used to display the pace
    @FXML
    private StackPane paceBar;
    @FXML
    private ImageView paceCursor;

    @FXML
    private Button leaveQuBo;
    @FXML
    private Label boardTitle;
    @FXML
    private ImageView boardStatusIcon;
    @FXML
    private MenuItem studentCodeItem;
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
    //Buttons
    @FXML
    private ToggleButton ansQuestions;
    @FXML
    private ToggleButton polls;
    //Containers
    @FXML
    private Label sideMenuTitle;
    @FXML
    private VBox ansQuVbox;
    @FXML
    private VBox pollVbox;
    @FXML
    private ScrollPane sideMenuPane;

    //Records if the side menu was open before hiding
    private boolean sideMenuOpen;

    private UUID modCode;
    private String authorName;

    //Used for converting JSON responses to their appropriate DTOs.
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    //HashMap of questionId:upvoteId, needed when deleting vote
    private HashMap<UUID, UUID> upvoteMap = new HashMap<>();

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
     * Method that sets the moderator code of the question board.
     *
     * @param modCode   Moderator code associated to the question board.
     */
    public void setModCode(UUID modCode) {
        this.modCode = modCode;
    }

    /**
     * This method is called by the SceneLoader and sets the title text and board open or closed icon.
     * If the board is open, it also displays its start time.
     */
    public void setBoardDetails() {
    }

    /**
     * Code that is run upon loading StudentView.fxml
     */
    @FXML
    private void initialize() {
        startUpProperties();
        //Display the questions
    }

    public void refresh() {
        QuestionRefresh.modRefresh(quBo, modCode, unAnsQuVbox, ansQuVbox, upvoteMap, unAnsQuScPane,
            sideMenuPane);

        //Refresh the pace
        displayPace();
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
     * This method displays the pace of the lecture as perceived by students.
     */
    public void displayPace() {
        //If the question board has not yet been initialised, return
        if (quBo == null) {
            return;
        }

        //Retrieve the pace details from the server
        String jsonPace = PaceVoteCommunication.getAggregatedPaceVotes(quBo.getId(), modCode);
        PaceDetailsDto pace;

        //If the jsonPace was not null, use the server's pace results
        //If the jsonPace was null, set the just right votes to 1 to center the pace cursor
        if (jsonPace != null) {
            pace = gson.fromJson(jsonPace, PaceDetailsDto.class);
        } else {
            pace = new PaceDetailsDto();
            pace.setJustRightVotes(1);
            pace.setTooFastVotes(0);
            pace.setTooSlowVotes(0);
        }

        //Calculate the pace bar modifier
        double paceBarModifier = calculatePace(pace);

        //TODO: Move the bar
    }

    /**
     * This method calculates the average pace. As JavaFX uses a coordinate system with the origin at the
     * top left of the screen, this method assigns a higher value to the tooSlowVotes and a lower value to
     * the tooFastVotes. This ensures that the double returned will transform the cursor appropriately.
     *
     * @param pace  The PaceDetailsDto containing integers of the number of votes per pace type.
     * @return A double between 0 and 1.
     */
    private static double calculatePace(PaceDetailsDto pace) {
        int numberOfVotes = pace.getTooSlowVotes() + pace.getJustRightVotes() + pace.getTooSlowVotes();

        //Set too slow votes equal to 2
        double tooSlow = pace.getTooSlowVotes() * 2;
        //Set just right votes equal to 1.5
        double justRight = pace.getJustRightVotes() * 1.5;
        //Set too fast votes equal to 1
        double tooFast = pace.getTooFastVotes();

        //Calculate the average pace by adding all votes (with relative weights) and dividing it by the total
        //number of votes
        double aggregatedPaceVotes = (tooSlow + justRight + tooFast) / numberOfVotes;

        //Return a double between 0 and 1
        return aggregatedPaceVotes - 1;
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
