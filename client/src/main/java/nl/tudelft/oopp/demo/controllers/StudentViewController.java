package nl.tudelft.oopp.demo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Control;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
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

import java.awt.event.MouseEvent;

public class StudentViewController {
    @FXML
    private HBox topBar;
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
    private ToggleButton hamburger;
    @FXML
    private ToggleButton ansQuestions;
    @FXML
    private ToggleButton polls;
    @FXML
    private Button leaveQuBo;

    private boolean sideMenuOpen;

    /**
     * Code that is run upon loading StudentView.fxml
     */
    @FXML
    private void initialize() {
        //Get questions
        ObservableList<Question> data = FXCollections.observableArrayList();
        data.addAll(new Question(2, "What is life?"),
                new Question(42,"Trolley problem."
                        + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                        + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."
                        + "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."));

        questionList.setItems(data);
        questionList.setCellFactory(listView -> new QuestionListCell());

        //Hide side menu and sidebar
        sideBar.managedProperty().bind(sideBar.visibleProperty());
        sideMenu.managedProperty().bind(sideMenu.visibleProperty());
        sideBar.setVisible(false);
        sideMenu.setVisible(false);
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

    public void leaveQuBo(ActionEvent actionEvent) {
        sideBar.setDisable(true);
        topBar.setDisable(true);
        displayLeavePopup();
    }

    public void displayLeavePopup() {
        Pane layer = new Pane();
        layer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5)");

        Label title = new Label("Leave Question Board?");
        title.setStyle("-fx-font-size: 18");
        Label description = new Label("You will have to use your code to join again.");

        Button yes = new Button("Yes");
        Button no = new Button("No");

        HBox hbox = new HBox(yes, no);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(20);
        hbox.setPadding(new Insets(10,0,0,0));

        VBox vbox = new VBox(title, description, hbox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(15);

        BorderPane dialogue = new BorderPane(vbox);
        int fixedHeight = 170;
        int fixedWidth = 340;
        dialogue.setMaxHeight(fixedHeight);
        dialogue.setMaxWidth(fixedWidth);
        dialogue.setPrefHeight(fixedHeight);
        dialogue.setPrefWidth(fixedWidth);
        dialogue.setStyle("-fx-background-color: rgb(255, 255, 255)");

        StackPane leaveDialogue = new StackPane(layer, dialogue);
        leaveDialogue.setAlignment(Pos.CENTER);
        content.getChildren().add(leaveDialogue);
    }

    public void returnToQuBo() {
        sideBar.setDisable(false);
        topBar.setDisable(false);
    }

    public void returnToHome() {
        SceneLoader.backToHome((Stage) leaveQuBo.getScene().getWindow());
    }

    private static class Question {
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
        private Label upvoteNumber;
        private Text questionContent;

        public QuestionListCell() {
            super();
            upvoteNumber = new Label();
            questionContent = new Text();

            //Create the Vbox for placing the upvote button and upvote number
            ToggleButton upvoteTriangle = new ToggleButton("up");
            VBox upvote = new VBox(upvoteTriangle, upvoteNumber);
            upvote.setSpacing(5);
            upvote.setAlignment(Pos.CENTER);

            //Create options menu with edit and delete options
            MenuButton options = new MenuButton();
            options.getItems().addAll(new MenuItem("Edit"), new MenuItem("Delete"));

            //Create GridPane and add nodes to it
            content = new GridPane();
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setHgrow(Priority.ALWAYS);

            content.setGridLinesVisible(true);
            content.getColumnConstraints().addAll(new ColumnConstraints(50), col2,
                    new ColumnConstraints(50));

            content.addColumn(0, upvote);
            content.addColumn(1, questionContent);
            content.addColumn(2, options);

            //Make questionContent resize with width of cell
            double paddingWidth = questionList.getPadding().getLeft()
                    +  questionList.getPadding().getRight();
            questionContent.wrappingWidthProperty().bind(questionList.widthProperty()
                    .subtract(paddingWidth + 120));

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

}
