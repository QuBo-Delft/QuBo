package nl.tudelft.oopp.demo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Button;

public class StudentViewController {
    @FXML
    private ListView<Question> questionList;
    @FXML
    private HBox sideScreen;
    @FXML
    private VBox sideBar;
    @FXML
    private VBox sideMenu;
    @FXML
    private ToggleButton hamburger;
    @FXML
    private ToggleButton ansQuestions;
    @FXML
    private ToggleButton polls;
    @FXML
    private Button leaveQuBo;

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
        questionList.setCellFactory(listView -> new CustomListCell());

        //Hide side menu and sidebar
        sideScreen.managedProperty().bind(sideScreen.visibleProperty());
        sideBar.managedProperty().bind(sideBar.visibleProperty());
        sideMenu.managedProperty().bind(sideMenu.visibleProperty());
        sideScreen.setVisible(false);
        sideMenu.setVisible(false);
    }

    public void showHideSideBar() {
        sideScreen.setVisible(!sideScreen.isVisible());
    }

    public void showHideAnsQuestions() {
        if (sideMenu.isVisible() && polls.isSelected()) {
            polls.setSelected(false);
            sideMenu.getChildren().clear();
            showAnsQuestions();
        } else if (!sideMenu.isVisible()) {
            showAnsQuestions();
        } else {
            sideMenu.getChildren().clear();
            sideMenu.setVisible(false);
        }
    }

    public void showAnsQuestions() {
        Label title = new Label("Answered Questions");
        sideMenu.setVisible(true);
        sideMenu.getChildren().add(title);
    }

    public void showHidePolls() {
        if (sideMenu.isVisible() && ansQuestions.isSelected()) {
            ansQuestions.setSelected(false);
            sideMenu.getChildren().clear();
            showPolls();
        } else if (!sideMenu.isVisible()) {
            showPolls();
        } else {
            sideMenu.getChildren().clear();
            sideMenu.setVisible(false);
        }
    }

    public void showPolls() {
        Label title = new Label("Polls");
        sideMenu.setVisible(true);
        sideMenu.getChildren().add(title);
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

    private class CustomListCell extends ListCell<Question> {
        private GridPane content;
        private Label upvoteNumber;
        private Text questionContent;

        public CustomListCell() {
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
