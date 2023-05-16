package dk.via.sep4.cloud.web.data;

import dk.via.sep4.cloud.data.DataRepository;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.data.dto.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Closeable;
import java.io.IOException;

@Repository
public class WebRepository implements Closeable {
    private DataRepository repository;

    @Autowired
    public WebRepository(DataRepository repository) {
        this.repository = repository;
    }

    public String getReadings() {
        SensorReading[] readings = repository.getReadings();
        return WebJSONData.getReadingsAsJSON(readings);
    }

    public void insertReading(SensorReading reading) {
        repository.insertReading(reading);
    }

    public String getLimits() {
        SensorLimits limits = repository.getLimits();
        return WebJSONData.getLimitsAsJSON(limits);
    }

    public void insertLimits(SensorLimits limits) {
        repository.insertLimits(limits);
    }

    public void updateLimits(SensorLimits limits) {
        repository.updateLimits(limits);
    }

    public String getCredentials() {
        UserCredentials credentials = repository.getCredentials();
        return WebJSONData.getCredentialsAsJSON(credentials);
    }

    public void insertCredentials(UserCredentials credentials) {
        repository.insertCredentials(credentials);
    }

    public void close() throws IOException {
        repository.close();
    }
}
