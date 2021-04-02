package nl.tudelft.oopp.qubo.controllers;

import nl.tudelft.oopp.qubo.dtos.pace.PaceDetailsDto;

public class ModeratorViewController {

    private static void displayPace() {
        //TODO: Call endpoint to retrieve the pace details and convert it to a PaceDetailsDto
        PaceDetailsDto pace = new PaceDetailsDto();

        double paceBarModifier = calculatePace(pace);

        //TODO: Move the bar
    }

    /**
     * This method calculates the average pace.
     *
     * @param pace  The PaceDetailsDto containing integers of the number of votes per pace type.
     * @return A double between 0 and 1.
     */
    private static double calculatePace(PaceDetailsDto pace) {
        int numberOfVotes = pace.getTooSlowVotes() + pace.getJustRightVotes() + pace.getTooSlowVotes();

        //Set too slow votes equal to 1
        double tooSlow = pace.getTooSlowVotes();
        //Set just right votes equal to 1.5
        double justRight = pace.getJustRightVotes() * 1.5;
        //Set too fast votes equal to 2
        double tooFast = pace.getTooFastVotes() * 2;

        //Calculate the average pace by adding all votes (with relative weights) and dividing it by the total
        //number of votes
        double aggregatedPaceVotes = (tooSlow + justRight + tooFast) / numberOfVotes;

        //Return a double between 0 and 1
        return aggregatedPaceVotes - 1;
    }
}
