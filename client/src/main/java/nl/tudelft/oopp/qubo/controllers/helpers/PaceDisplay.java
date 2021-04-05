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
        pace = gson.fromJson(jsonPace, PaceDetailsDto.class);

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
            location = tooFast / (tooFast + justRight);
            location = 0.5 - 0.5 * location;

        //If there were no too fast votes, return the fraction that the too slow votes contributed.
        } else if (tooFast == 0) {
            location = tooSlow / (tooSlow + justRight);
            location = 1 - (0.5 + 0.5 * location);

        //Calculate the relative fractions of the too slow and too fast votes and return the combined fractions.
        } else {
            double lowLoc = tooFast / (tooFast + tooSlow + justRight);
            lowLoc = 0.5 - lowLoc;
            location = tooSlow / (tooFast + tooSlow + justRight);
            location = 1 - location;

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
        //Get the local bounds of the pace bar and its cursor relative to the pace bar and its parent
        Bounds paceBarBounds = paceBar.getBoundsInParent();
        Bounds paceCursorBounds = paceCursor.getBoundsInParent();

        //Calculate the new position of the pace cursor
        double paceBarHeight = paceBarBounds.getHeight();
        double imageSize = paceCursorBounds.getMaxY() - paceCursorBounds.getMinY();
        double adjustTranslation = imageSize / 2 + paceCursorBounds.getMinY();

        double newPosition = paceBarModifier * paceBarHeight - adjustTranslation;

        //Set up a Transition to move the cursor visibly
        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.5), paceCursor);
        translate.setFromY(paceCursor.getY());
        translate.setToY(newPosition);

        //Move the cursor
        translate.play();
    }
}
