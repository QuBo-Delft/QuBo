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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.qubo.controllers.ModeratorViewController;
import nl.tudelft.oopp.qubo.controllers.StudentViewController;
import nl.tudelft.oopp.qubo.controllers.helpers.QuBoActionEvents;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * The QuestionItem pane.
 */
public class QuestionItem extends GridPane {
    private static final Image editImage = new Image("/icons/edit.png");
    private static final Image replyImage = new Image("/icons/reply.png");
    private static final Image markAsAnsImage = new Image("/icons/mark_as_answered.png");
    private static final Image banImage = new Image("/icons/ban.png");
    private static final Image deleteImage = new Image("/icons/delete.png");

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

    private StudentViewController stuController;
    private ModeratorViewController modController;

    public void setStuController(StudentViewController controller) {
        this.stuController = controller;
    }

    public void setModController(ModeratorViewController controller) {
        this.modController = controller;
    }

    /**
     * Constructs a new QuestionItem.
     *
     * @param questionId        The UUID of the question.
     * @param upvoteNumber      Label displaying the number of upvotes for the question.
     * @param questionBody      Text node of the question content.
     * @param authorName        Author of the question.
     * @param answers           Textual answers to the question.
     * @param answeredTime      TimeStamp of when the question was answered.
     * @param questionContainer VBox containing the list of questions.
     * @param scrollPane        ScrollPane containing the VBox that contains the list of questions.
     */
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
        this.setPadding(new Insets(10,0,15,0));

        //Construct a new questionPane to hold the question and add to content pane
        questionPane = newQuestionPane();
        this.addRow(0, questionPane);

        //Add the answers if there are any
        if (answers != null && answers.size() != 0) {
            addAnswers();
        }

        this.getStyleClass().add("qPane");
        questionBody.getStyleClass().add("qContent");
        upvoteNumber.getStyleClass().add("qVotes");
    }

    /**
     * This method adds the answers to a question if there are any.
     */
    private void addAnswers() {
        int i = 1;
        for (AnswerDetailsDto answerDetails : answers) {
            Text answer = new Text(answerDetails.getText());
            BorderPane answerPane = new BorderPane(answer);
            answerPane.setPadding(new Insets(10,15,10,15));
            answer.wrappingWidthProperty().bind(questionBody.wrappingWidthProperty().add(40));

            this.addRow((i + 1), answerPane);
            i++;
        }
    }

    /**
     * This method constructs a new questionPane to hold the upvote button, upvote number,
     * question body, options menu, and author name.
     *
     * @return      A GridPane containing above mentioned information.
     */
    private GridPane newQuestionPane() {
        GridPane gridpane = new GridPane();
        questionVbox = newQuestionVbox();

        //Add nodes to the gridpane
        gridpane.addColumn(1, questionVbox);

        //Set column constraints
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(new ColumnConstraints(50), col2,
            new ColumnConstraints(50));

        return gridpane;
    }

    /**
     * Constructs and returns a new VBox containing the question body and the author name.
     *
     * @return  Returns a new VBox containing the question body and the
     *          author name to be displayed.
     */
    private VBox newQuestionVbox() {
        //Create a pane for putting the author name
        HBox authorHbox = new HBox(authorName);
        authorHbox.setAlignment(Pos.BOTTOM_RIGHT);
        BorderPane space = new BorderPane(authorHbox);
        HBox.setHgrow(authorHbox, Priority.ALWAYS);

        //Set pane to fixed height
        int spaceHeight = 40;
        space.setPrefHeight(spaceHeight);
        space.setMinHeight(spaceHeight);
        space.setMaxHeight(spaceHeight);

        //Bind properties for easier management
        space.managedProperty().bind(space.visibleProperty());
        space.visibleProperty().bind(questionBody.visibleProperty());

        //Bind the wrapping width of the question body so that it doesn't overflow
        questionBody.wrappingWidthProperty().bind(quScPane.widthProperty()
            .subtract(quScPane.getPadding().getLeft() + quScPane.getPadding().getRight() + 120));
        space.prefWidthProperty().bind(questionBody.wrappingWidthProperty());
        space.minWidthProperty().bind(questionBody.wrappingWidthProperty());
        space.maxWidthProperty().bind(questionBody.wrappingWidthProperty());

        VBox vbox = new VBox(questionBody, space);
        vbox.setSpacing(10);

        return vbox;
    }

    /**
     * Method that checks whether the user is the author of the question or a mod, and displays
     * the suitable options MenuButton and options accordingly.
     *
     * @param secretCodeMap HashMap of questionId:secretCode.
     * @param modCode       The moderator code of the board.
     */
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
    private MenuButton newOptionsMenu(UUID code, boolean isMod) {
        MenuButton options = new MenuButton();

        //Create the edit menu item and set action event
        MenuItem edit = newIconItem("Edit", editImage);
        edit.setOnAction(event -> QuBoActionEvents
            .editQuestionOption(questionBody, questionVbox, options, questionId,
                code));
        options.getItems().add(edit);

        if (isMod) {
            //Add mod options
            newModOptions(options, code);
        }

        //Create the delete menu item and set action event
        MenuItem delete = newIconItem("Delete", deleteImage);
        delete.setOnAction(event -> QuBoActionEvents.deleteQuestionOption(
            this, questionPane, questionBody, options, questionId, code));
        options.getItems().add(delete);

        //Bind properties so that the visibility depends on the disabled property
        options.visibleProperty().bind(options.disableProperty().not());

        //Set alignment of children in the GridPane
        GridPane.setValignment(options, VPos.TOP);
        GridPane.setHalignment(options, HPos.RIGHT);

        options.setOnAction(event -> );

        return options;
    }

    /**
     * Adds mod options to the options menu.
     *
     * @param options   The options menu where the new MenuItems are added.
     * @param code      The moderator code. (To be passed on in the action events.)
     */
    private void newModOptions(MenuButton options, UUID code) {
        //Create the reply menu item and set action event
        MenuItem reply = newIconItem("Reply", replyImage);
        reply.setOnAction(event -> QuBoActionEvents.replyToQuestionOption(
            this, questionPane, questionId, code, options, questionBody));
        options.getItems().add(reply);

        if (quScPane.getId().equals("unAnsQuScPane")) {
            //Create the mark as answered menu item if the question resides in the unanswered
            //question list, and set action event
            MenuItem markAsAns = newIconItem("Mark As Answered", markAsAnsImage);
            markAsAns.setOnAction(event -> QuBoActionEvents.markAsAnsOption(
                options, questionBody, questionPane, questionId, code));
            options.getItems().add(markAsAns);
        }

        //Create the ban menu item and set action event
        MenuItem ban = newIconItem("Ban", banImage);
        ban.setOnAction(event -> QuBoActionEvents.banUserOption(
            options, questionPane, questionBody, questionId, code));
        options.getItems().add(ban);
    }

    /**
     * Helper method to construct MenuItems with a custom icon.
     *
     * @param name      The name to be displayed for the MenuItem.
     * @param image     The image to be displayed in the MenuItem.
     * @return          The MenuItem constructed with above mentioned data.
     */
    private MenuItem newIconItem(String name, Image image) {
        ImageView icon = new ImageView(image);
        //Set a fixed ratio for the images
        icon.setPreserveRatio(true);
        icon.setFitHeight(20);

        return new MenuItem(name, icon);
    }

    /**
     * Constructs and returns a new upvote box.
     *
     * @param upvoteMap HashMap of questionId:upvoteId.
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
