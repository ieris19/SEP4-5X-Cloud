package dk.via.sep4.cloud.data.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SensorLimitsTest {
    private static String dataJSON;
    private static String[] webJSONPairs;
    private static SensorLimits sampleReading;

    @BeforeAll
    static void setUp() {
        dataJSON =
                """
                   {
                      "_id": {
                        "$oid": "6474b2019d33e94c6ddf9e2e"
                      },
                      "type": "limit values",
                      "minTemperature": 4500,
                      "maxTemperature": 5000,
                      "minHumidity": 30,
                      "maxHumidity": 60,
                      "maxCo2": 800
                    }
                """.trim().replace(" ", "").replace("\n", "");
        webJSONPairs = new String[]{
                "\"minTemperature\":4500",
                "\"maxTemperature\":5000",
                "\"minHumidity\":30",
                "\"maxHumidity\":60",
                "\"maxCo2\":800",
        };
        sampleReading = new SensorLimits(4500, 5000, 30, 60, 800);
    }

    @Test
    void fromJson() {
        SensorLimits reading = SensorLimits.fromJson(dataJSON);
        assertEquals(sampleReading, reading);
    }

    @Test
    void toJSON() {
        String json = sampleReading.toJSON().toString();
        for (String pair : webJSONPairs) {
            assertTrue(json.contains(pair), "JSON does not contain " + pair);
        }
    }
}