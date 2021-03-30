package nl.tudelft.oopp.demo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.controllers.helpers.Question;
import nl.tudelft.oopp.demo.controllers.helpers.QuestionListCell;
import nl.tudelft.oopp.demo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.demo.dtos.questionvote.QuestionVoteDetailsDto;
import nl.tudelft.oopp.demo.controllers.helpers.NoFocusModel;
import nl.tudelft.oopp.demo.controllers.helpers.NoSelectionModel;
import nl.tudelft.oopp.demo.dtos.question.QuestionCreationDto;
import nl.tudelft.oopp.demo.sceneloader.SceneLoader;
import nl.tudelft.oopp.demo.views.AlertDialog;
import nl.tudelft.oopp.demo.views.ConfirmationDialog;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.demo.utilities.sorting.Sorting;
import nl.tudelft.oopp.demo.views.GetTextDialog;

import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StudentViewController {
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
    public Button askQuestion;
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

    //Records if the side menu was open before hiding
    private boolean sideMenuOpen;

    @FXML
    private ListView<Question> unAnsQuListView;
    private ListView<Question> ansQuListView = new ListView<>();

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
    private QuestionDetailsDto[] answeredQuestions = new QuestionDetailsDto[0];
    private QuestionDetailsDto[] unansweredQuestions = new QuestionDetailsDto[0];

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
            boardStatusIcon.setImage(new Image(getClass().getResource("/icons/status_closed.png").toString()));
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
        displayQuestions();
    }

    private void startUpProperties() {
        //Hide side menu and sidebar
        sideBar.managedProperty().bind(sideBar.visibleProperty());
        sideMenu.managedProperty().bind(sideMenu.visibleProperty());
        sideBar.setVisible(false);
        sideMenu.setVisible(false);

        sideMenu.prefWidthProperty().bind(content.widthProperty().multiply(0.45));
        paceVotePane.visibleProperty().bind(sideMenu.visibleProperty().not());

        //Make ListCells unable to be selected individually (remove blue highlighting)
        unAnsQuListView.setSelectionModel(new NoSelectionModel<>());
        unAnsQuListView.setFocusModel(new NoFocusModel<>());
        ansQuListView.setSelectionModel(new NoSelectionModel<>());
        ansQuListView.setFocusModel(new NoFocusModel<>());

        //Remove border of focus
        unAnsQuListView.setStyle("-fx-background-insets: 0 ;");
        ansQuListView.setStyle("-fx-background-insets: 0 ;");

        unAnsQuListView.setEditable(true);
    }

    /**
     * Method that displays the questions that are in the question board on the screen. Answered questions
     * will be sorted by the time at which they were answered, and unanswered questions will be sorted by
     * the number of upvotes they have received.
     */
    private void displayQuestions() {
        // To be deleted in final version
        if (quBo == null) {
            divideQuestions(null);
            return;
        }
        //

        //Retrieve the questions and convert them to an array of QuestionDetailsDtos if the response is
        //not null.
        String jsonQuestions = ServerCommunication.retrieveQuestions(quBo.getId());

        if (jsonQuestions == null) {
            divideQuestions(null);
        } else {
            QuestionDetailsDto[] questions = gson.fromJson(jsonQuestions, QuestionDetailsDto[].class);

            //Divide the questions over two lists and sort them.
            divideQuestions(questions);
            if (unansweredQuestions.length == 0) {
                unAnsQuListView.getItems().clear();
            } else {
                Sorting.sortOnUpvotes(unansweredQuestions);
                mapQuestions(unAnsQuListView, unansweredQuestions);
            }

            if (answeredQuestions.length == 0) {
                ansQuListView.getItems().clear();
            } else {
                Sorting.sortOnTimeAnswered(answeredQuestions);
                mapQuestions(ansQuListView, answeredQuestions);
            }
        }
    }

    /**
     * This method will be used to divide the question list into a list of answered questions,
     * and a list of unanswered questions.
     *
     * @param questions The question array that needs to be divided.
     */
    private void divideQuestions(QuestionDetailsDto[] questions) {
        //If there are no questions, initialise the questions lists with empty arrays and return.
        if (questions == null || questions.length == 0) {
            answeredQuestions = new QuestionDetailsDto[0];
            unansweredQuestions = new QuestionDetailsDto[0];
            return;
        }

        //Initialise two lists to contain the answered and unanswered questions.
        List<QuestionDetailsDto> answered = new ArrayList<>();
        List<QuestionDetailsDto> unanswered = new ArrayList<>();

        //Divide the questions over the two lists.
        for (QuestionDetailsDto question : questions) {
            if (question.getAnswered() != null) {
                answered.add(question);
            } else {
                unanswered.add(question);
            }
        }

        //Convert the list of answered and unanswered questions to arrays and store them in their
        //respective class attributes.
        answeredQuestions = answered.toArray(new QuestionDetailsDto[0]);
        unansweredQuestions = unanswered.toArray(new QuestionDetailsDto[0]);
    }

    private void mapQuestions(ListView<Question> questionListView, QuestionDetailsDto[] questionList) {
        ObservableList<Question> data = FXCollections.observableArrayList();

        //For each question in the list create a new Question object
        for (QuestionDetailsDto question : questionList) {
            Question newQu = new Question(question.getId(), question.getUpvotes(),
                question.getText(), question.getAuthorName(), null);

            //Get Answers if there are any
            if (question.getAnswers().size() != 0) {
                List<String> answers = new ArrayList<>();
                for (AnswerDetailsDto answer : question.getAnswers()) {
                    answers.add(answer.getText());
                }
                newQu.setAnswers(answers);
            }
            //Add the question to the ObservableList
            data.add(newQu);
        }

        questionListView.getItems().clear();
        //Set new questions in the ListView
        questionListView.setItems(data);
        //Set the custom cell factory for the listview
        questionListView.setCellFactory(listView
            -> new QuestionListCell(questionListView, secretCodeMap, upvoteMap));
    }

    //Temporary refresh button
    public void displayBoardInfo() {
        displayQuestions();
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
        String responseBody = ServerCommunication.addQuestion(quBo.getId(), questionText, author);
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

        displayQuestions();
    }

    /**
     * This method auto-upvotes the question that the user has just asked.
     *
     * @param questionId    UUID of the question that was just asked.
     */
    public void autoUpvote(UUID questionId) {
        String response = ServerCommunication.addQuestionVote(questionId);
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
        if (sideMenu.isVisible() && polls.isSelected()) {
            polls.setSelected(false);
            sideMenu.getChildren().clear();
            showAnsQuestions();
        } else if (!sideMenu.isVisible()) {
            sideMenuOpen = true;
            showAnsQuestions();
        } else {
            sideMenu.getChildren().clear();
            sideMenu.setVisible(false);
            sideMenuOpen = false;
        }
    }

    /**
     * Shows the content of the answered questions menu.
     */
    public void showAnsQuestions() {
        Label title = new Label("Answered Questions");
        sideMenu.setVisible(true);
        sideMenu.getChildren().add(title);
        sideMenu.getChildren().add(ansQuListView);
        VBox.setVgrow(ansQuListView, Priority.ALWAYS);
    }

    /**
     * Toggles the visibility of the poll menu.
     */
    public void showHidePolls() {
        if (sideMenu.isVisible() && ansQuestions.isSelected()) {
            ansQuestions.setSelected(false);
            sideMenu.getChildren().clear();
            showPolls();
        } else if (!sideMenu.isVisible()) {
            sideMenuOpen = true;
            showPolls();
        } else {
            sideMenu.getChildren().clear();
            sideMenu.setVisible(false);
            sideMenuOpen = false;
        }
    }

    /**
     * Shows the content of the poll menu.
     */
    public void showPolls() {
        Label title = new Label("Polls");
        sideMenu.setVisible(true);
        sideMenu.getChildren().add(title);

        //TODO: Fetch polls and display in a ListView
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
            SceneLoader.backToHome((Stage) leaveQuBo.getScene().getWindow());
        }
    }
}
