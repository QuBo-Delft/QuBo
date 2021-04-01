package nl.tudelft.oopp.qubo.sceneloader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.qubo.controllers.ModeratorViewController;
import nl.tudelft.oopp.qubo.controllers.QuBoCodesController;
import nl.tudelft.oopp.qubo.controllers.StudentViewController;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.views.AlertDialog;

import java.io.IOException;
import java.util.UUID;

/**
 * This class is used for scene and stage loading and generating when switching between fxml sheets.
 */
public class SceneLoader {

    /**
     * These variables get set when a method that uses them is called.
     */
    private static QuestionBoardDetailsDto qd;
    private static QuestionBoardCreationDto qc;
    private static String userName;

    /**
     * This method aims to load all scenes that do not require input details.
     *
     * @param stage The stage of the scene where the method is called.
     * @param fxml  The fxml sheet to be loaded.
     */
    public static void defaultLoader(Stage stage, String fxml) {
        // Start preparing the new scene
        startLoading(stage, fxml);
    }

    /**
     * This method aims to load the page that displays the student code and moderator code.
     *
     * @param qcI   The QuestionBoardCreationDto object to be transferred.
     * @param stage The stage of the scene where the method is called.
     */
    public static void loadQuBoCodes(QuestionBoardCreationDto qcI, Stage stage) {
        qc = qcI;
        // Start preparing the new scene
        startLoading(stage, "QuBoCodes");
    }

    /**
     * This method aims to load the page that displays either the student view or the moderator view.
     *
     * @param qdI       The QuestionBoardDetailsDto object to be transferred.
     * @param stage     The stage of the scene where the method is called.
     * @param userNameI The username to be transferred.
     * @param fxml      The fxml sheet to be loaded.
     */
    public void viewLoader(QuestionBoardDetailsDto qdI, Stage stage, String userNameI, String fxml) {
        qd = qdI;
        userName = userNameI;
        // Start preparing the new scene
        startLoading(stage, fxml);
    }

    /**
     * This method calls the necessary private methods to instantiate a scene with its proper details.
     *
     * @param stage The stage of the scene where the method is called.
     * @param fxml  The fxml sheet to be loaded.
     */
    private static void startLoading(Stage stage, String fxml) {
        // Create an FXMLLoader of the input fxml
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/fxmlsheets/" + fxml + ".fxml"));
        // Check whether the root is valid
        rootValid(loader, stage, fxml);
        // Set values to the fxml controller if necessary
        setController(fxml, loader);
        // Set the title of the stage
        setTitle(fxml, stage);
    }

    /**
     * This method checks whether the root is valid and whether a proper scene can be generated.
     * When this method is called for a student or moderator view, it creates a new stage.
     * In all other cases it replaces the currently displayed scene by a new one.
     *
     * @param loader    The loader that is used to locate and read the fxml sheet.
     * @param stage     The stage of the scene where the method is called.
     * @param fxml      The fxml sheet to be loaded.
     */
    private static void rootValid(FXMLLoader loader, Stage stage, String fxml) {
        if (fxml.equals("StudentView") || fxml.equals("ModeratorView")) {
            Stage newStage = new Stage();
            Scene newScene = null;
            // Check if file can be loaded
            try {
                newScene = new Scene(loader.load());
                newStage.setScene(newScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (newScene == null) {
                AlertDialog.display("", "Unable to display the requested view");
                return;
            }
            // Close current stage and show new stage
            stage.close();
            newStage.setMinHeight(550);
            newStage.setMinWidth(850);
            newStage.show();
        } else {
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (root == null) {
                AlertDialog.display("", "Unable to display the requested view");
                return;
            }
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        }
    }

    /**
     * Sets necessary values at controllers for some scenes.
     *
     * @param fxml      The fxml sheet to be loaded.
     * @param loader    The loader that is used to locate and read the fxml sheet.
     */
    private static void setController(String fxml, FXMLLoader loader) {
        switch (fxml) {
            case ("QuBoCodes"):
                QuBoCodesController controllerC = loader.getController();
                controllerC.displayCodes(qc);
                break;
            case ("ModeratorView"):
                ModeratorViewController controllerM = loader.getController();
                loader.setController(controller);
                controllerM.setQuBo(qd);
                controllerM.setModCode(modCode);
                controllerM.setAuthorName(userName);
                controllerM.setBoardDetails();
                // TODO: need a method to update data in moderatorView
                break;
            case ("StudentView"):
                // Get controller and initialize qb
                StudentViewController controllerS = loader.getController();
                loader.setController(controllerS);
                controllerS.setQuBo(qd);
                controllerS.setAuthorName(userName);
                controllerS.setBoardDetails();
                break;
            default:
                break;
        }
    }

    /**
     * Sets the correct title for the scene that is to be displayed.
     *
     * @param fxml  The fxml sheet to be loaded.
     * @param stage The stage of the scene where the method is called.
     */
    private static void setTitle(String fxml, Stage stage) {
        switch (fxml) {
            case ("StudentView"):
                stage.setTitle(qd.getTitle());
                break;
            case ("ModeratorView"):
                stage.setTitle(qd.getTitle() + " - Moderator");
                break;
            case ("QuBoCodes"):
                stage.setTitle("Created Question Board");
                break;
            case ("CreateQuBo"):
                stage.setTitle("Create Question Board");
                break;
            default:
                stage.setTitle("QuBo");
                stage.setMinWidth(Double.MIN_VALUE);
                stage.setMinHeight(Double.MIN_VALUE);
                break;
        }
    }
}
