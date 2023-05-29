package dk.via.sep4.cloud.data.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the SensorReading data transfer object.
 */
class SensorReadingTest {
    private static String dataJSON;
    private static String[] webJSONPairs;
    private static SensorReading sampleReading;
    private static String legacyJSON;
    private static SensorReading legacyReading;

    @BeforeAll
    static void setUp() {
        dataJSON =
                """
                        {
                          "_id": {
                            "$oid": "6474c0a4c737a0417aefe675"
                          },
                          "pir": true,
                          "temperature": 4240.5,
                          "humidity": 165,
                          "co2": 42405,
                          "sound": 42405,
                          "light": 2650,
                          "code": "100101",
                          "time": {
                            "$date": "2023-05-29T17:11:32.517Z"
                           },
                          "comment": ""
                        }
                        """.trim().replace(" ", "").replace("\n", "");
        webJSONPairs = new String[]{
                "\"id\":\"6474c0a4c737a0417aefe675\"",
                "\"code\":\"100101\"",
                "\"temperature\":4240.5",
                "\"co2\":42405",
                "\"humidity\":165",
                "\"light\":2650",
                "\"sound\":42405",
                "\"pir\":true",
                "\"time\":\"2023-05-29 17:11:32.517\""
        };
        sampleReading = new SensorReading("6474c0a4c737a0417aefe675",
                true, 4240.5, 165, 42405, 42405, 2650, "100101",
                Timestamp.valueOf("2023-05-29 17:11:32.517"), "");
        legacyJSON =
                """
                        {
                          "_id": {
                            "$oid": "6474c0a4c737a0417aefe675"
                          },
                          "pir": true,
                          "temperature": 4240.5,
                          "humidity": 165,
                          "co2": 42405,
                          "sound": 42405,
                          "light": 2650,
                          "code": 3,
                          "time": {
                            "$date": "2023-05-29T17:11:32.517Z"
                           },
                        }
                        """.trim().replace(" ", "").replace("\n", "");
        legacyReading = new SensorReading("6474c0a4c737a0417aefe675",
                true, 4240.5, 165, 42405, 42405, 2650, "3",
                Timestamp.valueOf("2023-05-29 17:11:32.517"), "");
    }

    @Test
    void fromJson() {
        SensorReading reading = SensorReading.fromJson(dataJSON);
        assertEquals(sampleReading, reading);
    }

    @Test
    void fromLegacyJson() {
        SensorReading test = SensorReading.fromJson(legacyJSON);
        assertEquals(legacyReading, test);
    }

    @Test
    void toJSON() {
        String json = sampleReading.toJSON().toString();
        for (String pair : webJSONPairs) {
            assertTrue(json.contains(pair), "JSON does not contain " + pair);
        }
    }


    @Test
    @Disabled
    void toBSON() {
        //TODO: Actually test BSON conversion
        assertDoesNotThrow(() -> sampleReading.toBSON());
    }
}
