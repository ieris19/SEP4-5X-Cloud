package dk.via.sep4.cloud.data.dto;

import dk.via.sep4.cloud.data.repository.MockRepository;
import org.junit.jupiter.api.BeforeEach;

class SensorLimitsTest extends JsonDTOTestable<SensorLimits> {
    @Override
    @BeforeEach
    void setUp() {
        dataJSON = MockRepository.LIMIT_DATA;
        webJSONPairs = MockRepository.LIMIT_PAIRS
        sample = MockRepository.LIMIT_SAMPLE;
    }
}