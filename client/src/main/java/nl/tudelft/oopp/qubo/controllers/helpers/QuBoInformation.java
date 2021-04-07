package nl.tudelft.oopp.qubo.controllers.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.tudelft.oopp.qubo.communication.QuestionBoardCommunication;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.views.AlertDialog;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * This class handles setting board information for the student and moderator views.
 */
public class QuBoInformation {
    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    /**
     * Sets the board details of the respective view by setting appropriate values to the input
     * parameters.
     *
     * @param quBo  The Question BoardDetailsDto.
     * @param icon  The board status icon (red/yellow/green).
     * @param text  The status text.
     * @param title The board title.
     */
    public static void setBoardDetails(QuestionBoardDetailsDto quBo, ImageView icon, Label text, Label title) {
        // Sets the board title
        title.setText(quBo.getTitle());
        displayBoardStatus(quBo, icon, text);
    }

    /**
     * Sets the board status and changes the display accordingly.
     *
     * @param quBo  The Question BoardDetailsDto.
     * @param icon  The board status icon (red/yellow/green).
     * @param text  The status text.
     */
    public static void displayBoardStatus(QuestionBoardDetailsDto quBo, ImageView icon, Label text) {
        String iconImage;

        if (quBo.isClosed()) {
            // Sets the board icon to be a closed indicator (red), board is closed
            iconImage = "status_closed";
            // Sets the following text below the board's title
            text.setText("Question board is closed, making changes is no longer possible ");
        } else if (quBo.getStartTime().toLocalDateTime().isAfter(LocalDateTime.now())) {
            // Sets the board icon to be a scheduled indicator (yellow), board not open yet
            iconImage = "status_scheduled";
            // Calls the getTimeText method to determine which text to display below the board's title
            text.setText(getTimeText(quBo));
        } else {
            // Sets the board icon to be a scheduled indicator (green), board is open
            iconImage = "status_open";
            // Calls the getTimeText method to determine which text to display below the board's title
            text.setText(getTimeText(quBo));
        }
        // Sets the actual board icon, the path is relative to the resources folder
        icon.setImage(new Image("/icons/" + iconImage + ".png"));
    }

    /**
     * Sends a request to the server to retrieve the new board details and display them.
     *
     * @param quBo  The Question BoardDetailsDto.
     * @param icon  The board status icon (red/yellow/green).
     * @param text  The status text.
     */
    public static QuestionBoardDetailsDto refreshBoardStatus(QuestionBoardDetailsDto quBo,
                                                             ImageView icon, Label text) {
        String response = QuestionBoardCommunication.retrieveQuestions(quBo.getId());
        QuestionBoardDetailsDto newQuBo = gson.fromJson(response, QuestionBoardDetailsDto.class);

        if (response == null) {
            AlertDialog.display("Unsuccessful request", "Could not fetch board details.");
            return quBo;
        } else {
            displayBoardStatus(quBo, icon, text);
            return newQuBo;
        }
    }

    /**
     * Gets the correct text to display.
     *
     * @param quBo The QuestionBoardDetailsDto which start time must be used.
     * @return The correct text to display.
     */
    public static String getTimeText(QuestionBoardDetailsDto quBo) {

        // Gets the start date and time of the Question Board
        ZonedDateTime startTime = quBo.getStartTime().toInstant().atZone(ZoneId.systemDefault());
        // Gets today's date and time
        ZonedDateTime today = Instant.now().atZone(ZoneId.systemDefault());
        // Calculates yesterday at 00:00 relative to the Question Board's start time
        ZonedDateTime relativeYesterday =
            Instant.now().minus(1, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).withHour(0);
        // Gets tomorrow at 23:59 relative to the Question Board's start time
        ZonedDateTime relativeTomorrow =
            Instant.now().plus(1, ChronoUnit.DAYS).atZone(ZoneId.systemDefault())
                .withHour(23).withMinute(59);

        // Formats the date and time like: '12:00'
        DateTimeFormatter formatterT =
            DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());
        // Formats the date and time like: 'monday 12:00'
        DateTimeFormatter formatterW =
            DateTimeFormatter.ofPattern("eeee HH:mm").withZone(ZoneId.systemDefault());
        // Formats the date and time like: '17-11 12:00'
        DateTimeFormatter formatterD =
            DateTimeFormatter.ofPattern("dd-MM HH:mm").withZone(ZoneId.systemDefault());

        // The entire text to be displayed and returned
        String displayDate;
        // The sentence that either states when the Question Board will open or states since when it is
        String openText;
        // Text that, if used, displays tomorrow or yesterday depending on whether the Question Board is open
        String openDay;
        // The days between the question board's start time and today
        long daysBetween;
        // The relative day that should be used depending on whether the Question Board is open
        ZonedDateTime relativeDay;

        if (startTime.isBefore(today)) {
            openText = "The Question Board has been open since ";
            openDay = " yesterday";
            daysBetween = ChronoUnit.DAYS.between(startTime, today);
            relativeDay = relativeYesterday;
        } else {
            openText = "The Question Board will open at ";
            openDay = " tomorrow";
            daysBetween = ChronoUnit.DAYS.between(today, startTime);
            relativeDay = relativeTomorrow;
        }

        if (today.getDayOfYear() == startTime.getDayOfYear()) {
            // If the Question Board opened or will open today we only show the time (12:00)
            displayDate = openText + formatterT.format(startTime);
        } else if (startTime.isBefore(relativeDay) || startTime.equals(relativeDay)) {
            // If the Question Board opened or will open within a day of today we will show
            // tomorrow or yesterday additionally (12:00 tomorrow/yesterday)
            displayDate = openText + formatterT.format(startTime) + openDay;
        } else if (daysBetween < 7) {
            // If the Question Board opened or will open within 6 days of today we will show
            // the weekday with time (monday 12:00)
            displayDate = openText + formatterW.format(startTime).toLowerCase();
        } else {
            // If the Question Board is not opened or will not open within 6 days we will show,
            // the date with time (17-11 12:00)
            displayDate = openText + formatterD.format(startTime);
        }
        return displayDate;
    }

    /**
     * Method that returns whether the board and it's functions should be usable by students.
     *
     * @param quBo The question board that is checked.
     * @return True or false depending on whether students are allowed to make changes
     */
    public static boolean isQuBoClosed(QuestionBoardDetailsDto quBo) {
        if (quBo.isClosed()) {
            AlertDialog.display("Action blocked",
                "This Question Board has been closed by its moderators.");
            return true;
        }
        if (quBo.getStartTime().toLocalDateTime().isAfter(LocalDateTime.now())) {
            AlertDialog.display("Action limited", getTimeText(quBo) + ".");
            return true;
        }
        return false;
    }
}
