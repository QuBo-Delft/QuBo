package nl.tudelft.oopp.qubo.controllers.helpers;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The Layout properties.
 */
public class LayoutProperties {
    /**
     * This method is run through the initialise method in StudentView and ModeratorView.
     *
     * @param content      StackPane containing the content of the scene apart from the sideBar and topBar
     * @param sideBar      Sidebar of the screen
     * @param sideMenu     Menu that shows up when using the sidebar
     * @param pollVbox     VBox containing the polls
     * @param ansQuVbox    VBox containing the answered questions
     * @param unAnsQuVbox  VBox containing the unanswered questions
     * @param paceVotePane BorderPane containing the pace votes
     */
    public static void startupProperties(StackPane content, VBox sideBar, VBox sideMenu, VBox pollVbox,
                                         VBox ansQuVbox, VBox unAnsQuVbox, BorderPane paceVotePane) {
        //Bind the management property and visibility property of some nodes for easier management.
        LayoutProperties.hideEntity(sideBar);
        LayoutProperties.hideEntity(sideMenu);
        LayoutProperties.hideEntity(pollVbox);
        LayoutProperties.hideEntity(ansQuVbox);

        //Bind the width of the sideMenu to take up 45% space of the content Pane it is on.
        sideMenu.prefWidthProperty().bind(content.widthProperty().multiply(0.45));
        //Bind the visibility of the paceVotePane to the visibility of the sideMenu to auto-hide when
        //sideMenu is visible
        paceVotePane.visibleProperty().bind(sideMenu.visibleProperty().not());

        //Make the children in the ScrollPane fill the width of said ScrollPane
        ansQuVbox.setFillWidth(true);
        unAnsQuVbox.setFillWidth(true);
    }

    /**
     * This method is run through the initialise method in ModeratorView.
     *
     * @param paceBar    The StackPane used to display the pace of the lecture
     * @param paceCursor The ImageView used to display the current pace of the lecture
     */
    public static void modStartUpProperties(StackPane paceBar, ImageView paceCursor) {
        //Obtain the coordinates of the pace bar and pace cursor
        Bounds paceBarBounds = paceBar.getBoundsInParent();
        Bounds paceCursorBounds = paceCursor.getBoundsInParent();

        double imageSize = paceCursorBounds.getMaxY() - paceCursorBounds.getMinY();

        //Place the pace cursor at the middle of the pace bar
        paceCursor.setTranslateY(0.5 * paceBarBounds.getHeight() - imageSize / 2);
    }

    /**
     * Bind the management and visible property of a node.
     *
     * @param node  The node of which the managed property and visible property should be bound
     */
    private static void hideEntity(Node node) {
        node.managedProperty().bind(node.visibleProperty());
        node.setVisible(false);
    }
}
