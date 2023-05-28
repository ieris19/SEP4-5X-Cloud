package dk.via.sep4.cloud.web.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActiveRequestTest {
    private static HttpTestClient client;

    @BeforeAll
    public static void beforeAll() {
        client = new HttpTestClient("http://localhost:8080");
    }

    @Test
    public void testPutLimits() {
        String body = "{\"maxTemperature\":35,\"minTemperature\":10,\"maxHumidity\":80,\"minHumidity\":20,\"maxCo2\":3000}";
        client.testRequest("/limits", "PUT", body);
    }

    @Test public void testGetLimits() {
        client.testRequest("/limits", "GET", "");
    }
}
