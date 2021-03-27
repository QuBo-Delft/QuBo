package nl.tudelft.oopp.demo.sceneloader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.controllers.CreateQuBoController;
import nl.tudelft.oopp.demo.controllers.ModeratorViewController;
import nl.tudelft.oopp.demo.controllers.QuBoCodesController;
import nl.tudelft.oopp.demo.controllers.StudentViewController;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.demo.views.AlertDialog;

import java.io.IOException;
import java.util.UUID;

public class SceneLoader {


    /**
     * This method aims to load the page used to create question boards.
     *
     * @param currentStage    The current stage that is displayed on-screen.
     *
     */
    public static void loadCreateQuBo(Stage currentStage) {
        // Create an FXMLLoader of CreateQuBo.fxml
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/fxmlsheets/CreateQuBo.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the controller of CreateQuBo
        CreateQuBoController controller = loader.getController();

        // Check if root is null
        if (root == null) {
            AlertDialog.display("", "Unable to display the create question board form");
            return;
        }

        // Display the new scene
        currentStage.setScene(new Scene(root));
        currentStage.setTitle("Create Question Board");
    }

    /**
     * This method aims to load the page that displays the student code and moderator code.
     *
     * @param qc    The QuestionBoardCreationDto object to be transferred to the controller
     *              of QuestionBoardCodes.
     *
     */
    public static void loadQuestionBoardCodes(QuestionBoardCreationDto qc, Stage currentStage) {
        // Create an FXMLLoader of QuBoCodes.fxml
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/fxmlsheets/QuBoCodes.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the controller of QuBoCodes
        QuBoCodesController controller = loader.getController();

        // Transfer the data for QuBoCodes
        controller.displayCodes(qc);

        // Check if root is null
        if (root == null) {
            AlertDialog.display("", "Unable to display the codes");
            return;
        }

        // Display the new scene
        currentStage.setScene(new Scene(root));
        currentStage.setTitle("Created Question Board");
    }

    /**
     * This method aims to load the page that displays the student view of a question board.
     *
     * @param qd    The QuestionBoardDetailsDto object that brings data for the
     *              student view of a question board.
     */
    public static void loadStudentView(QuestionBoardDetailsDto qd, Stage currentStage) {
        // Create an FXMLLoader of StudentView.fxml
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/fxmlsheets/StudentView.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the controller of StudentView
        StudentViewController controller = loader.getController();

        UUID boardId = qd.getId();

        // TODO: need a method to update data in studentView

        // Check if root is null
        if (root == null) {
            AlertDialog.display("", "Unable to display the student view");
            return;
        }

        //Display the new scene
        currentStage.setScene(new Scene(root));
        currentStage.setTitle(qd.getTitle());
    }

    /**
     * This method loads the moderator view of the question board associated with the QuestionBoardDetailsDto
     * passed to the method.
     *
     * @param qd  The QuestionBoardDetailsDto object associated with the question board that the moderator
     *      wants to join.
     */
    public static void loadModeratorView(QuestionBoardDetailsDto qd, Stage currentStage) {
        // Create an FXMLLoader of ModeratorView.fxml
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/fxmlsheets/ModeratorView.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get the controller of ModeratorView
        ModeratorViewController controller = loader.getController();

        UUID boardId = qd.getId();

        // TODO: need a method to update data in moderatorView

        //Check if the root is null
        if (root == null) {
            AlertDialog.display("", "Unable to display the moderator view");
            return;
        }

        //Display the new scene
        currentStage.setScene(new Scene(root));
        currentStage.setTitle(qd.getTitle() + " - Moderator");
    }

    /**
     * This method aims to load the JoinQuBo homepage and close the current stage.
     *
     * @param currentStage    The stage of the scene where the method is called
     */
    public static void backToHome(Stage currentStage) {
        //Create an FXMLLoader of JoinQuBo.fxml
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/fxmlsheets/JoinQuBo.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check if root is null
        if (root == null) {
            AlertDialog.display("", "Unable to display the homepage");
            return;
        }

        currentStage.setScene(new Scene(root));
        currentStage.setTitle("Join Question Board");
    }

}
