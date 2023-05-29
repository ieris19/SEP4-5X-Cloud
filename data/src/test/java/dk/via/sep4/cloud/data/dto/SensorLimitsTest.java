package dk.via.sep4.cloud.data.dto;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SensorLimitsTest extends JsonDTOTestable<SensorLimits> {
    @Override
    @BeforeEach
    void setUp() {
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
        sample = new SensorLimits(4500, 5000, 30, 60, 800);
    }
}