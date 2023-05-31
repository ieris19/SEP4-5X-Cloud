package dk.via.sep4.cloud.data.dto;

import dk.via.sep4.cloud.data.repository.MockData;
import org.junit.jupiter.api.BeforeEach;

class ControlStateTest extends JsonDTOTestable<ControlState> {
    @Override
    @BeforeEach
    public void setUp() {
        dataJSON = MockData.CONTROL_DATA;
        webJSONPairs = MockData.CONTROL_PAIRS;
        sample = MockData.CONTROL_SAMPLE;
    }
}