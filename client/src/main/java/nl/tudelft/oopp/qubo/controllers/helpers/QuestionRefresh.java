package nl.tudelft.oopp.qubo.controllers.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import nl.tudelft.oopp.qubo.communication.ServerCommunication;
import nl.tudelft.oopp.qubo.controllers.structures.Question;
import nl.tudelft.oopp.qubo.controllers.structures.QuestionListCell;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerDetailsDto;
import nl.tudelft.oopp.qubo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.qubo.dtos.questionboard.QuestionBoardDetailsDto;
import nl.tudelft.oopp.qubo.utilities.sorting.Sorting;

import java.util.ArrayList;
import java.util.List;

public class QuestionRefresh {
    private static QuestionBoardDetailsDto thisQuBo;
    
    private static QuestionDetailsDto[] answeredQuestions;
    private static QuestionDetailsDto[] unansweredQuestions;

    private static ListView<Question> unAnsQuListView;
    private static ListView<Question> ansQuListView;


    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();

    public static void refresh(QuestionBoardDetailsDto quBo, ListView<Question> unAnsLV,
                               ListView<Question> ansLV) {
        thisQuBo = quBo;
        unAnsQuListView = unAnsLV;
        ansQuListView = ansLV;
    }

    /**
     * Method that displays the questions that are in the question board on the screen. Answered questions
     * will be sorted by the time at which they were answered, and unanswered questions will be sorted by
     * the number of upvotes they have received.
     */
    public static void displayQuestions() {
        // To be deleted in final version
        if (thisQuBo == null) {
            divideQuestions(null);
            return;
        }
        //

        //Retrieve the questions and convert them to an array of QuestionDetailsDtos if the response is
        //not null.
        String jsonQuestions = ServerCommunication.retrieveQuestions(thisQuBo.getId());

        if (jsonQuestions == null) {
            divideQuestions(null);
        } else {
            QuestionDetailsDto[] questions = gson.fromJson(jsonQuestions, QuestionDetailsDto[].class);

            //Divide the questions over two lists and sort them.
            divideQuestions(questions);

            sortAndMap(unAnsQuListView, unansweredQuestions);
            sortAndMap(ansQuListView, answeredQuestions);
        }
    }

    private static void sortAndMap(ListView<Question> questionListView, QuestionDetailsDto[] questionArray) {
        if (questionArray.length == 0) {
            questionListView.getItems().clear();
        } else {
            Sorting.sortOnUpvotes(questionArray);
            mapQuestions(questionListView, questionArray);
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

    private static void mapQuestions(ListView<Question> questionListView, QuestionDetailsDto[] questionList) {
        ObservableList<Question> data = FXCollections.observableArrayList();

        //For each question in the list create a new Question object
        for (QuestionDetailsDto question : questionList) {
            Question newQu = new Question(question.getId(), question.getUpvotes(),
                question.getText(), question.getAuthorName(), null);

            //Get Answers if there are any
            if (question.getAnswers().size() != 0) {
                List<String> answers = new ArrayList<>();
                for (AnswerDetailsDto answer : question.getAnswers()) {
                    answers.add(answer.getText());
                }
                newQu.setAnswers(answers);
            }
            //Add the question to the ObservableList
            data.add(newQu);
        }

        questionListView.getItems().clear();
        //Set new questions in the ListView
        questionListView.setItems(data);
        //Set the custom cell factory for the listview
        questionListView.setCellFactory(listView
            -> new QuestionListCell(questionListView, secretCodeMap, upvoteMap));
    }
}
