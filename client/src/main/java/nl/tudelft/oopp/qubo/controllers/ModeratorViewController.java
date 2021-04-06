package nl.tudelft.oopp.qubo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.controllers.helpers.LayoutProperties;
import nl.tudelft.oopp.qubo.controllers.helpers.PaceDisplay;
import nl.tudelft.oopp.qubo.controllers.helpers.PollRefresh;
import nl.tudelft.oopp.qubo.controllers.helpers.QuBoInformation;
import nl.tudelft.oopp.qubo.controllers.helpers.QuestionRefresh;
import nl.tudelft.oopp.qubo.controllers.helpers.SideBarControl;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import nl.tudelft.oopp.qubo.views.ConfirmationDialog;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;

import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.UUID;

/**
 * The Moderator view controller.
 */
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

    /**
    * Records if the side menu was open before hiding.
    */
    private boolean sideMenuOpen;

    /**
     * Stage to be shown when the QuBo details button is clicked.
     */
    Stage popUp = new Stage();

    private UUID modCode;
    private String authorName;

    /**
    * HashMap of questionId:upvoteId, needed when deleting vote.
    */
    private HashMap<UUID, UUID> upvoteMap = new HashMap<>();

    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();

    private QuestionBoardDetailsDto quBo;

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
     * Method that sets the moderator code of the question board.
     *
     * @param modCode Moderator code associated to the question board.
     */
    public void setModCode(UUID modCode) {
        this.modCode = modCode;
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
        //Display the questions and pace
        refresh();
    }

    /**
     * This method refreshes the questions and pace bar.
     */
    public void refresh() {
        //Refresh the question list.
        QuestionRefresh.modRefresh(quBo, modCode, unAnsQuVbox, ansQuVbox, upvoteMap, unAnsQuScPane,
            sideMenuPane);

        //Refresh the pace.
        PaceDisplay.displayPace(quBo, modCode, paceBar, paceCursor);

        //Refresh the list of polls.
        PollRefresh.modRefresh(quBo, pollVbox, sideMenuPane, this);
    }

    private void startUpProperties() {
        //Hide side menu and sidebar
        LayoutProperties.startupProperties(content, sideBar, sideMenu, pollVbox, ansQuVbox, unAnsQuVbox,
            paceVotePane);
        LayoutProperties.modStartUpProperties(paceBar, paceCursor);
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
                new SceneLoader().viewLoader(quBo, popUp, "", "QuBoDetails", modCode);
            }
        }
    }

    /**
     * Copy student code.
     */
    public void copyStudentCode() {
        clipboardContent.putString(quBo.getId().toString());
        clipboard.setContent(clipboardContent);
    }

    /**
     * Display help doc.
     */
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

    /**
     * Sidebar logic.
     *
     * @param select   The select button.
     * @param deselect The deselect button.
     * @return result
     */
    public boolean sidebarLogic(ToggleButton select, ToggleButton deselect) {
        return SideBarControl.showHideSelected(select, deselect, sideMenu, sideMenuTitle, ansQuVbox, pollVbox);
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
            SceneLoader.defaultLoader((Stage) leaveQuBo.getScene().getWindow(), "JoinQuBo");
        }
    }
}
