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
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.sceneloader.SceneLoader;
import nl.tudelft.oopp.demo.views.AlertDialog;
import nl.tudelft.oopp.demo.views.ConfirmationDialog;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.demo.utilities.sorting.Sorting;

import java.util.ArrayList;
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

    private HashSet<UUID> upvoteList = new HashSet<>();
    private HashSet<UUID> askedQuestionList = new HashSet<>();

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
        data.addAll(new Question(2, "What is life?"),
            new Question(42,"Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."));

        questionList.setItems(data);
        questionList.setCellFactory(listView -> new QuestionListCell());
    }

    /**
     * Method that displays the questions that are in the question board on the screen. Answered questions
     * will be sorted by the time at which they were answered, and unanswered questions will be sorted by
     * the number of upvotes they have received.
     */
    private void displayQuestions() {
        //Retrieve the questions and convert them to an array of QuestionDetailsDtos if the response is
        //not null.

        //Uncomment this part when you need to test the student view individually
        //To be deleted in final version

        if (quBo == null) {
            divideQuestions(null);
            return;
        }

        String jsonQuestions = ServerCommunication.retrieveQuestions(quBo.getId());

        if (jsonQuestions == null) {
            divideQuestions(null);
        } else {
            Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                .create();
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
        private UUID code;
        private int upvoteNumber;
        private String questionContent;

        public int getUpvoteNumber() {
            return upvoteNumber;
        }

        public String getQuestionContent() {
            return questionContent;
        }

        public Question(int upvoteNumber, String questionContent) {
            this.upvoteNumber = upvoteNumber;
            this.questionContent = questionContent;
        }
    }

    private class QuestionListCell extends ListCell<Question> {
        private GridPane content;
        private UUID questionId;
        private UUID code;
        private Label upvoteNumber;
        private Text questionContent;

        public QuestionListCell() {
            super();
            upvoteNumber = new Label();
            questionContent = new Text();
            //Bind the managed property to the visible property so that when the node is
            //not visible, it will also not be accounted for in the layout
            questionContent.managedProperty().bind(questionContent.visibleProperty());

            //Create the Vbox for placing the upvote button and upvote number
            ToggleButton upvoteTriangle = new ToggleButton("up");
            VBox upvote = new VBox(upvoteTriangle, upvoteNumber);
            upvote.setSpacing(5);
            upvote.setAlignment(Pos.CENTER);

            //Create the edit and delete menu items
            MenuItem edit = new MenuItem("Edit");
            MenuItem delete = new MenuItem("Delete");
            //Create options menu and add the edit and delete menu items
            MenuButton options = new MenuButton();
            options.getItems().addAll(edit, delete);

            //Create GridPane and add nodes to it
            content = new GridPane();
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setHgrow(Priority.ALWAYS);

            content.setGridLinesVisible(true);
            content.getColumnConstraints().addAll(new ColumnConstraints(50), col2,
                    new ColumnConstraints(50));

            content.addColumn(0, upvote);
            VBox questionVbox = new VBox(questionContent);
            content.addColumn(1, questionVbox);
            content.addColumn(2, options);

            //Make questionContent resize with width of cell
            double paddingWidth = questionList.getPadding().getLeft()
                    +  questionList.getPadding().getRight() + 120;
            questionContent.wrappingWidthProperty().bind(questionList.widthProperty()
                    .subtract(paddingWidth));

            //Add action listeners to options menu
            edit.setOnAction(event -> editQuestion(questionContent, questionVbox, options, questionId, code));
            delete.setOnAction(event -> deleteQuestionOption(content, options, questionId, code));

            //Set alignment of children in the GridPane
            upvote.setAlignment(Pos.TOP_CENTER);
            GridPane.setValignment(options, VPos.TOP);
            GridPane.setHalignment(options, HPos.RIGHT);
        }

        @Override
        protected void updateItem(Question item, boolean empty) {
            super.updateItem(item, empty);
            //If the item was not null and empty was false, add content to the graphic
            if (item != null && !empty) {
                upvoteNumber.setText(Integer.toString(item.getUpvoteNumber()));
                questionContent.setText(item.getQuestionContent());
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }

    public void editQuestion(Text questionContent, VBox questionVbox, MenuButton options,
                             UUID questionId, UUID code) {
        options.setDisable(true);

        TextArea input = new TextArea();
        input.setWrapText(true);
        input.prefWidthProperty().bind(questionContent.wrappingWidthProperty());

        Button cancel = new Button("Cancel");
        Button update = new Button("Update");
        HBox buttons = new HBox(cancel, update);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        update.setOnAction(event -> updateQuestionContent(options, questionId, code, input.getText()));
        cancel.setOnAction(event -> cancelEdit(options, questionVbox, input, buttons, questionContent));

        questionContent.setVisible(false);
        questionVbox.getChildren().add(input);
        questionVbox.getChildren().add(buttons);
        input.setText(questionContent.getText());
    }

    public void updateQuestionContent(MenuButton options, UUID questionId, UUID code, String text) {
        options.setDisable(false);
        String response = ServerCommunication.editQuestion(questionId, code, text);

        if (response == null) {
            AlertDialog.display("", "Question update failed.");
        } else {
            AlertDialog.display("", "Question update successful.");
        }
    }

    public void cancelEdit(MenuButton options, VBox questionVbox, TextArea input,
                            HBox buttons, Text questionContent) {
        options.setDisable(false);
        questionVbox.getChildren().remove(input);
        questionVbox.getChildren().remove(buttons);
        questionContent.setVisible(true);
    }

    public void deleteQuestionOption(GridPane gridpane, MenuButton options, UUID questionId, UUID code) {
        options.setDisable(true);

        Label confirmation = new Label("Are you sure you want to delete this question?");
        confirmation.setWrapText(true);
        Button yes = new Button("Yes");
        Button cancel = new Button("Cancel");

        HBox dialogue = new HBox(confirmation, yes, cancel);
        dialogue.setPadding(new Insets(5,10,5,10));
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
