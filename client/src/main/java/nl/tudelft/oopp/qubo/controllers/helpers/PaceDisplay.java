package nl.tudelft.oopp.qubo.controllers.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import nl.tudelft.oopp.qubo.communication.PaceVoteCommunication;
import nl.tudelft.oopp.qubo.dtos.pace.PaceDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;

import java.util.UUID;

public class PaceDisplay {

    //Used for converting JSON responses to their appropriate DTOs.
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    /**
     * This method displays the pace of the lecture as perceived by students.
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
        double paceBarModifier = calculatePace(pace);

        //Get the local bounds of the pace bar and its cursor relative to the pace bar and its parent
        Bounds paceBarBounds = paceBar.getBoundsInLocal();
        Bounds paceCursorBounds = paceCursor.getBoundsInParent();

        //Move the pace bar cursor to the appropriate height
        double paceBarHeight = paceBarBounds.getHeight();
        double imageSize = paceCursorBounds.getMaxY() - paceCursorBounds.getMinY();
        double adjustTranslation = imageSize / 2 + paceCursorBounds.getMinY();

        paceCursor.setTranslateY(paceBarModifier * paceBarHeight - adjustTranslation);
    }

    /**
     * This method calculates the average pace. As JavaFX uses a coordinate system with the origin at the
     * top left of the screen, this method assigns a higher value to the tooSlowVotes and a lower value to
     * the tooFastVotes. This ensures that the double returned will transform the cursor appropriately.
     *
     * @param pace  The PaceDetailsDto containing integers of the number of votes per pace type.
     * @return A double between 0 and 1.
     */
    private static double calculatePace(PaceDetailsDto pace) {
        int numberOfVotes = pace.getTooSlowVotes() + pace.getJustRightVotes() + pace.getTooSlowVotes();

        //If there are no pace votes, set the pace to "Just Right"
        if (numberOfVotes == 0) {
            return 0.5;
        }

        //Set too slow votes equal to 2
        double tooSlow = pace.getTooSlowVotes() * 2;
        //Set just right votes equal to 1.5
        double justRight = pace.getJustRightVotes() * 1.5;
        //Set too fast votes equal to 1
        double tooFast = pace.getTooFastVotes();

        //Calculate the average pace by adding all votes (with relative weights) and dividing it by the total
        //number of votes
        double aggregatedPaceVotes = (tooSlow + justRight + tooFast) / numberOfVotes;

        //Return a double between 0 and 1
        return aggregatedPaceVotes - 1;
    }
}
