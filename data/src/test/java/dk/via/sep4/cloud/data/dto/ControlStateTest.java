package dk.via.sep4.cloud.data.dto;

import org.junit.jupiter.api.BeforeEach;

class ControlStateTest extends JsonDTOTestable<ControlState> {
    @Override
    @BeforeEach
    void setUp() {
        dataJSON =
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
        webJSONPairs = new String[]{
                "\"isOn\":true",
        };
        sample = new ControlState(true);
    }
}