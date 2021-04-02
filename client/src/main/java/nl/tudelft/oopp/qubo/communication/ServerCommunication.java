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

    /**
     * Set the value of the client of ServerCommunication.
     *
     * @param client    The HttpClient object to set.
     */
    public static void setClient(HttpClient client) {
        ServerCommunication.client = client;
    }

    /**
     * Retrieves an http response from the server by sending an http request.
     *
     * @param request       The http request to be sent to be server.
     * @return The http response returned.
     */
    protected static HttpResponse<String> sendRequest(HttpRequest request) {
        //Instantiate a response object
        HttpResponse<String> response = null;

        //Send the request to the server and retrieve the response
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
     * @param fullUrl         The full url of the request.
     * @param requestBody     The request body of JSON form.
     * @param headers         The http headers of the request.
     * @return The http response returned.
     */
    protected static HttpResponse<String> post(String fullUrl, String requestBody, String... headers) {
        //Set up the request Object
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(fullUrl))
                .headers(headers)
                .build();

        //Send the request, and retrieve and return the response from the server
        return sendRequest(request);
    }

    /**
     * Retrieves an HTTP response from the server by sending an HTTP delete request.
     *
     * @param fullUrl   The URL corresponding to the server endpoint.
     * @return The HTTP response returned.
     */
    public static HttpResponse<String> delete(String fullUrl) {
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
    public static HttpResponse<String> put(String fullUrl, String requestBody) {
        //Set up the request Object
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(fullUrl))
                .headers("Content-Type", "application/json;charset=UTF-8")
                .build();

        //Send the request, and retrieve and return the response from the server
        return sendRequest(request);
    }

    /**
     * Retrieves an HTTP response from the server by sending an HTTP patch request.
     *
     * @param fullUrl   The URL corresponding to the server endpoint.
     * @return The http response.
     */
    protected static HttpResponse<String> patch(String fullUrl) {
        //Set up the request Object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{}"))
                .header("Content-Type", "application/json")
                .build();

        //Send the request, and return the http response
        return  sendRequest(request);
    }

}