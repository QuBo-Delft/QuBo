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
import javafx.scene.layout.BorderPane;
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
    //HashMap of questionId:secretCode, needed when editing and deleting questions
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
     * Method that sets the username of the application user.
     *
     * @param authorName    The name of the student that joined the question board.
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * Code that is run upon loading StudentView.fxml
     */
    @FXML
    private void initialize() {
//        testQuestions();
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

        //Make ListCells unable to be selected individually (remove blue highlighting)
        questionList.setSelectionModel(new NoSelectionModel<>());
        questionList.setFocusModel(new NoFocusModel<>());
        //Remove border of focus
        questionList.setStyle("-fx-background-insets: 0 ;");

        questionList.setEditable(true);
    }

    private void testQuestions() {
        ObservableList<Question> data = FXCollections.observableArrayList();

        data.addAll(new Question(UUID.randomUUID(), 2, "What is life?",
                "ChickenWings", null),
            new Question(UUID.randomUUID(), 42,"Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem.",
                "ChickenWings", null));

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

        if (unansweredQuestions.length != 0) { //There exists unanswered questions
            //Clear current questions
            questionList.getItems().clear();

            ObservableList<Question> data = FXCollections.observableArrayList();
            //For each question in the list create a new Question object
            for (QuestionDetailsDto question : unansweredQuestions) {
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

            questionList.setItems(data);
            questionList.setCellFactory(listView -> new QuestionListCell());
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
        displayQuestions();
    }

    public void displayHelpDoc() {
    }

    private static class Question {
        private UUID questionId;
        private int upvoteNumber;
        private String questionContent;
        private String authorName;
        private List<String> answers;

        public UUID getQuestionId() {
            return questionId;
        }

        public int getUpvoteNumber() {
            return upvoteNumber;
        }

        public String getQuestionContent() {
            return questionContent;
        }

        public String getAuthorName() {
            return authorName;
        }

        public List<String> getAnswers() {
            return answers;
        }

        public void setAnswers(List<String> answers) {
            this.answers = answers;
        }

        public Question(UUID questionId, int upvoteNumber, String questionContent, String authorName,
                        List<String> answers) {
            this.questionId = questionId;
            this.upvoteNumber = upvoteNumber;
            this.questionContent = questionContent;
            this.authorName = authorName;
            this.answers = answers;
        }
    }

    private class QuestionListCell extends ListCell<Question> {
        private GridPane content;

        private UUID questionId;
        private Label upvoteNumber;
        private Text questionContent;

        private Label authorName;
        private List<String> answers;

        public QuestionListCell() {
            super();
            questionId = null;
            content = new GridPane();
            upvoteNumber = new Label();
            questionContent = new Text();
            authorName = new Label();
            answers = new ArrayList<>();

            //Bind the managed property to the visible property so that the node is not accounted for
            //in the layout when it is not visible.
            questionContent.managedProperty().bind(questionContent.visibleProperty());

            this.setPadding(new Insets(0,10,20,0));

            VBox questionVbox = newQuestionVbox(authorName);

            //Determine whether the question was asked by the user
            //If yes create and display the options menu
            MenuButton options = new MenuButton();
            if (secretCodeMap.containsKey(questionId)) {
                options = newOptionsMenu(questionVbox);
                this.setEditable(true);
            }

            //Add nodes to the gridpane
            content.addColumn(0, newUpvoteVbox(upvoteNumber));
            content.addColumn(1, questionVbox);
            content.addColumn(2, options);

            //Set column constraints
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setMaxWidth(GridPane.USE_PREF_SIZE);
            col2.setHgrow(Priority.ALWAYS);
            content.getColumnConstraints().addAll(new ColumnConstraints(50), col2,
                new ColumnConstraints(50));

            //Make questionContent resize with width of cell
            double paddingWidth = questionList.getPadding().getLeft()
                    +  questionList.getPadding().getRight() + 140;
            questionContent.wrappingWidthProperty().bind(questionList.widthProperty()
                    .subtract(paddingWidth));

            //Make gridlines visible for clarity during development
            content.setGridLinesVisible(true);
            //Set paddings
            content.setPadding(new Insets(6,3,8,3));
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

            //Set event listener
            upvoteTriangle.setOnAction(event -> upvoteQuestion(questionId, upvoteTriangle, upvoteNumber));

            //Set upvote triangle to selected if question has been upvoted
            if (upvoteMap.containsKey(questionId)) {
                upvoteTriangle.setSelected(true);
            }

            return upvote;
        }

        public MenuButton newOptionsMenu(VBox questionVbox) {
            //Create the edit and delete menu items
            MenuItem edit = new MenuItem("Edit");
            MenuItem delete = new MenuItem("Delete");
            //Create options menu and add the edit and delete menu items
            MenuButton options = new MenuButton();
            options.getItems().addAll(edit, delete);

            //Bind properties so that the visibility depends on the disabled property
            options.visibleProperty().bind(options.disableProperty().not());

            //Add action listeners to options menu
            edit.setOnAction(event -> editQuestionOption(questionContent, questionVbox, options,
                questionId, secretCodeMap.get(questionId)));
            delete.setOnAction(event -> deleteQuestionOption(content, options, questionId,
                secretCodeMap.get(questionId)));

            return options;
        }

        public VBox newQuestionVbox(Label authorName) {
            //Create a pane for putting the author name
            HBox authorHbox = new HBox(authorName);
            authorHbox.setAlignment(Pos.BOTTOM_RIGHT);
            BorderPane space = new BorderPane(authorHbox);

            //Set pane to fixed height
            int spaceHeight = 40;
            space.setPrefHeight(spaceHeight);
            space.setMinHeight(spaceHeight);
            space.setMaxHeight(spaceHeight);

            //Bind properties for easier management
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
                authorName.setText(item.getAuthorName());
                questionId = item.getQuestionId();
                answers = item.getAnswers();



                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }

        protected void functionDisplay() {

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

        //Request automatic upvote
        String response = ServerCommunication.addQuestionVote(questionId);
        if (response == null) {
            //When the request fails, display alert
            AlertDialog.display("", "Automatic upvote failed.");
        } else {
            //When the request is successful, store the question UUID with the vote UUID in the HashMap
            QuestionVoteDetailsDto vote = gson.fromJson(response, QuestionVoteDetailsDto.class);
            upvoteMap.put(questionId, vote.getId());
        }

        // TODO: Update the view of questions
    }

    /**
     * This method is run when the upvote button is clicked.
     * When the ToggleButton is activated: Sends a request to the server to add a vote.
     * When the ToggleButton is deactivated: Sends a request to the server to remove the vote.
     *
     * @param questionId        UUID of the question that the user decides to upvote.
     * @param upvoteTriangle    The ToggleButton which the user clicks to add a vote.
     */
    public void upvoteQuestion(UUID questionId, ToggleButton upvoteTriangle, Label upvoteNumber) {
        if (upvoteTriangle.isSelected()) {
            //Code that runs when the button is activated
            //Check if the question has already been upvoted
            if (upvoteMap.containsKey(questionId)) {
                AlertDialog.display("", "You have already upvoted this question!");
                return;
            }
            toggleUpvoteTrue(questionId, upvoteTriangle, upvoteNumber);
        } else {
            //Code that runs when the button is deactivated
            toggleUpvoteFalse(questionId, upvoteTriangle, upvoteNumber);
        }
    }

    /**
     * This method is called when the upvote button is toggled on/selected.
     * Sends a request to add a vote to the server, and executes different behaviour depending
     * on the received response.
     *
     * @param questionId        The UUID of the question that has been upvoted.
     * @param upvoteTriangle    The upvote ToggleButton (Needs to be deselected if request fails).
     */
    public void toggleUpvoteTrue(UUID questionId, ToggleButton upvoteTriangle, Label upvoteNumber) {
        String response = ServerCommunication.addQuestionVote(questionId);

        if (response == null) {
            AlertDialog.display("", "Upvote failed.");
            //Unselect the button as the upvote action failed
            upvoteTriangle.setSelected(false);
        } else {
            //When the request is successful, store the question UUID with the vote UUID in the HashMap
            QuestionVoteDetailsDto dto = gson.fromJson(response, QuestionVoteDetailsDto.class);
            upvoteMap.put(questionId, dto.getId());
            //Update upvote number locally
            int number = Integer.parseInt(upvoteNumber.getText());
            number++;
            upvoteNumber.setText(Integer.toString(number));
        }
    }

    /**
     * This method is called when the upvote button is toggled off/deselected.
     * Sends a request to delete a vote from the server, and executes different behaviour depending
     * on the received response.
     *
     * @param questionId        The UUID of the question that the upvote needs to be removed from.
     * @param upvoteTriangle    The upvote ToggleButton (Needs to be reselected if request fails).
     */
    public void toggleUpvoteFalse(UUID questionId, ToggleButton upvoteTriangle, Label upvoteNumber) {
        String response = ServerCommunication.deleteQuestionVote(questionId, upvoteMap.get(questionId));

        if (response == null) {
            AlertDialog.display("", "Canceling upvote failed.");
            //Reselect the button as the un-upvote action failed
            upvoteTriangle.setSelected(true);
        } else {
            //When the request is successful, remove upvote from the HashMap
            upvoteMap.remove(questionId);
            //Update upvote number locally
            int number = Integer.parseInt(upvoteNumber.getText());
            number--;
            upvoteNumber.setText(Integer.toString(number));
        }
    }

    /**
     * This method runs when the user selects Edit from the options Menu.
     * Hides the original Text node and displays a Text Area node with the content of the
     * original question set in the Text Area for easier editing.
     * /
     * Displays two buttons ("Cancel" and "Update").
     * Update -> Sends a request to the server to update question content
     * Cancel -> Cancels the action
     *
     * @param questionContent   Text node of the question content (Needs to be hidden when editing)
     * @param questionVbox      VBox containing question content (Needed to display the text area in it)
     * @param options           The options menu node (Needs to be disabled when editing)
     * @param questionId        The UUID of the question that is being edited
     * @param code              Secret code of the question
     */
    public void editQuestionOption(Text questionContent, VBox questionVbox, MenuButton options,
                                   UUID questionId, UUID code) {
        //Disable options menu
        options.setDisable(true);

        //Create a new TextArea and bind its size
        TextArea input = new TextArea();
        input.setWrapText(true);
        input.prefWidthProperty().bind(questionContent.wrappingWidthProperty());
        input.minWidthProperty().bind(questionContent.wrappingWidthProperty());
        input.maxWidthProperty().bind(questionContent.wrappingWidthProperty());
        input.setPrefRowCount(5);

        //Create the buttons and set their alignments
        Button cancel = new Button("Cancel");
        Button update = new Button("Update");
        HBox buttons = new HBox(cancel, update);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setSpacing(15);

        //Set action listeners for the buttons
        update.setOnAction(event -> updateQuestion(options, questionId, code, input.getText(),
            questionContent, questionVbox, input, buttons));
        cancel.setOnAction(event -> cancelEdit(options, questionVbox, input, buttons, questionContent));

        //Hide question text and display text area
        questionContent.setVisible(false);
        questionVbox.getChildren().add(input);
        questionVbox.getChildren().add(buttons);
        input.setText(questionContent.getText());
    }

    /**
     * This method runs when the Update button (created in editQuestion) is clicked.
     * Sends an editQuestion request to the server.
     * /
     * If the request is successful -> Displays alert, removes the text area and buttons, and updates
     * the question content locally as well.
     * If the request fails -> Displays an alert and prevents the user from losing the edited question.
     *
     * @param options           The options menu node (Needs to be disabled when editing)
     * @param questionId        The UUID of the question that is being edited
     * @param code              Secret code of the question
     * @param text              Content of the text area (Edited question)
     * @param questionContent   Text node of the question content (Needs to be shown after successful edit)
     * @param questionVbox      VBox containing question content (Needed to remove the text area in it)
     * @param input             The Text Area node (Needs to be removed after a successful edit)
     * @param buttons           The HBox containing the buttons (Needs to be removed after a successful edit)
     */
    public void updateQuestion(MenuButton options, UUID questionId, UUID code, String text,
                               Text questionContent, VBox questionVbox, TextArea input,
                               HBox buttons) {
        //Send a request to the server
        String response = ServerCommunication.editQuestion(questionId, code, text);

        if (response == null) {
            //If request failed
            AlertDialog.display("Unsuccessful Request", "Failed to update your question, please try again.");
        } else {
            //If request successful
            //Remove text area and buttons
            questionVbox.getChildren().remove(input);
            questionVbox.getChildren().remove(buttons);
            //Set edited text to question content area and show
            questionContent.setText(text);
            questionContent.setVisible(true);
            //Enable options menu as editing has been completed successfully
            options.setDisable(false);
        }
    }

    /**
     * This method runs when the Cancel button (created in editQuestion) is clicked.
     * Removes all the nodes that were added when editing the question and displays original question.
     *
     * @param options           The options menu node (Needs to be disabled when editing)
     * @param questionVbox      VBox containing question content (Needed to remove the text area in it)
     * @param input             The Text Area node (Needs to be removed after a successful edit)
     * @param buttons           The HBox containing the buttons (Needs to be removed after a successful edit)
     * @param questionContent   Text node of the question content (Needs to be shown after successful edit)
     */
    public void cancelEdit(MenuButton options, VBox questionVbox, TextArea input,
                            HBox buttons, Text questionContent) {
        options.setDisable(false);
        questionVbox.getChildren().remove(input);
        questionVbox.getChildren().remove(buttons);
        questionContent.setVisible(true);
    }

    /**
     * This method runs when the user selects Delete from the options Menu.
     * Displays a confirmation dialogue and two buttons ("Yes" and "Cancel").
     * /
     * Yes -> Sends a request to the server to delete the question.
     * Cancel -> Cancels the action.
     *
     * @param gridpane      GridPane of the cell (Needed to add a row for the confirmation dialogue)
     * @param options       The options menu node (Needs to be disabled when confirmation dialogue shows up)
     * @param questionId    The UUID of the question that is being deleted
     * @param code          Secret code of the question
     */
    public void deleteQuestionOption(GridPane gridpane, MenuButton options, UUID questionId, UUID code) {
        //Disable options menu
        options.setDisable(true);

        //Create new label
        Label confirmation = new Label("Are you sure you want to delete this question?");
        confirmation.setPadding(new Insets(0,5,0,0));
        confirmation.setWrapText(true);
        //Set width bounds to the dialogue so it doesn't overflow the question box
        double widthBind = questionList.getPadding().getLeft()
            +  questionList.getPadding().getRight() + 200;
        confirmation.prefWidthProperty().bind(questionList.widthProperty().subtract(widthBind));

        //Create buttons
        Button yes = new Button("Yes");
        Button cancel = new Button("Cancel");
        HBox dialogue = new HBox(confirmation, yes, cancel);

        //Set layouts
        dialogue.setPadding(new Insets(5,10,5,10));
        dialogue.setSpacing(15);
        dialogue.setAlignment(Pos.CENTER);

        //Show confirmation dialogue
        gridpane.addRow(1, dialogue);
        GridPane.setColumnSpan(dialogue, GridPane.REMAINING);

        //Set action listeners
        yes.setOnAction(event -> deleteQuestion(gridpane, questionId, code));
        cancel.setOnAction(event -> cancelDeletion(options, gridpane, dialogue));
    }

    /**
     * This method runs when the Delete button (created in deleteQuestion) is clicked.
     * Sends a deleteQuestion request to the server.
     * /
     * If the request is successful -> Displays an alert.
     * If the request fails -> Displays successful removal label and icon.
     *
     * @param gridPane      GridPane of the cell (Needed to add a row for the confirmation dialogue)
     * @param questionId    The UUID of the question that is being edited
     * @param code          Secret code of the question
     */
    public void deleteQuestion(GridPane gridPane, UUID questionId, UUID code) {
        //Send a request to the server
        String response = ServerCommunication.deleteQuestion(questionId, code);

        if (response == null) {
            //If the request failed
            AlertDialog.display("Unsuccessful Request", "Failed to delete your question, please try again.");
        } else {
            //If the request was successful
            AlertDialog.display("", "Question deletion successful.");
            //TODO: Display successful removal label and icon
            //gridPane.setVisible(false);
            //gridPane.setManaged(false);
        }
    }

    /**
     * This method runs when the Cancel button (created in deleteQuestion) is clicked.
     * Removes the confirmation dialogue and enables the options menu.
     *
     * @param options   The options menu node (Needs to be enabled)
     * @param gridPane  GridPane of the cell (Needed to remove row of confirmation dialogue)
     * @param dialogue  The confirmation dialogue (Needs to be removed)
     */
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
