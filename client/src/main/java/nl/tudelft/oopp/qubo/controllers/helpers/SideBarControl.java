package nl.tudelft.oopp.qubo.controllers.helpers;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.qubo.controllers.structures.Question;

public class SideBarControl {
    public static boolean showHideSelected(ToggleButton select, ToggleButton deselect, VBox sideMenu,
                                           ListView<Question> ansQuListView, boolean sideMenuOpen) {
        if (sideMenu.isVisible() && deselect.isSelected()) {
            deselect.setSelected(false);
            sideMenu.getChildren().clear();
            toggleSelector(select, sideMenu);
        } else if (!sideMenu.isVisible()) {
            sideMenuOpen = true;
            toggleSelector(select, sideMenu);
            return true;
        } else {
            sideMenu.getChildren().clear();
            sideMenu.setVisible(false);
            sideMenuOpen = false;
            return false;
        }
    }

    public static void toggleSelector(ToggleButton select, VBox sideMenu, ListView<Question> ansQuListView) {
        if (select.getId().equals("polls")) {
            showPolls(sideMenu);
        } else {
            showAnsQuestions(sideMenu, ansQuListView);
        }
    }

    public static void showPolls(VBox sideMenu) {
        Label title = new Label("Polls");
        sideMenu.setVisible(true);
        sideMenu.getChildren().add(title);

        //TODO: Fetch polls and display in a ListView
    }

    public static void showAnsQuestions(VBox sideMenu, ListView<Question> ansQuListView) {
        Label title = new Label("Answered Questions");
        sideMenu.setVisible(true);
        sideMenu.getChildren().add(title);
        sideMenu.getChildren().add(ansQuListView);
        VBox.setVgrow(ansQuListView, Priority.ALWAYS);
    }
}
