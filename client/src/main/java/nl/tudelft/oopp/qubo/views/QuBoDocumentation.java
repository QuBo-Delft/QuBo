package nl.tudelft.oopp.qubo.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuBoDocumentation {

    /**
     * This method displays the documentation for either moderators or students.
     *
     * @param isStudentView     Indicates whether this documentation is for moderators or students.
     */
    public static void display(boolean isStudentView) {
        Stage window = new Stage();

        // Block the user from performing other actions
        window.initModality(Modality.APPLICATION_MODAL);
        window.setHeight(700);
        window.setWidth(870);

        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox(15);
        layout.getStyleClass().add("layout");

        // Add the explanation HBoxes of icons to the layout
        layout.getChildren().add(getHBoxOf("status_open",
                "It indicates that this question board is currently open."));
        layout.getChildren().add(getHBoxOf("status_closed",
                "It indicates that this question board is currently closed."));
        layout.getChildren().add(getHBoxOf("status_scheduled",
                "It indicates that this question board will open as scheduled."));

        if (!isStudentView) {
            window.setTitle("QuBo Documentation for Moderators");
            layout.getChildren().add(getHBoxOf("triangle_pace_bar",
                    "It indicates the pace of this lecture as students experienced, "
                            + "the higher it locates the faster the lecture is experienced."));
        }

        if (isStudentView) {
            window.setTitle("QuBo Documentation for Students");
            layout.getChildren().add(getHBoxOf("btn_ask",
                    "Click it to post a question on this question board."));
        }

        layout.getChildren().add(getHBoxOf("upvote_true",
                "Click it to upvote a question, click again to cancel the previous upvote."));
        layout.getChildren().add(getHBoxOf("upvote_false",
                "It indicates that this question has not been voted by the user."));

        layout.getChildren().add(getHBoxOf("options",
                "Click it to open the option menu for the question."));
        layout.getChildren().add(getHBoxOf("delete", "Click it to delete this question."));
        layout.getChildren().add(getHBoxOf("edit", "Click it to edit this question."));

        if (!isStudentView) {
            layout.getChildren().add(getHBoxOf("ban",
                    "Click it to ban the student who posted this question from the question board."));
            layout.getChildren().add(getHBoxOf("mark_as_answered",
                    "Click it to mark this question as answered."));
            layout.getChildren().add(getHBoxOf("reply", "Click it to answer this question."));
        }

        layout.getChildren().add(getHBoxOf("board_details",
                "Click it to display the details of this question board."));

        if (isStudentView) {
            layout.getChildren().add(getHBoxOf("help",
                    "Click it to display the guide of using this question board as a student."));
        }

        if (!isStudentView) {
            layout.getChildren().add(getHBoxOf("help",
                    "Click it to display the guide of using this question board as a moderator."));
        }

        layout.getChildren().add(getHBoxOf("hamburger",
                "Click it to open the additional option menu on the right side of the window."));
        layout.getChildren().add(getHBoxOf("close_sidebar",
                "Click it to hide the additional option menu."));

        layout.getChildren().add(getHBoxOf("ans_q",
                "Click it to open a section that displays all answered questions, "
                        + "click again to close this section."));
        layout.getChildren().add(getHBoxOf("polls",
                "Click it to open a section that displays the details of a poll, "
                        + "click again to close this section."));

        if (!isStudentView) {
            layout.getChildren().add(getHBoxOf("btn_export",
                    "Click it to export a list of most important questions of this lecture to a text file."));
        }

        layout.getChildren().add(getHBoxOf("leave",
                "Click it to choose to leave this question board."));

        if (!isStudentView) {
            layout.getChildren().add(getHBoxOf("btn_closeBoard",
                    "Click it to close this question board."));
        }

        HBox bottomHBox = new HBox(20);
        Region r3 = new Region();
        Region r4 = new Region();
        bottomHBox.getChildren().addAll(r4, returnButton, r3);
        bottomHBox.setMinHeight(Region.USE_PREF_SIZE);
        bottomHBox.getStyleClass().add("bottomBox");

        layout.getChildren().add(bottomHBox);
        // Center all components
        layout.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout);
        Scene scene = new Scene(scrollPane);
        scene.getStylesheets().add("/css/QuBoDocumentation.css");

        // Display the dialog
        window.setScene(scene);
        // Can return back only if the current dialog is closed
        window.showAndWait();
    }

    /**
     * This method return an HBox that contains the icon image and the explanation of this icon.
     *
     * @param path  The main identifying path of the icon.
     * @param info  The explanation.
     * @return The HBox with the icon image and explanation.
     */
    private static HBox getHBoxOf(String path, String info) {
        // Get the icon image
        String augmentedPath = "/icons/" + path + ".png";
        ImageView i = new ImageView(new Image(augmentedPath));
        i.setFitHeight(50);
        i.setFitWidth(50);
        i.getStyleClass().add("image-group");

        HBox box = new HBox(30);
        Region r1 = new Region();
        Region r2 = new Region();

        Label msg = new Label(info);
        VBox msgBox = new VBox();
        msgBox.getChildren().add(msg);
        msgBox.getStyleClass().add("msgBox");

        box.getChildren().addAll(i, r1, msgBox, r2);
        box.setMinHeight(Region.USE_PREF_SIZE);
        box.getStyleClass().add("hBox-group");

        return box;
    }
}
