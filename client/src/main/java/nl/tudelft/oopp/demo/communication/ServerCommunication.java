package nl.tudelft.oopp.demo.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardCreationBindingModel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();
    private static String suburl = "http://localhost:8080/";

    /**
     * Retrieves an http response from the server by sending an http request.
     *
     * @param request       The http request to be sent to be server.
     * @return The http response returned.
     * @throws Exception    if communication with the server fails.
     */
    private static HttpResponse<String> sendRequest(HttpRequest request) {
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Retrieves an http response from the server by sending an http post request.
     *
     * @param full_url         The full url of the request.
     * @param requestBody     The request body of JSON form.
     * @param headers         The http headers of the request.
     * @return The http response returned.
     */
    private static HttpResponse<String> post(String full_url, String requestBody, String... headers) {
        
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(full_url))
                .headers(headers)
                .build();

        HttpResponse<String> response = sendRequest(request);

        return response;
    }

    /**
     * The method sends a request to the server to create a question board.
     *
     * @param board     The QuestionBoardCreationBindingModel object that contains details of a question board.
     * @return The http response returned.
     */
    public static HttpResponse<String> createBoardRequest(QuestionBoardCreationBindingModel board) {
        String full_url = suburl + "api/board";

        Gson gson = new Gson();
        String requestBody = gson.toJson(board);
        HttpResponse<String> res = post(full_url, requestBody, "Content-Type", 
                                        "application/json;charset=UTF-8");

        if (res == null || res.statusCode() != 200) {
            return null;
        }

        return res;
    }


    /**
     * Retrieves a quote from the server.
     * @return the body of a get request to the server.
     * @throws Exception if communication with the server fails.
     */
    public static String getQuote() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/quote")).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return response.body();
    }

}
