package dk.via.sep4.cloud.web.service;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpParser {
    public String requestInfo(HttpRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Request Header: ").append(stringifyHeaders(request.headers())).append('\n');
        sb.append("Request Method: ").append(request.method()).append('\n');
        return sb.toString();
    }

    public String responseInfo(HttpResponse<String> response) {
        StringBuilder sb = new StringBuilder();
        sb.append("Response Status Code: ").append(response.statusCode()).append('\n');
        sb.append("Response Header: ").append(stringifyHeaders(response.headers())).append('\n');
        sb.append("Response Body: ").append(formatJSON(response.body())).append('\n');
        return sb.toString();
    }

    public String exchangeInfo(HttpRequest request, HttpResponse<String> response) {
        return requestInfo(request) + responseInfo(response);
    }

    private String formatJSON(String body) {
        return body.replace(",", ",\n\t")
                .replace("{", "\n{\n\t")
                .replace("}", "\n}\n");
    }

    private String stringifyHeaders(HttpHeaders headers) {
        StringBuilder builder = new StringBuilder();
        for (String headerName : headers.map().keySet()) {
            builder.append(headerName).append(": ");
            for (String headerValue : headers.allValues(headerName)) {
                builder.append(headerValue).append(", ");
            }
            builder.delete(builder.length() - 2, builder.length());
            builder.append('\n');
        }
        return builder.toString();
    }

    private String stringifyBodyPublisher(HttpRequest.BodyPublisher bodyPublisher) {
        return null;
    }
}
