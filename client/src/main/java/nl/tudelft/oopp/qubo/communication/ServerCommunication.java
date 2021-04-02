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
    //  - delete()
    //  - patch()
    //  - put()
}

