package nl.tudelft.oopp.qubo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardCreationBindingModel;

import java.sql.Timestamp;
import java.time.Instant;

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
