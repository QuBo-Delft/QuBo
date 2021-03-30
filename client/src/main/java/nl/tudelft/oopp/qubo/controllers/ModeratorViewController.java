package nl.tudelft.oopp.qubo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.sceneloader.SceneLoader;
import nl.tudelft.oopp.qubo.views.ConfirmationDialog;

import java.util.HashMap;
import java.util.UUID;

public class ModeratorViewController {
    @FXML
    public Button closeBtn;
    @FXML
    public Button leaveBtn;
    @FXML
    public Button exportBtn;
    @FXML
    private HBox topBar;
    @FXML
    public Button boardInfo;
    @FXML
    public Button helpDoc;
    @FXML
    private StackPane content;
    @FXML
    private VBox sideBar;
    @FXML
    private VBox sideMenu;
    @FXML
    private BorderPane paceVotePane;
    @FXML
    private ToggleButton hamburger;
    @FXML
    private ToggleButton ansQuestions;
    @FXML
    private ToggleButton polls;
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
    @FXML
    private StackPane paceBar;

    //Records if the side menu was open before hiding
    private boolean sideMenuOpen;

    @FXML
//    private ListView<> unAnsQuListView;
//    private ListView<> ansQuListView = new ListView<>();

    private String authorName;

    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();

    private QuestionBoardDetailsDto quBo;
    private QuestionDetailsDto[] answeredQuestions = new QuestionDetailsDto[0];
    private QuestionDetailsDto[] unansweredQuestions = new QuestionDetailsDto[0];
}

