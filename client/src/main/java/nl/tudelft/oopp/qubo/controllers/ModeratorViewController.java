package nl.tudelft.oopp.qubo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.communication.PollCommunication;
import nl.tudelft.oopp.qubo.communication.QuestionBoardCommunication;
import nl.tudelft.oopp.qubo.controllers.helpers.LayoutProperties;
import nl.tudelft.oopp.qubo.controllers.helpers.PollRefresh;
import nl.tudelft.oopp.qubo.controllers.helpers.SideBarControl;
import nl.tudelft.oopp.qubo.controllers.helpers.PaceDisplay;
import nl.tudelft.oopp.qubo.controllers.helpers.QuBoInformation;
import nl.tudelft.oopp.qubo.controllers.helpers.QuestionRefresh;
import nl.tudelft.oopp.qubo.dtos.poll.PollDetailsDto;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import nl.tudelft.oopp.qubo.utilities.QuestionToStringConverter;
import nl.tudelft.oopp.qubo.views.AlertDialog;
import nl.tudelft.oopp.qubo.views.ConfirmationDialog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.TimerTask;
import java.util.UUID;
import nl.tudelft.oopp.qubo.views.QuBoDocumentation;

import java.util.HashMap;
import java.util.Timer;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    private MenuItem moderatorCodeItem;
    @FXML
    private MenuItem studentCodeItemBtn;
    @FXML
    private MenuItem moderatorCodeItemBtn;
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
    private Button createPollBtn;
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
    // Poll creation
    @FXML
    private VBox createPollVbox;
    @FXML
    private TextField optionA;
    @FXML
    private TextField optionB;
    @FXML
    private Label pollCreateNewLabel;
    @FXML
    private HBox pollVoteBtn;
    @FXML
    private HBox pollVoteHbox;
    @FXML
    private TextField createPollTitle;
    @FXML
    private Button createPollNewOptionBtn;
    @FXML
    private Button createPollCancelBtn;
    @FXML
    private Button createPollCreateBtn;

    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    @FXML
    public Button export;
    @FXML
    private Button closeQuBo;

    /**
     * Records if the side menu was open before hiding.
     */
    private boolean sideMenuOpen;
    /**
     * The number of options displayed when creating a new poll, 2 by default.
     */
    int optionCounter = 1;

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
    private TimerTask refreshPace = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(() -> conditionalPaceRefresh(refreshing.get()));
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
        // Binds the visible and managed property of the create poll VBox together.
        createPollVbox.visibleProperty().bindBidirectional(createPollVbox.managedProperty());
        createPollVbox.setVisible(false);
        startUpProperties();
        //Display the questions and pace
        timer.scheduleAtFixedRate(refreshQuestions, 0, 2000);
        timer.scheduleAtFixedRate(refreshPace, 0, 4000);

    }

    /**
     * This method refreshes the questions.
     */
    public void refresh() {
        QuestionRefresh.modRefresh(this, quBo, modCode, unAnsQuVbox, ansQuVbox, upvoteMap, unAnsQuScPane,
            sideMenuPane);

        quBo = QuBoInformation.refreshBoardStatus(quBo, boardStatusIcon, boardStatusText, closeQuBo);

        //Refresh the list of polls.
        PollRefresh.modRefresh(quBo, pollVbox, sideMenuPane, this);
    }


    /**
     * This method refreshes the pace bar.
     */
    public void refreshPace() {
        //Refresh the pace
        PaceDisplay.displayPace(quBo, modCode, paceBar, paceCursor);
    }

    /**
     * Conditional refresh.
     */
    public void conditionalRefresh(boolean condition) {
        if (condition) {
            refresh();
        }
    }

    /**
     * Conditional refresh.
     */
    public void conditionalPaceRefresh(boolean condition) {
        if (condition) {
            refreshPace();
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
     * Copy moderator code.
     */
    public void copyModeratorCode() {
        clipboardContent.putString(modCode.toString());
        clipboard.setContent(clipboardContent);
    }

    /**
     * Display help doc.
     */
    public void displayHelpDoc() {
        QuBoDocumentation.display(true);
    }

    /**
     * Gets called by the create poll button.
     */
    public void createPoll() {
        if (PollCommunication.retrievePollDetails(quBo.getId()) != null) {
            AlertDialog.display("Poll creation failed",
                "Please close the current poll, before creating a new one.");
            return;
        }
        // Shows the create poll VBox, or hides it when it was already showing
        createPollVbox.setVisible(!createPollVbox.isVisible());
    }

    /**
     * Gets called by the poll option delete button and will delete the row of the option completely.
     *
     * @param optionButton The option button that is calling this method.
     */
    public void createPollOptionDelete(Button optionButton) {
        ((VBox) optionButton.getParent().getParent()).getChildren().remove(optionButton.getParent());
        int newLabelNumber = 0;
        for (Node node : createPollVbox.getChildren()) {
            if (node instanceof HBox) {
                HBox optionBox = (HBox) node;
                // If a HBox is found, iterate through all its children as well.
                for (Node nodeChild : optionBox.getChildren()) {
                    if (nodeChild instanceof Label) {
                        // If it has found a label, correct the text
                        newLabelNumber++;
                        if (nodeChild.getId().equals(pollCreateNewLabel.getId())) {
                            return;
                        }
                        ((Label) nodeChild).setText(newLabelNumber + ":");
                    }
                }
            }
        }
    }

    /**
     * This method gets called by the create poll button.
     */
    public void createPollNewOption() {
        // Gets the number of currently displayed options by getting all children
        // and by substracting the children that contain buttons and labels, non-option items
        int veBoxChildren = createPollVbox.getChildren().size() - 2;
        // Assign that number to the label
        Label optionLabel = new Label(veBoxChildren + ":");

        // Set a new text field for option text input.
        TextField optionField = new TextField();
        HBox.setHgrow(optionField, Priority.ALWAYS);

        // Set a delete button for this option, as the minimum is two this option is not required.
        Button optionButton = new Button("Delete");
        optionButton.setOnAction(event -> createPollOptionDelete(optionButton));

        // Create a new HBox.
        HBox newOptionContainer = new HBox();
        // Assign the above elements to such HBox.
        newOptionContainer.getChildren().addAll(optionLabel, optionField, optionButton);
        newOptionContainer.setSpacing(10);
        newOptionContainer.setAlignment(Pos.CENTER);

        // Add the above HBox to the VBox.
        createPollVbox.getChildren().add(veBoxChildren, newOptionContainer);

    }

    /**
     * Gets called by the cancel button.
     */
    public void createPollCancel() {
        createPollVbox.setVisible(false);
        int toBeRemoved = 1;
        for (Node node : createPollVbox.getChildren()) {
            if (node instanceof HBox) {
                toBeRemoved++;
                HBox heBox = (HBox) node;
                if (heBox.getId().equals(pollVoteBtn.getId()) || heBox.getId().equals(pollVoteHbox.getId())) {
                    return;
                }
                createPollVbox.getChildren().remove(toBeRemoved);
            }
        }
    }

    /**
     * Gets called when the create poll button is clicked. Creates a new poll item.
     */
    public void createPollCreate() {
        Set<String> stringSet = new HashSet<>();

        // Iterate to all children of the VBox.
        for (Node node : createPollVbox.getChildren()) {
            if (node instanceof HBox) {
                HBox optionBox = (HBox) node;
                // If a HBox is found, iterate through all its children as well.
                for (Node nodeChild : optionBox.getChildren()) {
                    if (nodeChild instanceof TextField) {
                        // If it has found a text field that is not empty, add it to the board options set.
                        if (((TextField) nodeChild).getText().equals("")) {
                            AlertDialog.display("Empty poll option",
                                "An empty poll option was found, please fill in every option.");
                            return;
                        }
                        stringSet.add(((TextField) nodeChild).getText());
                    }
                }
            }
        }
        // Check that the poll title is not empty.
        if (createPollTitle.getText().equals("")) {
            AlertDialog.display("Empty poll title",
                "The poll title was found to be empty, please fill in every option.");
            return;
        }
        // Check that no duplicates have been added, as this will not work for a set.
        if (stringSet.size() < 2) {
            AlertDialog.display("Duplicate poll options",
                "The poll contained duplicate options which is not allowed. Please change them.");
            return;
        }
        createPollVbox.setVisible(false);
        // Creates a new poll based on the input title and the added text fields.
        PollCommunication.addPoll(quBo.getId(), modCode, createPollTitle.getText(), stringSet);
        createPollCancel();
        createPollTitle.setText("");
        optionA.setText("");
        optionB.setText("");
    }

    /**
     * This method gets the title and options of the currently closed poll and re-opens a new poll
     * with that information. By re-opening a poll, it deletes the old one and creates a new one.
     */
    public void redoPoll() {
        boolean confirmed = ConfirmationDialog.display("Redo Poll",
            "Would you like to redo this poll? This will delete the results and re-open it.");

        if (confirmed) {
            String resBody = PollCommunication.retrievePollDetails(quBo.getId());

            if (resBody == null) {
                AlertDialog.display("Unsuccessful request", "Please try again.");
                return;
            }
            PollDetailsDto pollDetailsDto = gson.fromJson(resBody, PollDetailsDto.class);

            List<PollOptionDetailsDto> optionList = new ArrayList<>(pollDetailsDto.getOptions());
            Set<String> stringSet = new HashSet<>();

            for (PollOptionDetailsDto pollOptionsDto : optionList) {
                stringSet.add(pollOptionsDto.getOptionText());
            }
            deletePoll(false);

            PollCommunication.addPoll(quBo.getId(), modCode, pollDetailsDto.getText(), stringSet);
        }
    }

    /**
     * This method deletes the poll that is currently in the poll.
     * @param showMessage   If this boolean is true, the successful deletion method will be shown.
     *                      This is set to false for reopening polls.
     */
    public void deletePoll(boolean showMessage) {
        String pollDelete = PollCommunication.deletePoll(quBo.getId(), modCode);

        if (pollDelete == null) {
            AlertDialog.display("Failed to delete the poll", "Please try again later.");
            return;
        }

        if (showMessage) {
            AlertDialog.display("Success", "The poll was deleted successfully");
        }
    }

    /**
     * This method closes the poll that is currently in the poll.
     */
    public void closePoll() {
        String pollClose = PollCommunication.closePoll(quBo.getId(), modCode);

        if (pollClose == null) {
            AlertDialog.display("Failed to close the poll", "Please try again later.");
            return;
        }

        AlertDialog.display("Success", "The poll was closed successfully");
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
        createPollBtn.setVisible(false);
        sideMenuOpen = sidebarLogic(ansQuestions, polls);
    }

    /**
     * Toggles the visibility of the poll menu.
     */
    public void showHidePolls() {
        createPollBtn.setVisible(false);
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
        return SideBarControl.showHideSelected(select,
            deselect, sideMenu, sideMenuTitle, ansQuVbox, pollVbox, createPollBtn, createPollVbox);
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
