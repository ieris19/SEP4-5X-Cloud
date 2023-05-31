package dk.via.sep4.cloud.data.dto;

import dk.via.sep4.cloud.data.repository.MockRepository;
import org.junit.jupiter.api.BeforeEach;

class ControlStateTest extends JsonDTOTestable<ControlState> {
    @Override
    @BeforeEach
    void setUp() {
        dataJSON = MockRepository.CONTROL_DATA;
        webJSONPairs = MockRepository.CONTROL_PAIRS;
        sample = MockRepository.CONTROL_SAMPLE;
    }
}