package dk.via.sep4.cloud.web.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class WebControllerTest {
    private String portNumber = "8080";

    @Test
    void getLastReading() {
        HttpRequest request;
        try {
            request = lastReadingRequest();
        } catch (URISyntaxException e) {
            System.out.println("URISyntaxException: " + e.getMessage());
            e.printStackTrace();
            assert false;
            return;
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            e.printStackTrace();
            assert false;
            return;
        } catch (InterruptedException e) {
            System.out.println("InterruptedException: " + e.getMessage());
            e.printStackTrace();
            assert false;
            return;
        }
        System.out.println(response.body());
        assertEquals(200, response.statusCode());
    }

    private HttpRequest lastReadingRequest() throws URISyntaxException {
        String TARGET_URL = "http://localhost:" + portNumber + "/lastReading";
        URI targetURI = new URI(TARGET_URL);
        return HttpRequest.newBuilder()
                .uri(targetURI)
                .GET()
                .build();
    }
}