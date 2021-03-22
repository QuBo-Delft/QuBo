package nl.tudelft.oopp.demo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StudentViewController {
    @FXML
    private ListView<CustomThing> questionList;

    @FXML
    private void initialize() {
        ObservableList<StudentViewController.CustomThing> data = FXCollections.observableArrayList();
        data.addAll(new StudentViewController.CustomThing("Cheese", 123),
                new StudentViewController.CustomThing("Horse", 456),
                new StudentViewController.CustomThing("Jam", 789));

        questionList.setItems(data);
        questionList.setCellFactory(listView -> new CustomListCell());
    }

    private static class CustomThing {
        private String name;
        private int price;

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public CustomThing(String name, int price) {
            super();
            this.name = name;
            this.price = price;
        }
    }

    private class CustomListCell extends ListCell<CustomThing> {
        private HBox content;
        private Text name;
        private Text price;

        public CustomListCell() {
            super();
            name = new Text();
            price = new Text();
            VBox vbox = new VBox(name, price);
            content = new HBox(new Label("[Graphic]"), vbox);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(StudentViewController.CustomThing item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) { // <== test for null item and empty parameter
                name.setText(item.getName());
                price.setText(String.format("%d $", item.getPrice()));
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }

}
