package dk.via.sep4.cloud.data.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the SensorReading data transfer object.
 */
class SensorReadingTest extends JsonDTOTestable<SensorReading> {
    private String legacyJSON;
    private SensorReading legacyReading;

    @BeforeEach
    void setUp() {
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
        sample = new SensorReading("6474c0a4c737a0417aefe675",
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
    void fromLegacyJson() {
        SensorReading test = SensorReading.fromJson(legacyJSON);
        assertEquals(legacyReading, test);
    }

    @Test
    @Disabled
    void toBSON() {
        //TODO: Actually test BSON conversion
        assertDoesNotThrow(() -> sample.toBSON());
    }
}
