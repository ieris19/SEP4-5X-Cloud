package dk.via.sep4.cloud.web.data;

import dk.via.sep4.cloud.data.repository.MockData;
import dk.via.sep4.cloud.data.repository.MockRepository;
import dk.via.sep4.cloud.data.utils.JsonComparator;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebRepositoryTest {
    private static WebRepository repository;
    private static final String DATE = "2023-05-29";

    @BeforeAll
    static void setUp() {
        repository = new WebRepository(new MockRepository());
    }

    @Test
    public void getReadings() {
        String readings = repository.getReadings(DATE);
        String result = new JSONArray(readings).get(0).toString();
        assertTrue(JsonComparator.contains(result, MockData.READING_PAIRS));
    }

    @Test
    public void getLimits() {
        String limits = repository.getLimits();
        assertTrue(JsonComparator.contains(limits, MockData.LIMIT_PAIRS));
    }

    @Test
    public void getState() {
        String state = repository.getState();
        assertTrue(JsonComparator.contains(state, MockData.CONTROL_PAIRS));
    }

    @Test
    public void addComment() {
        assertSame(DataResultStatus.OK, repository.addComment(MockRequest.PUT_addComment));
    }

    @Test
    public void updateLimits() {
        assertSame(DataResultStatus.OK, repository.updateLimits(MockRequest.PUT_updateLimits));
    }

    @Test
    public void updateState() {
        assertSame(DataResultStatus.OK, repository.updateState(MockRequest.PUT_updateState));
    }
}