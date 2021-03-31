package nl.tudelft.oopp.qubo.controllers.structures;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class QuestionItem extends GridPane {
    private GridPane questionPane;
    private Label upvoteNumber;
    private Text questionBody;
    private Label authorName;
    private ToggleButton upvoteTriangle;

    private UUID questionId;
    private List<String> answers;
    private VBox questionVbox;

    private HashMap<UUID, UUID> upvoteMap;
    private HashMap<UUID, UUID> secretCodeMap;

    private ScrollPane unAnsQuScPane;
    private VBox questionContainer;


    public QuestionItem(UUID questionId, int upvoteNumber, String questionBody, String authorName,
                        List<String> answers, VBox questionContainer, HashMap<UUID, UUID> upvoteMap,
                        HashMap<UUID, UUID> secretCodeMap, ScrollPane scrollPane) {
        this.upvoteNumber = new Label(Integer.toString(upvoteNumber));
        this.questionBody = new Text(questionBody);
        this.authorName = new Label(authorName);
        upvoteTriangle = new ToggleButton();

        this.questionId = questionId;
        this.answers = answers;
        this.questionContainer = questionContainer;

        this.upvoteMap = upvoteMap;
        this.secretCodeMap = secretCodeMap;
        
        unAnsQuScPane = scrollPane;

        construct();
    }

    private void construct() {
        //Bind the managed property to the visible property so that the node is not accounted for
        //in the layout when it is not visible.
        questionBody.managedProperty().bind(questionBody.visibleProperty());

        //Set padding for individual cell (needed to prevent horizontal overflow)
        this.setPadding(new Insets(0,10,20,0));

        //Construct a new questionPane to hold the question and add to content pane
        questionPane = newQuestionPane();
        this.addRow(0, questionPane);

        displayOptions();

        //Add the answers if there are any
        if (answers != null && answers.size() != 0) {
            addAnswers();
        }

        this.setGridLinesVisible(true);
    }

    /**
     * This method adds the answers to a question if there are any.
     *
     *
     */
    public void addAnswers() {
        for (int i = 0; i < answers.size(); i++) {
            Text answer = new Text(answers.get(i));
            BorderPane answerPane = new BorderPane(answer);
//            answer.wrappingWidthProperty().bind(questionContainer.widthProperty()
//                .subtract(80));

            this.addRow((i + 1), answerPane);
        }
    }

    /**
     * This method constructs a new questionPane to hold the upvote button, upvote number,
     * question body, options menu, and author name.
     *
     *
     * @return              A GridPane containing above mentioned information
     */
    public GridPane newQuestionPane() {
        GridPane gridpane = new GridPane();
        questionVbox = newQuestionVbox();

        //Add nodes to the gridpane
        gridpane.addColumn(0, newUpvoteVbox());
        gridpane.addColumn(1, questionVbox);

        //Set column constraints
        ColumnConstraints col2 = new ColumnConstraints();
//        col2.setMaxWidth(GridPane.USE_PREF_SIZE);
        col2.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(new ColumnConstraints(50), col2,
            new ColumnConstraints(50));

        //Set paddings
        gridpane.setPadding(new Insets(6,3,8,3));

        return gridpane;
    }

    /**
     * Constructs and returns a new upvote box.
     *
     * @return              Returns a new upvote box to be displayed.
     */
    public VBox newUpvoteVbox() {
        //Create the Vbox for placing the upvote button and upvote number
        VBox upvote = new VBox(upvoteTriangle, upvoteNumber);
        upvote.setSpacing(5);
        upvote.setAlignment(Pos.TOP_CENTER);

        //Set event listener
        upvoteTriangle.setOnAction(event -> StudentViewActionEvents.upvoteQuestion(questionId, upvoteMap,
            upvoteTriangle, upvoteNumber));

        //Set upvote triangle to selected if question has been upvoted
        if (upvoteMap.containsKey(questionId)) {
            upvoteTriangle.setSelected(true);
        }

        return upvote;
    }

    /**
     * Constructs and returns a new VBox containing the question body and the author name.
     *
     * @return              Returns a new VBox containing the question body and the
     *                      author name to be displayed.
     */
    public VBox newQuestionVbox() {
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
        space.visibleProperty().bind(questionBody.visibleProperty());

        //
        questionBody.wrappingWidthProperty().bind(unAnsQuScPane.widthProperty().subtract(180));

        VBox vbox = new VBox(questionBody, space);
        vbox.setSpacing(10);

        return vbox;
    }

    public void displayOptions() {
        //Determine whether the question was asked by the user
        //If yes create and display the options menu
        if (secretCodeMap.containsKey(questionId)) {
            MenuButton options = newOptionsMenu();
            questionPane.addColumn(2, options);
        }
    }

    /**
     * Constructs and returns a new options menu.
     *
     * @return      Returns a new options menu to be displayed.
     */
    public MenuButton newOptionsMenu() {
        //Create the edit and delete menu items
        MenuItem edit = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");
        //Create options menu and add the edit and delete menu items
        MenuButton options = new MenuButton();
        options.getItems().addAll(edit, delete);

        //Bind properties so that the visibility depends on the disabled property
        options.visibleProperty().bind(options.disableProperty().not());

        //Set alignment of children in the GridPane
        GridPane.setValignment(options, VPos.TOP);
        GridPane.setHalignment(options, HPos.RIGHT);

        //Add action listeners to options menu
        edit.setOnAction(event -> StudentViewActionEvents
            .editQuestionOption(questionBody, questionVbox, options, questionId,
                secretCodeMap.get(questionId)));
        delete.setOnAction(event -> StudentViewActionEvents.deleteQuestionOption(
            this, questionPane, questionContainer, options, questionId, secretCodeMap.get(questionId)));

        return options;
    }
}
