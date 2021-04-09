package nl.tudelft.oopp.qubo.controllers.helpers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

/**
 * The Sidebar control.
 */
public class SideBarControl {
    /**
     * This method hides and shows the content of the sideMenu based on the Toggle button clicked on the
     * sidebar.
     *
     * @param select        The selected ToggleButton.
     * @param deselect      The unselected ToggleButton.
     * @param sideMenu      The VBox containing the content of the sideMenu.
     * @param sideMenuTitle The title of the sideMenu.
     * @param ansQuVbox     VBox containing the list of answered questions.
     * @param pollVbox      VBox containing the list of polls.
     * @param pollBtn       The button in the moderator view to create new polls.
     * @return Boolean of whether or not the sideMenu is still showing.
     */
    public static boolean showHideSelected(ToggleButton select, ToggleButton deselect, VBox sideMenu,
                                           Label sideMenuTitle, VBox ansQuVbox, VBox pollVbox, Button pollBtn) {
        if (sideMenu.isVisible() && deselect.isSelected()) {
            deselect.setSelected(false);
            toggleSelector(select, sideMenuTitle, ansQuVbox, pollVbox, pollBtn);
            return true;
        } else if (!sideMenu.isVisible()) {
            sideMenu.setVisible(true);
            toggleSelector(select, sideMenuTitle, ansQuVbox, pollVbox, pollBtn);
            return true;
        } else {
            sideMenu.setVisible(false);
            return false;
        }
    }

    private static void toggleSelector(ToggleButton select, Label sideMenuTitle,
                                       VBox ansQuVbox, VBox pollVbox, Button pollBtn) {
        if (select.getId().equals("polls")) {
            ansQuVbox.setVisible(false);
            showPolls(sideMenuTitle, pollVbox, pollBtn);
        } else {
            pollVbox.setVisible(false);
            if (pollBtn != null) {
                pollBtn.setVisible(false);
            }
            showAnsQuestions(sideMenuTitle, ansQuVbox);
        }
    }

    private static void showPolls(Label sideMenuTitle, VBox pollVbox, Button pollBtn) {
        sideMenuTitle.setText("Polls");
        pollVbox.setVisible(true);
        if (pollBtn != null) {
            pollBtn.setVisible(true);
        }
    }

    private static void showAnsQuestions(Label sideMenuTitle, VBox ansQuVbox) {
        sideMenuTitle.setText("Answered Questions");
        ansQuVbox.setVisible(true);
    }
}
