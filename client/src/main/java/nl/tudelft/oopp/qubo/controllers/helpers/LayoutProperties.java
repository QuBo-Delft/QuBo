package nl.tudelft.oopp.qubo.controllers.helpers;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LayoutProperties {
    public static void startupProperties(StackPane content, VBox sideBar, VBox sideMenu, VBox pollVbox,
                                         VBox ansQuVbox, VBox unAnsQuVbox, BorderPane paceVotePane) {
        LayoutProperties.hideEntity(sideBar);
        LayoutProperties.hideEntity(sideMenu);
        LayoutProperties.hideEntity(pollVbox);
        LayoutProperties.hideEntity(ansQuVbox);

        sideMenu.prefWidthProperty().bind(content.widthProperty().multiply(0.45));
        paceVotePane.visibleProperty().bind(sideMenu.visibleProperty().not());

        ansQuVbox.setFillWidth(true);
        unAnsQuVbox.setFillWidth(true);
    }

    private static void hideEntity(Node node) {
        node.managedProperty().bind(node.visibleProperty());
        node.setVisible(false);
    }
}
