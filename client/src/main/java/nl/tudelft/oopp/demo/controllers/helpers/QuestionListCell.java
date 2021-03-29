package nl.tudelft.oopp.demo.controllers.helpers;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class QuestionListCell extends ListCell<Question> {
    private GridPane content;
    private UUID questionId;
    private Label upvoteNumber;
    private Text questionContent;
    private Label authorName;
    private List<String> answers;

    private ListView<Question> questionList;
    private HashMap<UUID, UUID> secretCodeMap;
    private HashMap<UUID, UUID> upvoteMap;

    public QuestionListCell (ListView<Question> questionList, HashMap<UUID, UUID> secretCodeMap, HashMap<UUID, UUID> upvoteMap) {
        super();
        questionId = null;
        content = new GridPane();
        upvoteNumber = new Label();
        questionContent = new Text();
        authorName = new Label();
        answers = new ArrayList<>();

        this.questionList = questionList;
        this.secretCodeMap = secretCodeMap;
        this.upvoteMap = upvoteMap;
    }

    public VBox newUpvoteVbox(Label upvoteNumber) {
        //Create the Vbox for placing the upvote button and upvote number
        ToggleButton upvoteTriangle = new ToggleButton("up");
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
        edit.setOnAction(event -> StudentViewActionEvents
            .editQuestionOption(questionContent, questionVbox, options, questionId,
                secretCodeMap.get(questionId)));
        delete.setOnAction(event -> StudentViewActionEvents.deleteQuestionOption(content, questionList,
            options, questionId, secretCodeMap.get(questionId)));

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

            cellDisplay();

            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }

    protected void cellDisplay() {
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
}
