package nl.tudelft.oopp.qubo.controllers.helpers;

import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SideBarControl {
    public static boolean showHideSelected(ToggleButton select, ToggleButton deselect, VBox sideMenu,
                                           Label sideMenuTitle, VBox ansQuVbox, VBox pollVbox) {
        if (sideMenu.isVisible() && deselect.isSelected()) {
            deselect.setSelected(false);
            toggleSelector(select, sideMenuTitle, ansQuVbox, pollVbox);
            return true;
        } else if (!sideMenu.isVisible()) {
            sideMenu.setVisible(true);
            toggleSelector(select, sideMenuTitle, ansQuVbox, pollVbox);
            return true;
        } else {
            sideMenu.setVisible(false);
            return false;
        }
    }

    private static void toggleSelector(ToggleButton select, Label sideMenuTitle,
                                       VBox ansQuVbox, VBox pollVbox) {
        if (select.getId().equals("polls")) {
            ansQuVbox.setVisible(false);
            showPolls(sideMenuTitle, pollVbox);
        } else {
            pollVbox.setVisible(false);
            showAnsQuestions(sideMenuTitle, ansQuVbox);
        }
    }

    private static void showPolls(Label sideMenuTitle, VBox pollVbox) {
        sideMenuTitle.setText("Polls");
        pollVbox.setVisible(true);

        //TODO: Fetch polls and display in VBox
    }

    private static void showAnsQuestions(Label sideMenuTitle, VBox ansQuVbox) {
        sideMenuTitle.setText("Answered Questions");
        ansQuVbox.setVisible(true);
    }
}
