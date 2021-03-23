package nl.tudelft.oopp.demo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StudentViewController {
    @FXML
    private ListView<CustomThing> questionList;

    @FXML
    private void initialize() {
        ObservableList<StudentViewController.CustomThing> data = FXCollections.observableArrayList();
        data.addAll(new StudentViewController.CustomThing(2, "What is life?"),
                new StudentViewController.CustomThing(42,"Trolley problem."));

        questionList.setItems(data);
        questionList.setCellFactory(listView -> new CustomListCell());
    }

    private static class CustomThing {
        private int upvoteNumber;
        private String questionContent;

        public int getUpvoteNumber() {
            return upvoteNumber;
        }

        public String getQuestionContent() {
            return questionContent;
        }

        public CustomThing(int upvoteNumber, String questionContent) {
            super();
            this.upvoteNumber = upvoteNumber;
            this.questionContent = questionContent;
        }
    }

    private class CustomListCell extends ListCell<CustomThing> {
        private HBox content;
        private Label upvoteNumber;
        private Text questionContent;

        public CustomListCell() {
            super();
            upvoteNumber = new Label();
            questionContent = new Text();

            Button upvoteTriangle = new Button("up");
            VBox upvote = new VBox(upvoteTriangle, upvoteNumber);
            upvote.setAlignment(Pos.CENTER);

            MenuButton options = new MenuButton();
            options.getItems().addAll(new MenuItem("Edit"), new MenuItem("Delete"));

            Pane pane = new Pane();
            HBox.setHgrow(pane, Priority.ALWAYS);

            content = new HBox(upvote, questionContent, pane, options);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(StudentViewController.CustomThing item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) { // <== test for null item and empty parameter
                upvoteNumber.setText(Integer.toString(item.getUpvoteNumber()));
                questionContent.setText(item.getQuestionContent());
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }

}
