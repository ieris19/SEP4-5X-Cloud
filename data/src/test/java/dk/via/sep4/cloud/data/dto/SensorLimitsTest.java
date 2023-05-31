package dk.via.sep4.cloud.data.dto;

import dk.via.sep4.cloud.data.repository.MockData;
import dk.via.sep4.cloud.data.repository.MockRepository;
import org.junit.jupiter.api.BeforeEach;

class SensorLimitsTest extends JsonDTOTestable<SensorLimits> {
    @Override
    @BeforeEach
    public void setUp() {
        dataJSON = MockData.LIMIT_DATA;
        webJSONPairs = MockData.LIMIT_PAIRS;
        sample = MockData.LIMIT_SAMPLE;
    }
}