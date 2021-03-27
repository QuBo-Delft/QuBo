package nl.tudelft.oopp.demo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.stage.Stage;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    private ListView<Question> questionList;
    @FXML
    private VBox sideBar;
    @FXML
    private VBox sideMenu;
    @FXML
    private Pane paceVotePane;
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
    private boolean sideMenuOpen;

    private String authorName;

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    //HashMap of questionId:upvoteId, needed when deleting vote
    private HashMap<UUID, UUID> upvoteMap = new HashMap<>();
    //HashMap of questionId:secretCode
    private HashMap<UUID, UUID> secretCodeMap = new HashMap<>();

    private QuestionBoardDetailsDto quBo;
    private QuestionDetailsDto[] answeredQuestions;
    private QuestionDetailsDto[] unansweredQuestions;

    /**
     * Method that sets the QuestionBoardDetailsDto of the student view.
     *
     * @param quBo  The QuestionBoardDetailsDto of the question board that the student joined.
     */
    public void setQuBo(QuestionBoardDetailsDto quBo) {
        this.quBo = quBo;
    }

    /**
     * Code that is run upon loading StudentView.fxml
     */
    @FXML
    private void initialize() {
        testQuestions();
        //Display the questions
        displayQuestions();

        //Hide side menu and sidebar
        sideBar.managedProperty().bind(sideBar.visibleProperty());
        sideMenu.managedProperty().bind(sideMenu.visibleProperty());
        sideBar.setVisible(false);
        sideMenu.setVisible(false);
    }

    private void testQuestions() {
        ObservableList<Question> data = FXCollections.observableArrayList();

        data.addAll(new Question(UUID.randomUUID(), 2, "What is life?"),
            new Question(UUID.randomUUID(), 42,"Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."),
            new Question(UUID.randomUUID(), 42,"Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."),
            new Question(UUID.randomUUID(), 42,"Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."));

        questionList.setItems(data);
        questionList.setCellFactory(listView -> new QuestionListCell());

        questionList.setSelectionModel(new NoSelectionModel<>());
        questionList.setFocusModel(new NoFocusModel<>());

        questionList.setEditable(true);
        //Remove border of focus
        questionList.setStyle("-fx-background-insets: 0 ;");
    }

    /**
     * Method that displays the questions that are in the question board on the screen. Answered questions
     * will be sorted by the time at which they were answered, and unanswered questions will be sorted by
     * the number of upvotes they have received.
     */
    private void displayQuestions() {
        //Retrieve the questions and convert them to an array of QuestionDetailsDtos if the response is
        //not null.

        // To be deleted in final version
        if (quBo == null) {
            divideQuestions(null);
            return;
        }
        //

        String jsonQuestions = ServerCommunication.retrieveQuestions(quBo.getId());

        if (jsonQuestions == null) {
            divideQuestions(null);
        } else {
            QuestionDetailsDto[] questions = gson.fromJson(jsonQuestions, QuestionDetailsDto[].class);

            //Divide the questions over two lists and sort them.
            divideQuestions(questions);
            if (unansweredQuestions != null) {
                Sorting.sortOnUpvotes(unansweredQuestions);
            }
            if (answeredQuestions != null) {
                Sorting.sortOnTimeAnswered(answeredQuestions);
            }
        }


        //TODO: Display the questions in the list view by accessing class attributes.
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

    public void displayBoardInfo() {
    }

    public void displayHelpDoc() {
    }

    private static class Question {
        private UUID questionId;
        private int upvoteNumber;
        private String questionContent;

        public UUID getQuestionId() {
            return questionId;
        }

        public int getUpvoteNumber() {
            return upvoteNumber;
        }

        public String getQuestionContent() {
            return questionContent;
        }

        public Question(UUID questionId, int upvoteNumber, String questionContent) {
            this.questionId = questionId;
            this.upvoteNumber = upvoteNumber;
            this.questionContent = questionContent;
        }
    }

    private class QuestionListCell extends ListCell<Question> {
        private GridPane content;
        private UUID questionId;
        private Label upvoteNumber;
        private Text questionContent;

        public QuestionListCell() {
            super();
            upvoteNumber = new Label();
            questionContent = new Text();
            //Bind the managed property to the visible property so that when the node is
            //not visible, it will also not be accounted for in the layout
            questionContent.managedProperty().bind(questionContent.visibleProperty());

            //TODO:Search if questionId exists in upvoteMap and set editable
            //this.setEditable(true);

            this.setPadding(new Insets(0,10,20,0));

            Pane space = new Pane();
            VBox questionVbox = newQuestionVbox(space);
            MenuButton options = newOptionsMenu(questionVbox, space);

            //Create new GridPane and add nodes to it
            content = new GridPane();
            content.addColumn(0, newUpvoteVbox(upvoteNumber));
            content.addColumn(1, questionVbox);
            content.addColumn(2, options);

            content.setPadding(new Insets(6,3,8,3));
            GridPane.setMargin(content, new Insets(0,10,0,0));

            ColumnConstraints col2 = new ColumnConstraints();
            col2.setMaxWidth(GridPane.USE_PREF_SIZE);
            col2.setHgrow(Priority.ALWAYS);
            content.setGridLinesVisible(true);
            content.getColumnConstraints().addAll(new ColumnConstraints(50), col2,
                new ColumnConstraints(50));

            //Make questionContent resize with width of cell
            double paddingWidth = questionList.getPadding().getLeft()
                    +  questionList.getPadding().getRight() + content.getPadding().getLeft()
                    + content.getPadding().getRight() + 130;
            questionContent.wrappingWidthProperty().bind(questionList.widthProperty()
                    .subtract(paddingWidth));

            //Set alignment of children in the GridPane
            GridPane.setValignment(options, VPos.TOP);
            GridPane.setHalignment(options, HPos.RIGHT);
        }

        public VBox newUpvoteVbox(Label upvoteNumber) {
            //Create the Vbox for placing the upvote button and upvote number
            ToggleButton upvoteTriangle = new ToggleButton("up");
            VBox upvote = new VBox(upvoteTriangle, upvoteNumber);
            upvote.setSpacing(5);
            upvote.setAlignment(Pos.TOP_CENTER);

            return upvote;
        }

        public MenuButton newOptionsMenu(VBox questionVbox, Pane space) {
            //Create the edit and delete menu items
            MenuItem edit = new MenuItem("Edit");
            MenuItem delete = new MenuItem("Delete");
            //Create options menu and add the edit and delete menu items
            MenuButton options = new MenuButton();
            options.getItems().addAll(edit, delete);

            //Add action listeners to options menu
            edit.setOnAction(event -> editQuestion(questionContent, questionVbox, options,
                questionId, secretCodeMap.get(questionId), space));
            delete.setOnAction(event -> deleteQuestionOption(content, options, questionId,
                secretCodeMap.get(questionId), space));

            return options;
        }

        public VBox newQuestionVbox(Pane space) {
            int spaceHeight = 20;
            space.setPrefHeight(spaceHeight);
            space.setPrefHeight(spaceHeight);
            space.setPrefHeight(spaceHeight);
            space.managedProperty().bind(space.visibleProperty());
            space.visibleProperty().bind(this.questionContent.visibleProperty());
            VBox questionVbox = new VBox(this.questionContent, space);
            questionVbox.setSpacing(10);

            return questionVbox;
        }

        @Override
        protected void updateItem(Question item, boolean empty) {
            super.updateItem(item, empty);
            //If the item was not null and empty was false, add content to the graphic
            if (item != null && !empty) {
                upvoteNumber.setText(Integer.toString(item.getUpvoteNumber()));
                questionContent.setText(item.getQuestionContent());
                questionId = item.getQuestionId();
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
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

        // TODO: Update the view of questions
    }

    public void editQuestion(Text questionContent, VBox questionVbox, MenuButton options,
                             UUID questionId, UUID code, Pane space) {
        options.setDisable(true);

        TextArea input = new TextArea();
        input.setWrapText(true);
        input.prefWidthProperty().bind(questionContent.wrappingWidthProperty());
        input.minWidthProperty().bind(questionContent.wrappingWidthProperty());
        input.maxWidthProperty().bind(questionContent.wrappingWidthProperty());
        input.setPrefRowCount(5);

        Button cancel = new Button("Cancel");
        Button update = new Button("Update");
        HBox buttons = new HBox(cancel, update);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setSpacing(15);

        update.setOnAction(event -> updateQuestionContent(options, questionId, code, input.getText(),
            questionContent, questionVbox, input, buttons));
        cancel.setOnAction(event -> cancelEdit(options, questionVbox, input, buttons, questionContent));

        questionContent.setVisible(false);
        questionVbox.getChildren().add(input);
        questionVbox.getChildren().add(buttons);
        input.setText(questionContent.getText());
    }

    public void updateQuestionContent(MenuButton options, UUID questionId, UUID code, String text,
                                      Text questionContent, VBox questionVbox, TextArea input,
                                      HBox buttons) {
        String response = ServerCommunication.editQuestion(questionId, code, text);

        if (response == null) {
            AlertDialog.display("", "Question update failed.");
        } else {
            AlertDialog.display("", "Question update successful.");
            questionVbox.getChildren().remove(input);
            questionVbox.getChildren().remove(buttons);
            questionContent.setText(text);
            questionContent.setVisible(true);
            options.setDisable(false);
        }
    }

    public void cancelEdit(MenuButton options, VBox questionVbox, TextArea input,
                            HBox buttons, Text questionContent) {
        options.setDisable(false);
        questionVbox.getChildren().remove(input);
        questionVbox.getChildren().remove(buttons);
        questionContent.setVisible(true);
    }

    public void deleteQuestionOption(GridPane gridpane, MenuButton options, UUID questionId, UUID code,
                                     Pane space) {
        options.setDisable(true);

        Label confirmation = new Label("Are you sure you want to delete this question?");
        confirmation.setPadding(new Insets(0,5,0,0));
        confirmation.setWrapText(true);
        Button yes = new Button("Yes");
        Button cancel = new Button("Cancel");

        HBox dialogue = new HBox(confirmation, yes, cancel);
        dialogue.setPadding(new Insets(5,10,5,10));
        dialogue.setSpacing(15);
        dialogue.setAlignment(Pos.CENTER);

        gridpane.addRow(1, dialogue);
        GridPane.setColumnSpan(dialogue, GridPane.REMAINING);

        //Set action listeners
        yes.setOnAction(event -> deleteQuestion(options, questionId, code));
        cancel.setOnAction(event -> cancelDeletion(options, gridpane, dialogue));
    }

    /**.
     *
     * @param questionId
     * @param code
     */
    public void deleteQuestion(MenuButton options, UUID questionId, UUID code) {
        String response = ServerCommunication.deleteQuestion(questionId, code);

        if (response == null) {
            AlertDialog.display("", "Question deletion failed.");
        } else {
            AlertDialog.display("", "Question deletion successful.");
        }
    }

    public void cancelDeletion(MenuButton options, GridPane gridPane, HBox dialogue) {
        gridPane.getChildren().remove(dialogue);
        options.setDisable(false);
    }

    /**
     * Toggles the visibility of the sideBar.
     */
    public void showHideSideBar() {
        if (hamburger.isSelected()) {
            if (sideMenuOpen) {
                paceVotePane.setVisible(false);
            }
            sideMenu.setVisible(sideMenuOpen);
            sideBar.setVisible(true);
        } else {
            paceVotePane.setVisible(true);
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
            paceVotePane.setVisible(false);
            sideMenuOpen = true;
            showAnsQuestions();
        } else {
            sideMenu.getChildren().clear();
            sideMenu.setVisible(false);
            sideMenuOpen = false;
            paceVotePane.setVisible(true);
        }
    }

    /**
     * Shows the content of the answered questions menu.
     */
    public void showAnsQuestions() {
        Label title = new Label("Answered Questions");
        sideMenu.setVisible(true);
        sideMenu.getChildren().add(title);

        //TODO: Fetch questions and display in a ListView
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
            paceVotePane.setVisible(false);
            sideMenuOpen = true;
            showPolls();
        } else {
            sideMenu.getChildren().clear();
            sideMenu.setVisible(false);
            sideMenuOpen = false;
            paceVotePane.setVisible(true);
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
