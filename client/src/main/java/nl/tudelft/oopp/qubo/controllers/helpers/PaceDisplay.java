package nl.tudelft.oopp.qubo.controllers.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import nl.tudelft.oopp.qubo.communication.PaceVoteCommunication;
import nl.tudelft.oopp.qubo.dtos.pace.PaceDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;

import java.util.UUID;

/**
 * The Pace display.
 */
public class PaceDisplay {

    //Used for converting JSON responses to their appropriate DTOs.
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();
    //Used for storing the previous pace bar modifier
    private static double paceBarMod = Integer.MIN_VALUE;

    /**
     * Resets the pace bar modifier. Called when the user leaves a question board.
     *
     */
    public static void resetPaceBarMod() {
        paceBarMod = Integer.MIN_VALUE;
    }

    /**
     * This method displays the pace of the lecture as perceived by students.
     *
     * @param quBo       The details of the question board whose pace should be displayed.
     * @param modCode    The moderator code of the question board.
     * @param paceBar    The stack pane used to display the pace bar.
     * @param paceCursor The image view used to display the current pace of the lecture.
     */
    public static void displayPace(QuestionBoardDetailsDto quBo, UUID modCode,
                                   StackPane paceBar, ImageView paceCursor) {
        //If the question board is null, return
        if (quBo == null) {
            return;
        }

        //Retrieve the pace details from the server
        String jsonPace = PaceVoteCommunication.getAggregatedPaceVotes(quBo.getId(), modCode);
        PaceDetailsDto pace;

        //If the jsonPace was not null, use the server's pace results
        //If the jsonPace was null, set the just right votes to 1 to center the pace cursor
        if (jsonPace != null) {
            pace = gson.fromJson(jsonPace, PaceDetailsDto.class);
        } else {
            pace = new PaceDetailsDto();
            pace.setJustRightVotes(1);
            pace.setTooFastVotes(0);
            pace.setTooSlowVotes(0);
        }

        //Calculate the pace bar modifier
        double paceBarModifier = calculatePace(pace.getTooFastVotes(), pace.getJustRightVotes(),
                pace.getTooSlowVotes());

        //Move the pace bar cursor
        movePaceCursor(paceBar, paceCursor, paceBarModifier);
    }

    /**
     * This method calculates the average pace. As JavaFX uses a coordinate system with the origin at the
     * top left of the screen, this method assigns a higher value to the tooSlowVotes and a lower value to
     * the tooFastVotes. This ensures that the double returned will transform the cursor appropriately.
     *
     * @param tooFast   The amount of too fast votes.
     * @param justRight The amount of just right votes.
     * @param tooSlow   The amount of too slow votes.
     * @return A double between 0 and 1.
     */
    private static double calculatePace(double tooFast, double justRight, double tooSlow) {
        //If two values are equal to 0, return 0, 1, or 0.5 depending on the pace type.
        if (tooFast == 0 && tooSlow == 0 || tooFast == tooSlow) {
            return 0.5;
        } else if (justRight == 0 && tooFast == 0) {
            return 1;
        } else if (justRight == 0 && tooSlow == 0) {
            return 0;
        }

        double location;

        //If there were no too slow votes, return the fraction that the too fast votes contributed.
        if (tooSlow == 0) {
            location = 0.5 - (0.5 * tooFast) / (tooFast + justRight);

        //If there were no too fast votes, return the fraction that the too slow votes contributed.
        } else if (tooFast == 0) {
            location = 0.5 + (0.5 * tooSlow) / (tooSlow + justRight);

        //Calculate the relative fractions of the too slow and too fast votes and return the combined fractions.
        } else {
            double lowLoc = 0.5  - tooSlow / (tooFast + tooSlow + justRight);
            location = 1 - (tooFast / (tooFast + tooSlow + justRight));

            location -= lowLoc;
        }
        return location;
    }

    /**
     * This method calculates the new position of the pace cursor and moves the cursor to this position.
     *
     * @param paceBar           The stack pane used to display the pace bar.
     * @param paceCursor        The image view used to display the current pace of the lecture.
     * @param paceBarModifier   The modifier used to calculate the new position of the pace cursor.
     */
    private static void movePaceCursor(StackPane paceBar, ImageView paceCursor, double paceBarModifier) {
        //Check if the average pace has changed
        if (paceBarModifier == paceBarMod) {
            return;
        }
        paceBarMod = paceBarModifier;

        //Calculate the current position of the pace cursor
        Bounds paceBarBounds = paceBar.getBoundsInParent();
        Bounds paceCursorBounds = paceCursor.getBoundsInParent();

        double paceBarHeight = paceBarBounds.getHeight();
        double newPosition = paceBarModifier * paceBarHeight - 0.5 * paceBarHeight;

        //Make sure the pace cursor stays inside of the window.
        if (paceBarModifier == 0) {
            newPosition += paceCursorBounds.getHeight() / 2 - 4;
        } else if (paceBarModifier == 1) {
            newPosition -= paceCursorBounds.getHeight() / 2 - 0.4;
        }

        //Set up the transition.
        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.5), paceCursor);
        translate.setFromY(paceCursor.getY());
        translate.setToY(newPosition);

        //Move the cursor and set its new Y coordinate attributes.
        translate.play();
        paceCursor.setLayoutY(translate.getToY());
        paceCursor.setY(translate.getToY());
        paceCursor.setTranslateY(0);
    }
}
