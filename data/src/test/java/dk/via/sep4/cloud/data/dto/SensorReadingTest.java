package dk.via.sep4.cloud.data.dto;

import dk.via.sep4.cloud.data.repository.MockData;
import dk.via.sep4.cloud.data.repository.MockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the SensorReading data transfer object.
 */
class SensorReadingTest extends JsonDTOTestable<SensorReading> {
    private String legacyJSON;
    private SensorReading legacyReading;

    @BeforeEach
    public void setUp() {
        dataJSON = MockData.READING_DATA;
        webJSONPairs = MockData.READING_PAIRS;
        sample = MockData.READING_SAMPLE;
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
    public void fromLegacyJson() {
        SensorReading test = SensorReading.fromJson(legacyJSON);
        assertEquals(legacyReading, test);
    }

    @Test
    @Disabled
    public void toBSON() {
        //TODO: Actually test BSON conversion
        assertDoesNotThrow(() -> sample.toBSON());
    }
}
