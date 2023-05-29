package dk.via.sep4.cloud.web.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ActiveRequestTest {
    private static final String PUT_LIMITS_BODY;
    private static final String PUT_STATE_BODY;
    private static final String PUT_COMMENT_BODY;
    private static HttpTestClient client;

    static {
        try (Scanner reader = new Scanner(new File(ActiveRequestTest.class.getResource("requestBodies").toURI()))) {
            PUT_LIMITS_BODY = reader.nextLine();
            PUT_STATE_BODY = reader.nextLine();
            PUT_COMMENT_BODY = reader.nextLine();
        } catch (IOException | URISyntaxException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void beforeAll() {
        client = new HttpTestClient("http://localhost:8080");
    }

    @Test
    public void testGetLimits() {
        HttpResponse<String> response = client.testRequest("/limits", "GET", "");
        assert response.statusCode() == 200;
    }

    @Test
    public void testPutLimits() {
        HttpResponse<String> response = client.testRequest("/limits", "PUT", PUT_LIMITS_BODY);
        assert response.statusCode() == 200;
    }

    @Test
    public void testPutComment() {
        HttpResponse<String> response = client.testRequest("/comment", "PUT", PUT_COMMENT_BODY);
        assert response.statusCode() == 200;
    }
}
