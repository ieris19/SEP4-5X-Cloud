package dk.via.sep4.cloud.web.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTestClient {
    private static String baseURL;

    public HttpTestClient(String url) {
        baseURL = url;
    }

    public HttpResponse<String> testRequest(String endpoint, String method, String body) {
        HttpRequest request = buildRequest(endpoint, method, body);
        return makeRequest(request);
    }

    private HttpResponse<String> makeRequest(HttpRequest request) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new AssertionError("Request failed", e);
        }
    }


    private HttpRequest buildRequest(String endpoint, String method, String params) {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        switch (endpoint) {
            case "/limits" -> {
                buildLimitsRequest(builder, endpoint, method, params);
            }
            case "/state" -> {
                buildStateRequest(builder, endpoint, method, params);
            }
            case "/readings" -> {
                buildReadingsRequest(builder, endpoint, method, params);
            }
            case "/comment" -> {
                buildCommentRequest(builder, endpoint, method, params);
            }
            default -> throw new IllegalArgumentException("Invalid endpoint");
        }
        return builder.build();
    }

    private void buildCommentRequest(HttpRequest.Builder builder, String endpoint, String method, String params) {
        builder.uri(URI.create(baseURL + endpoint));
        builder.PUT(HttpRequest.BodyPublishers.ofString(params));
    }

    private void buildReadingsRequest(HttpRequest.Builder builder, String endpoint, String method, String params) {
        builder.uri(URI.create(baseURL + endpoint + "?date=" + params));
    }

    private void buildStateRequest(HttpRequest.Builder builder, String endpoint, String method, String params) {
        builder.uri(URI.create(baseURL + endpoint));
    }

    private void buildLimitsRequest(HttpRequest.Builder builder, String endpoint, String method, String params) {
        builder.uri(URI.create(baseURL + endpoint));
        switch (method) {
            case "GET" -> builder.GET();
            case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(params));
            default -> throw new IllegalArgumentException("Invalid method");
        }
    }
}
