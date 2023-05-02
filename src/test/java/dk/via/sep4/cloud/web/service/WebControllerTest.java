package dk.via.sep4.cloud.web.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebControllerTest {
	private String portNumber = "8080";

	private void assertRequestStatus(HttpRequest request, int expectedStatusCode) {
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
		assertEquals(expectedStatusCode, response.statusCode());
	}

	private HttpRequest getRequestForPath(String apiPath, String apiMethod) {
		String TARGET_URL = "http://localhost:" + portNumber + apiPath;
		URI targetURI;
		try {
			targetURI = new URI(TARGET_URL);
		} catch (URISyntaxException e) {
			System.out.println("URISyntaxException: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(targetURI);
		if (apiMethod.equalsIgnoreCase("GET")) {
			requestBuilder.GET();
		} else if (apiMethod.equalsIgnoreCase("POST")) {
			requestBuilder.POST(HttpRequest.BodyPublishers.ofString(""));
		} else if (apiMethod.equalsIgnoreCase("PUT")) {
			requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(""));
		} else if (apiMethod.equalsIgnoreCase("DELETE")) {
			requestBuilder.DELETE();
		} else {
			return null;
		}
		return requestBuilder.build();
	}

	@Test void getLastReading() {
		assertRequestStatus(getRequestForPath("/readings/last", "GET"), 200);
	}

	@Test void getAllReadings() {
		assertRequestStatus(getRequestForPath("/readings", "GET"), 200);
	}
}