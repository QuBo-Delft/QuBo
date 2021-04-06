package nl.tudelft.oopp.qubo.sceneloader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.tudelft.oopp.qubo.controllers.QuBoCodesController;
import nl.tudelft.oopp.qubo.controllers.QuBoDetailsController;
import nl.tudelft.oopp.qubo.controllers.StudentViewController;
import nl.tudelft.oopp.qubo.controllers.ModeratorViewController;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.views.AlertDialog;
import nl.tudelft.oopp.qubo.views.ConfirmationDialog;

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
    private static UUID modCode;

    /**
     * Set when loading a student view. Used to possibly remove a set pace vote
     * when attempting to leave a student view.
     */
    private static StudentViewController controllerS;

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
     * @param code      The moderator code of the board
     */
    public void viewLoader(QuestionBoardDetailsDto qdI, Stage stage, String userNameI,
                           String fxml, UUID code) {
        qd = qdI;
        userName = userNameI;
        modCode = code;

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
        stage = rootValid(loader, stage, fxml);
        // Set values to the fxml controller if necessary
        setController(fxml, loader);
        // Set the title of the stage
        setTitle(fxml, stage);
        // Set the close method of the stage
        setCloseMethod(fxml, stage);
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
    private static Stage rootValid(FXMLLoader loader, Stage stage, String fxml) {
        Scene newScene = null;

        // Check if file can be loaded
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (newScene == null) {
            AlertDialog.display("", "Unable to display the requested view");
            return stage;
        }

        switch (fxml) {
            case "StudentView":
            case "ModeratorView":
                // Create new stage and set new scene
                Stage newStage = new Stage();
                newStage.setScene(newScene);

                // Set minimal resize resolution
                newStage.setMinHeight(550);
                newStage.setMinWidth(850);
                // Close current stage and show new stage
                stage.close();
                newStage.show();

                return newStage;
            case "JoinQuBo":
                stage.setMinWidth(Double.MIN_VALUE);
                stage.setMinHeight(Double.MIN_VALUE);
                break;
            case "QuBoDetails":
                stage.show();
                break;
            default:
                break;
        }

        stage.setScene(newScene);
        stage.centerOnScreen();
        return stage;
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
                // Get controller and initialize qb
                ModeratorViewController controllerM = loader.getController();
                loader.setController(controllerM);
                controllerM.setQuBo(qd);
                controllerM.setModCode(modCode);
                controllerM.setAuthorName(userName);
                controllerM.setBoardDetails();

                controllerM.refresh();
                break;
            case ("StudentView"):
                // Get controller and initialize qb
                controllerS = loader.getController();
                loader.setController(controllerS);
                controllerS.setQuBo(qd);
                controllerS.setAuthorName(userName);
                controllerS.setBoardDetails();

                controllerS.refresh();
                break;
            case ("QuBoDetails"):
                QuBoDetailsController controllerD = loader.getController();
                loader.setController(controllerD);
                controllerD.setDetails(qd, modCode);
                break;
            default:
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
            case ("JoinQuBo"):
                stage.setTitle("QuBo");
                break;
            case ("QuBoDetails"):
                stage.setTitle("Details");
                break;
            default:
                break;
        }
    }

    /**
     * Sets the correct close method for the scene that is to be displayed.
     *
     * @param fxml  The fxml sheet to be loaded.
     * @param stage The stage of the scene on which the method is called.
     */
    public static void setCloseMethod(String fxml, Stage stage) {
        String message;

        switch (fxml) {
            case "JoinQuBo":
                message = "";
                stage.setOnCloseRequest(e -> closeMethodHelper(e, message));
                break;
            case ("CreateQuBo"):
                message = "\n'Cancel' will return you to the homepage";
                stage.setOnCloseRequest(e -> closeMethodHelper(e, message));
                break;
            case ("QuBoCodes"):
                message = "\nMake sure you have copied the question board codes!";
                stage.setOnCloseRequest(e -> closeMethodHelper(e, message));
                break;
            case ("StudentView"):
            case ("ModeratorView"):
                message = "\nYou can return to the homepage by pressing the designated button at the"
                        + " bottom of the sidebar";
                stage.setOnCloseRequest(e -> closeMethodHelper(e, message));
                break;
            default:
                break;
        }
    }

    private static void closeMethodHelper(WindowEvent windowEvent, String message) {
        boolean close = ConfirmationDialog.display("Close Application",
                "Are you sure you wish to close the application?\n" + message);

        if (!close) {
            windowEvent.consume();
        } else {
            if (controllerS != null && controllerS.getPaceVoteCreationDto() != null) {
                controllerS.deletePaceVote(true);
            }
        }
    }
}
