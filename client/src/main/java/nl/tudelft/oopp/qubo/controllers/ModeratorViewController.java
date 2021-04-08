package nl.tudelft.oopp.qubo.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.communication.QuestionBoardCommunication;
import nl.tudelft.oopp.qubo.controllers.helpers.LayoutProperties;
import nl.tudelft.oopp.qubo.controllers.helpers.PaceDisplay;
import nl.tudelft.oopp.qubo.controllers.helpers.QuBoInformation;
import nl.tudelft.oopp.qubo.controllers.helpers.QuestionRefresh;
import nl.tudelft.oopp.qubo.controllers.helpers.SideBarControl;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import nl.tudelft.oopp.qubo.utilities.QuestionToStringConverter;
import nl.tudelft.oopp.qubo.views.AlertDialog;
import nl.tudelft.oopp.qubo.views.ConfirmationDialog;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;

import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

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

    @FXML
    public Button export;
    @FXML
    private Button closeQuBo;

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
        QuBoInformation.setBoardDetails(quBo, boardStatusIcon, boardStatusText, boardTitle, closeQuBo);
    }

    /**
     * Code that is run upon loading StudentView.fxml
     */
    @FXML
    private void initialize() {
        startUpProperties();
        //Display the questions and pace
        timer.scheduleAtFixedRate(refreshQuestions, 0, 2000);
    }

    /**
     * This method refreshes the questions and pace bar.
     */
    public void refresh() {
        QuestionRefresh.modRefresh(this, quBo, modCode, unAnsQuVbox, ansQuVbox, upvoteMap, unAnsQuScPane,
            sideMenuPane);

        //Refresh the pace
        PaceDisplay.displayPace(quBo, modCode, paceBar, paceCursor);

        quBo = QuBoInformation.refreshBoardStatus(quBo, boardStatusIcon, boardStatusText, closeQuBo);
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
            null);
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
            timer.cancel();
            //Reset the pace bar modifier.
            PaceDisplay.resetPaceBarMod();
            SceneLoader.defaultLoader((Stage) leaveQuBo.getScene().getWindow(), "JoinQuBo");
        }
    }

    /**
     * Method that runs when the Export button is clicked.
     * Shows a file picker dialogue.
     * If the user selects a destination file -> Questions are exported to that file.
     * If the user clicks cancel -> Dialogue closes and user returns to the question board.
     */
    public void exportQuestions() {
        String jsonQuestions = QuestionBoardCommunication.retrieveQuestions(quBo.getId());

        if (jsonQuestions == null) {
            AlertDialog.display("Unsuccessful Request",
                "Failed to retrieve questions, please try again.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export questions");
        fileChooser.setInitialFileName(quBo.getTitle() + ".txt");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text file (*.txt)", "*.txt"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showSaveDialog(boardTitle.getScene().getWindow());

        if (file == null) {
            return;
        }

        Gson gson = (new GsonBuilder()).create();

        QuestionDetailsDto[] questions = gson.fromJson(jsonQuestions, QuestionDetailsDto[].class);

        String asString = QuestionToStringConverter.convertToString(questions);

        try {
            Files.write(file.toPath(), asString.getBytes());
        } catch (IOException e) {
            AlertDialog.display("Error",
                "Failed to save questions to file, please try again.");
        }
    }

    /**
     * Method that runs when the closeQuBo button is clicked.
     * Pops up a confirmation dialogue.
     * If the user clicks yes -> Question board will be closed.
     * If the user clicks no -> Confirmation dialogue closes and user returns to the question board.
     * The user will be informed whether the question board has been closed successfully on the server-side.
     */
    public void closeQuBo() {
        boolean closeConfirmed = ConfirmationDialog.display("Close Question Board?",
                "This question board will be closed.");

        // The user confirmed to close the question board
        if (closeConfirmed) {
            String questionBoardDetailsDto = QuestionBoardCommunication
                    .closeBoardRequest(quBo.getId(), modCode);

            if (questionBoardDetailsDto == null) {
                // Null returned, the question board was not closed
                AlertDialog.display("Unsuccessful Request",
                        "Failed to close the question board, please try again.");
            } else {
                AlertDialog.display("Successful Request",
                        "The question board has been closed.");
            }

        }

    }
}
