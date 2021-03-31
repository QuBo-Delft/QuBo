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
import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class QuestionItem extends GridPane {
    private GridPane questionPane;
    private Label upvoteNumber;
    private VBox questionVbox;
    private Text questionBody;
    private Label authorName;
    private ToggleButton upvoteTriangle;

    private UUID questionId;
    private Set<AnswerDetailsDto> answers;
    private Timestamp answeredTime;

    private ScrollPane quScPane;
    private VBox questionContainer;

    public QuestionItem(UUID questionId, int upvoteNumber, String questionBody, String authorName,
                        Set<AnswerDetailsDto> answers, Timestamp answeredTime, VBox questionContainer,
                        ScrollPane scrollPane) {
        this.upvoteNumber = new Label(Integer.toString(upvoteNumber));
        this.questionBody = new Text(questionBody);
        this.authorName = new Label(authorName);
        upvoteTriangle = new ToggleButton();

        this.questionId = questionId;
        this.answers = answers;
        this.answeredTime = answeredTime;

        this.questionContainer = questionContainer;
        
        quScPane = scrollPane;

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

        //Add the answers if there are any
        if (answers != null && answers.size() != 0) {
            addAnswers();
        }

        this.setGridLinesVisible(true);
    }

    /**
     * This method adds the answers to a question if there are any.
     */
    public void addAnswers() {
        int i = 1;
        for (AnswerDetailsDto answerDetails : answers) {
            Text answer = new Text(answerDetails.getText());
            BorderPane answerPane = new BorderPane(answer);
            answer.wrappingWidthProperty().bind(questionBody.wrappingWidthProperty());

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
        gridpane.addColumn(1, questionVbox);

        //Set column constraints
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(new ColumnConstraints(50), col2,
            new ColumnConstraints(50));

        //Set paddings
        gridpane.setPadding(new Insets(6,3,8,3));

        return gridpane;
    }

    /**
     * Constructs and returns a new VBox containing the question body and the author name.
     *
     * @return  Returns a new VBox containing the question body and the
     *          author name to be displayed.
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

        //Bind the wrapping width of the question body so that it doesn't overflow
        questionBody.wrappingWidthProperty().bind(quScPane.widthProperty().subtract(180));

        VBox vbox = new VBox(questionBody, space);
        vbox.setSpacing(10);

        return vbox;
    }

    public void displayOptions(HashMap<UUID, UUID> secretCodeMap, UUID modCode) {
        //Determine whether the question was asked by the user
        //If yes create and display the options menu
        if (modCode != null) {
            MenuButton options = newOptionsMenu(modCode, true);
            questionPane.addColumn(2, options);
        } else if (secretCodeMap.containsKey(questionId)) {
            MenuButton options = newOptionsMenu(secretCodeMap.get(questionId), false);
            questionPane.addColumn(2, options);
        }
    }

    /**
     * Constructs and returns a new options menu.
     *
     * @return      Returns a new options menu to be displayed.
     */
    public MenuButton newOptionsMenu(UUID code, boolean isMod) {
        MenuButton options = new MenuButton();

        //Create the edit menu item and set action event
        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(event -> QuBoActionEvents
            .editQuestionOption(questionBody, questionVbox, options, questionId,
                code));
        options.getItems().add(edit);

        if (isMod) {
            MenuItem reply = new MenuItem("Reply");
            reply.setOnAction(event -> QuBoActionEvents.editQuestionOption(questionBody, questionVbox,
                options, questionId, code));
            options.getItems().add(reply);

            if (quScPane.getId().equals("unAnsQuScPane")) {
                MenuItem markAsAns = new MenuItem("Mark As Answered");
                markAsAns.setOnAction(event -> QuBoActionEvents.markAsAnsUnAns(questionId, code));
                options.getItems().add(markAsAns);
            }
        }

        //create the delete menu item and set action event
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(event -> QuBoActionEvents.deleteQuestionOption(
            this, questionPane, questionContainer, options, questionId, code));
        options.getItems().add(delete);

        //Bind properties so that the visibility depends on the disabled property
        options.visibleProperty().bind(options.disableProperty().not());

        //Set alignment of children in the GridPane
        GridPane.setValignment(options, VPos.TOP);
        GridPane.setHalignment(options, HPos.RIGHT);

        return options;
    }

    /**
     * Constructs and returns a new upvote box.
     */
    public void newUpvoteVbox(HashMap<UUID, UUID> upvoteMap) {
        //Create the Vbox for placing the upvote button and upvote number
        VBox upvote = new VBox(upvoteTriangle, upvoteNumber);
        upvote.setSpacing(8);
        upvote.setAlignment(Pos.TOP_CENTER);

        //Set event listener
        upvoteTriangle.setOnAction(event -> QuBoActionEvents.upvoteQuestion(questionId, upvoteMap,
            upvoteTriangle, upvoteNumber));

        //Set upvote triangle to selected if question has been upvoted
        if (upvoteMap.containsKey(questionId)) {
            upvoteTriangle.setSelected(true);
        }

        questionPane.addColumn(0, upvote);
    }
}
