package dk.via.sep4.cloud.data.repository;

import dk.via.sep4.cloud.data.dto.ControlState;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;

import java.sql.Timestamp;

public class MockData {
    public static final String CONTROL_DATA =
            """
                      {
                         "_id": {
                           "$oid": "6474b08d9d33e94c6ddf9e2c"
                         },
                         "type": "state",
                         "isOn": true
                         }
                      }
                    """.trim().replace(" ", "").replace("\n", "");
    public static final String[] CONTROL_PAIRS = new String[]{
            "\"isOn\":true",
    };
    public static final ControlState CONTROL_SAMPLE = new ControlState(true);
    public static final String LIMIT_DATA =
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
    public static final String[] LIMIT_PAIRS = new String[]{
            "\"minTemperature\":4500",
            "\"maxTemperature\":5000",
            "\"minHumidity\":30",
            "\"maxHumidity\":60",
            "\"maxCo2\":800",
    };
    public static final SensorLimits LIMIT_SAMPLE = new SensorLimits(4500, 5000, 30, 60, 800);
    public static final String READING_DATA =
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
    public static final String[] READING_PAIRS = new String[]{
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
    public static final SensorReading READING_SAMPLE = new SensorReading("6474c0a4c737a0417aefe675",
            true, 4240.5, 165, 42405, 42405, 2650, "100101",
            Timestamp.valueOf("2023-05-29 17:11:32.517"), "");
    public static final DataOperationResult SUCCESS_OPERATION_RESULT = new DataOperationResult(true, 1);
}

