package nl.tudelft.oopp.qubo.controllers.helpers;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SideBarControl {
    public static boolean showHideSelected(ToggleButton select, ToggleButton deselect, VBox sideMenu,
                                           VBox ansQuVbox) {
        if (sideMenu.isVisible() && deselect.isSelected()) {
            deselect.setSelected(false);
            sideMenu.getChildren().clear();
            toggleSelector(select, sideMenu, ansQuVbox);
            return true;
        } else if (!sideMenu.isVisible()) {
            toggleSelector(select, sideMenu, ansQuVbox);
            return true;
        } else {
            sideMenu.getChildren().clear();
            sideMenu.setVisible(false);
            return false;
        }
    }

    private static void toggleSelector(ToggleButton select, VBox sideMenu, VBox ansQuVbox) {
        if (select.getId().equals("polls")) {
            showPolls(sideMenu);
        } else {
            showAnsQuestions(sideMenu, ansQuVbox);
        }
    }

    private static void showPolls(VBox sideMenu) {
        Label title = new Label("Polls");
        sideMenu.setVisible(true);
        sideMenu.getChildren().add(title);

        //TODO: Fetch polls and display in a ListView
    }

    private static void showAnsQuestions(VBox sideMenu, VBox ansQuVbox) {
        Label title = new Label("Answered Questions");
        sideMenu.setVisible(true);
        sideMenu.getChildren().add(title);
        sideMenu.getChildren().add(ansQuVbox);
        VBox.setVgrow(ansQuVbox, Priority.ALWAYS);
    }
}
