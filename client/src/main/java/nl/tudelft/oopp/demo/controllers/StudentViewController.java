package nl.tudelft.oopp.demo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.text.Text;

public class StudentViewController {
    @FXML
    private ListView<CustomThing> questionList;

    /**
     * Code that is run upon loading StudentView.fxml
     */
    @FXML
    private void initialize() {
        ObservableList<StudentViewController.CustomThing> data = FXCollections.observableArrayList();
        data.addAll(new StudentViewController.CustomThing(2, "What is life?"),
                new StudentViewController.CustomThing(42,"Trolley problem." +
                        "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem." +
                        "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem." +
                        "Trolley problem.Trolley problem.Trolley problem.Trolley problem.Trolley problem."));

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

            content.setGridLinesVisible( true );
            content.getColumnConstraints().addAll(new ColumnConstraints(50), col2,
                    new ColumnConstraints(50));

            content.addColumn( 0, upvote);
            content.addColumn( 1, questionContent);
            content.addColumn( 2, options);

            //Make questionContent resize with width of cell
            double paddingWidth = questionList.getPadding().getLeft() +
                    questionList.getPadding().getRight();
            questionContent.wrappingWidthProperty().bind(questionList.widthProperty().subtract(
                    paddingWidth + 120));
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
