package nl.tudelft.oopp.qubo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This class hold methods related to doing the actual requests to the endpoints.
 */
public class ServerCommunication {
    private static HttpClient client = HttpClient.newBuilder().build();
    protected static final String subUrl = "http://localhost:8080/api/";
    protected static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        .create();
    // To be added methods
    //public static:
    //  -  setClient()
    //protected static:
    //  - sendRequest()
    //  - post()
    //  - patch()

    /**
     * Retrieves an HTTP response from the server by sending an HTTP delete request.
     *
     * @param fullUrl   The URL corresponding to the server endpoint.
     * @return The HTTP response returned.
     */
    private static HttpResponse<String> delete(String fullUrl) {
        //Set up the request Object
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(fullUrl))
                .build();

        //Send the request, and retrieve and return the response from the server
        return sendRequest(request);
    }

    /**
     * Retrieves an HTTP response from the server by sending an HTTP put request.
     *
     * @param fullUrl       The URL corresponding to the server endpoint.
     * @param requestBody   The body of the request. This should contain the information that should be sent to
     *      the server.
     * @return The HTTP response returned.
     */
    private static HttpResponse<String> put(String fullUrl, String requestBody) {
        //Set up the request Object
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(fullUrl))
                .headers("Content-Type", "application/json;charset=UTF-8")
                .build();

        //Send the request, and retrieve and return the response from the server
        return sendRequest(request);
    }


}

