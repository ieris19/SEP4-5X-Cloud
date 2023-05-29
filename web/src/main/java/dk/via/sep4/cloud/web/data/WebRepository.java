package dk.via.sep4.cloud.web.data;

import dk.via.sep4.cloud.data.repository.DataOperationResult;
import dk.via.sep4.cloud.data.repository.DataRepository;
import dk.via.sep4.cloud.data.dto.ControlState;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.Closeable;
import java.io.IOException;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

/**
 * This class is used as database access wrapper.
 * It converts the data from the DataRepository into JSON for the API
 * and turns JSON from the API into data for the DataRepository.
 */
@Repository
@Scope(scopeName = SCOPE_SINGLETON)
public class WebRepository implements Closeable {
    private final DataRepository repository;

    @Autowired
    public WebRepository(DataRepository repository) {
        this.repository = repository;
    }

    public String getReadings(String date) {
        SensorReading[] readings = repository.getReadings(date);
        JSONArray array = new JSONArray();
        for (SensorReading reading : readings) {
            array.put(reading.toJSON());
        }
        return array.toString();
    }

    public String getLimits() {
        return repository.getLimits().toJSON().toString();
    }

    public String getState() {
        return repository.getState().toJSON().toString();
    }

    public DataResultStatus addComment(String jsonString) {
        JSONObject jsonBody = new JSONObject(jsonString);
        String id = jsonBody.getString("id");
        String comment = jsonBody.getString("comment");
        DataOperationResult result = repository.addComment(id, comment);
        return updateResultStatus(result);
    }

    public DataResultStatus updateLimits(String jsonLimits) {
        DataOperationResult result = repository.updateLimits(SensorLimits.fromJson(jsonLimits));
        return updateResultStatus(result);
    }

    public DataResultStatus updateState(String stateJson) {
        DataOperationResult result = repository.updateState(ControlState.fromJson(stateJson));
        return updateResultStatus(result);
    }

    private DataResultStatus updateResultStatus(DataOperationResult result) {
        if (!result.isSuccessful()) {
            return DataResultStatus.NOT_ACKNOWLEDGED;
        }
        if (result.getAffectedCount() < 1) {
            return DataResultStatus.NO_DATA_AFFECTED;
        }
        return DataResultStatus.OK;
    }

    public void close() throws IOException {
        repository.close();
    }
}
