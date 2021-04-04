package nl.tudelft.oopp.qubo.communication;

import java.net.http.HttpResponse;
import java.util.UUID;
import nl.tudelft.oopp.qubo.dtos.answer.AnswerCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.question.QuestionCreationBindingModel;
import nl.tudelft.oopp.qubo.dtos.question.QuestionEditingBindingModel;

/**
 * This class hold methods related to communication for Questions.
 */
public class QuestionCommunication {

    /**
     * Adds a question with specified text and author to the board.
     * Communicates with the /api/board/{boardid}/question server endpoint.
     *
     * @param boardId The ID of the Question Board whose details should be retrieved.
     * @param text    The content of the question.
     * @param author  The name of the author of the question.
     * @return The QuestionCreationDto in JSON String format that contains the ID and
     *         secret code associated with the question.
     */
    public static String addQuestion(UUID boardId, String text, String author) {
        //Instantiate a QuestionCreationBindingModel
        QuestionCreationBindingModel questionModel = new QuestionCreationBindingModel();
        questionModel.setText(text);
        questionModel.setAuthorName(author);

        //Create a request and response object, send the request, and retrieve the response
        String fullUrl = ServerCommunication.subUrl + "board/" + boardId + "/question";
        String requestBody = ServerCommunication.gson.toJson(questionModel);
        HttpResponse<String> response = ServerCommunication.post(fullUrl, requestBody, "Content-Type",
            "application/json;charset=UTF-8");

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Adds an answer to a question.
     * Communicates with the /api/question/{questionid}/answer?code={code} server endpoint.
     *
     * @param questionId The ID of the question that is being answered.
     * @param modCode    The moderator code associated with the question board
     *                   that contains the question.
     * @param text       The body of the answer to be added.
     * @return The AnswerCreationDto associated with the answer in JSON String
     *         format if, and only if, the request was successful.
     */
    public static String addAnswer(UUID questionId, UUID modCode, String text) {
        //Instantiate a AnswerCreationBindingModel
        AnswerCreationBindingModel answerModel = new AnswerCreationBindingModel();
        answerModel.setText(text);

        //Create a request and response object, send the request, and retrieve the response
        String fullUrl = ServerCommunication.subUrl + "question/" + questionId + "/answer?code=" + modCode;
        String requestBody = ServerCommunication.gson.toJson(answerModel);
        HttpResponse<String> response = ServerCommunication.post(fullUrl, requestBody, "Content-Type",
            "application/json;charset=UTF-8");

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Edits the text of a question
     * Communicates with the /api/question/{questionid}?code={code} server endpoint.
     *
     * @param questionId The ID of the question whose text should be modified.
     * @param code       The moderator code associated with the question board
     *                   that contains the question or the question secret code.
     * @param text       The new question text.
     * @return The QuestionDetailsDto associated with the question in JSON String
     *         format if, and only if, the request was successful.
     */
    public static String editQuestion(UUID questionId, UUID code, String text) {
        //Set up the parameters required by the put helper method
        String fullUrl = ServerCommunication.subUrl + "question/" + questionId + "?code=" + code;

        QuestionEditingBindingModel editedQuestion = new QuestionEditingBindingModel();
        editedQuestion.setText(text);
        String requestBody = ServerCommunication.gson.toJson(editedQuestion);

        //Send the put request to edit the question and retrieve the response
        HttpResponse<String> response = ServerCommunication.put(fullUrl, requestBody);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Deletes the question from the board.
     * Communicates with the /api/question/{questionid}?code={code} server endpoint.
     *
     * @param questionId The ID of the question that should be deleted.
     * @param code       The moderator code associated with the board or the question's secret code.
     * @return The QuestionDetailsDto associated with the deleted question in JSON String format if,
     *         and only if, the question was deleted from the board.
     */
    public static String deleteQuestion(UUID questionId, UUID code) {
        //Set up the variables required by the delete helper method
        String fullUrl = ServerCommunication.subUrl + "question/" + questionId + "?code=" + code;

        //Send the request to delete the question from the board and retrieve the response
        HttpResponse<String> response = ServerCommunication.delete(fullUrl);

        //If the request was unsuccessful, return null
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    /**
     * Mark the question from the board as answered.
     *
     * @param questionId The ID of the question to be marked as answered.
     * @param code       The moderator code that is associated with the board
     *                   the question is part of, or the question's secret code.
     * @return The QuestionDetailsDto of the answered question in JSON String format
     *         if and only if the question has been marked as answered successfully.
     */
    public static String markQuestionAsAnswered(UUID questionId, UUID code) {
        //Set up the variables required by the patch helper method
        String fullUrl = ServerCommunication.subUrl + "question/" + questionId + "/answer?code=" + code;

        //Send the request to mark the question as answered and retrieve the response
        HttpResponse<String> response = ServerCommunication.patch(fullUrl);

        //Check if the request was successful
        if (response == null || response.statusCode() != 200) {
            return null;
        }

        //The question has been marked as answered
        return response.body();
    }


}