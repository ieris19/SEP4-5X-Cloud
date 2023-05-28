package dk.via.sep4.cloud.web.data;

import dk.via.sep4.cloud.data.DataRepository;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.data.dto.SensorState;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Closeable;
import java.io.IOException;

/**
 * This class is used as a database access for the web-api that returns JSON objects instead of implemented Java class objects.
 */
@Repository
public class WebRepository implements Closeable {
    private DataRepository repository;

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

    public void insertReading(SensorReading reading) {
        repository.insertReading(reading);
    }

    public String getLimits() {
        return repository.getLimits().toJSON().toString();
    }

    public void updateLimits(SensorLimits newLimits) {
        repository.updateLimits(newLimits);
    }

    public String getState() {
        return repository.getState().toJSON().toString();
    }

    public void updateState(String state) {
        repository.updateState(new SensorState(Boolean.valueOf(state)));
    }

    public void close() throws IOException {
        repository.close();
    }

    public void addComment(String jsonString) {
        JSONObject jsonBody = new JSONObject(jsonString);
        String id = jsonBody.getString("id");
        String comment = jsonBody.getString("comment");
        repository.addComment(id, comment);
    }
}
