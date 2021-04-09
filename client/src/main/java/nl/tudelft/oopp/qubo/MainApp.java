package nl.tudelft.oopp.qubo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.qubo.communication.PaceVoteCommunication;
import nl.tudelft.oopp.qubo.communication.PollCommunication;
import nl.tudelft.oopp.qubo.communication.QuestionBoardCommunication;
import nl.tudelft.oopp.qubo.communication.QuestionCommunication;
import nl.tudelft.oopp.qubo.communication.QuestionVoteCommunication;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceType;
import nl.tudelft.oopp.qubo.dtos.pacevote.PaceVoteCreationDto;
import nl.tudelft.oopp.qubo.dtos.poll.PollDetailsDto;
import nl.tudelft.oopp.qubo.dtos.polloption.PollOptionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionCreationDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationDto;
import nl.tudelft.oopp.qubo.dtos.questionvote.QuestionVoteCreationDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * The startup file.
 */
public class MainApp {

    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    /**
     * This method displays currently implemented functionality.
     * It displays the working windows and views of the client.
     * It displays the working endpoints between the server and client.
     *
     * @param args Parameters passed by the user.
     */
    public static void main(String[] args) {
        //Display the application homepage
        SceneDisplay.main(new String[0]);
    }
}
