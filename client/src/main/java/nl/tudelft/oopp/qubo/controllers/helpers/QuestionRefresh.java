package nl.tudelft.oopp.qubo.controllers.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.qubo.communication.QuestionBoardCommunication;
import nl.tudelft.oopp.qubo.controllers.ModeratorViewController;
import nl.tudelft.oopp.qubo.controllers.StudentViewController;
import nl.tudelft.oopp.qubo.controllers.structures.QuestionItem;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.utilities.sorting.Sorting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The Question refreshing helper.
 */
public class QuestionRefresh {
    private static StudentViewController thisStuController;
    private static ModeratorViewController thisModController;

    private static QuestionBoardDetailsDto thisQuBoId;
    
    private static QuestionDetailsDto[] answeredQuestions;
    private static QuestionDetailsDto[] unansweredQuestions;

    private static VBox unAnsQuVbox;
    private static VBox ansQuVbox;

    private static HashMap<UUID, UUID> secretCodeMap;
    private static HashMap<UUID, UUID> upvoteMap;

    private static ScrollPane unAnsQuScPane;
    private static ScrollPane ansQuScPane;

    private static UUID modCode = null;

    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    /**
     * This method takes in the information and nodes to be able to refresh the question lists for students.
     *
     * @param controller      Controller of the student view.
     * @param quBo            QuestionBoardDetailsDto of the board.
     * @param unAnsVbox       VBox containing the list of unanswered questions.
     * @param ansVbox         VBox containing the list of answered questions.
     * @param upvote          HashMap of questionId:upvoteId.
     * @param secret          HashMap of questionId:secretCode.
     * @param unAnsScrollPane ScrollPane containing the VBox that contains the list of unanswered questions.
     * @param ansScrollPane   ScrollPane containing the VBox that contains the list of answered questions.
     */
    public static void studentRefresh(StudentViewController controller,
                                      QuestionBoardDetailsDto quBo, VBox unAnsVbox, VBox ansVbox, HashMap<UUID,
                                      UUID> upvote, HashMap<UUID, UUID> secret, ScrollPane unAnsScrollPane,
                                      ScrollPane ansScrollPane) {
        thisStuController = controller;
        thisModController = null;

        thisQuBoId = quBo;

        unAnsQuVbox = unAnsVbox;
        ansQuVbox = ansVbox;

        upvoteMap = upvote;
        secretCodeMap = secret;

        unAnsQuScPane = unAnsScrollPane;
        ansQuScPane = ansScrollPane;

        modCode = null;

        displayQuestions();
    }

    /**
     * This method takes in the information and nodes to be able to refresh the question lists for mods.
     *
     * @param controller      Controller of the moderator view.
     * @param quBo            QuestionBoardDetailsDto of the board.
     * @param code            Moderator code of the board.
     * @param unAnsVbox       VBox containing the list of unanswered questions.
     * @param ansVbox         VBox containing the list of answered questions.
     * @param upvote          HashMap of questionId:upvoteId.
     * @param unAnsScrollPane ScrollPane containing the VBox that contains the list of unanswered questions.
     * @param ansScrollPane   ScrollPane containing the VBox that contains the list of answered questions.
     */
    public static void modRefresh(ModeratorViewController controller, QuestionBoardDetailsDto quBo, UUID code,
                                  VBox unAnsVbox, VBox ansVbox,
                                  HashMap<UUID, UUID> upvote, ScrollPane unAnsScrollPane,
                                  ScrollPane ansScrollPane) {
        thisStuController = null;
        thisModController = controller;

        thisQuBoId = quBo;

        unAnsQuVbox = unAnsVbox;
        ansQuVbox = ansVbox;

        upvoteMap = upvote;

        unAnsQuScPane = unAnsScrollPane;
        ansQuScPane = ansScrollPane;

        modCode = code;

        displayQuestions();
    }

    /**
     * Method that displays the questions that are in the question board on the screen. Answered questions
     * will be sorted by the time at which they were answered, and unanswered questions will be sorted by
     * the number of upvotes they have received.
     */
    private static void displayQuestions() {
        // To be deleted in final version
        if (thisQuBoId == null) {
            divideQuestions(null);
            return;
        }
        //

        //Retrieve the questions and convert them to an array of QuestionDetailsDtos if the response is
        //not null.
        String jsonQuestions = QuestionBoardCommunication.retrieveQuestions(thisQuBoId.getId());

        if (jsonQuestions == null) {
            divideQuestions(null);
        } else {
            QuestionDetailsDto[] questions = gson.fromJson(jsonQuestions, QuestionDetailsDto[].class);

            //Divide the questions over two lists and sort them.
            divideQuestions(questions);

            sortAndMap(unAnsQuVbox, unansweredQuestions, unAnsQuScPane);
            sortAndMap(ansQuVbox, answeredQuestions, ansQuScPane);
        }
    }

    /**
     * This method will be used to divide the question list into a list of answered questions,
     * and a list of unanswered questions.
     *
     * @param questions The question array that needs to be divided.
     */
    private static void divideQuestions(QuestionDetailsDto[] questions) {
        //If there are no questions, initialise the questions lists with empty arrays and return.
        if (questions == null || questions.length == 0) {
            answeredQuestions = new QuestionDetailsDto[0];
            unansweredQuestions = new QuestionDetailsDto[0];
            return;
        }

        //Initialise two lists to contain the answered and unanswered questions.
        List<QuestionDetailsDto> answered = new ArrayList<>();
        List<QuestionDetailsDto> unanswered = new ArrayList<>();

        //Divide the questions over the two lists.
        for (QuestionDetailsDto question : questions) {
            if (question.getAnswered() != null) {
                answered.add(question);
            } else {
                unanswered.add(question);
            }
        }

        //Convert the list of answered and unanswered questions to arrays and store them in their
        //respective class attributes.
        answeredQuestions = answered.toArray(new QuestionDetailsDto[0]);
        unansweredQuestions = unanswered.toArray(new QuestionDetailsDto[0]);
    }

    private static void sortAndMap(VBox questionVbox, QuestionDetailsDto[] questionArray,
                                   ScrollPane scrollpane) {
        if (questionArray.length == 0) {
            questionVbox.getChildren().clear();
        } else {
            if (questionVbox.getId().equals("unAnsQuVbox")) {
                Sorting.sortOnUpvotes(questionArray);
            } else {
                Sorting.sortOnTimeAnswered(questionArray);
            }
            mapQuestions(questionVbox, questionArray, scrollpane);
        }
    }

    private static void mapQuestions(VBox questionVbox, QuestionDetailsDto[] questionList,
                                     ScrollPane scrollpane) {
        questionVbox.getChildren().clear();

        //For each question in the list create a new Question object
        for (QuestionDetailsDto question : questionList) {
            QuestionItem newQu = new QuestionItem(question.getId(), question.getUpvotes(),
                question.getText(), question.getAuthorName(), question.getAnswers(),
                question.getAnswered(), scrollpane, thisQuBoId);

            //Set controller
            if (thisStuController != null) {
                newQu.setStuController(thisStuController);
            } else {
                newQu.setModController(thisModController);
            }
            //Display the upvotes and the options menu
            newQu.newUpvoteVbox(upvoteMap, (modCode != null));
            newQu.displayOptions(secretCodeMap, modCode);

            //Add the question to the ObservableList
            questionVbox.getChildren().add(newQu);
        }
    }
}
