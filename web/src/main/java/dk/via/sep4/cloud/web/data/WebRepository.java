package dk.via.sep4.cloud.web.data;

import dk.via.sep4.cloud.data.DataRepository;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Timestamp;

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

    public void updateLimits(String minTemp, String maxTemp, String minHumidity, String maxHumidity, String maxCO2) {
        repository.updateLimits(minTemp, maxTemp, minHumidity, maxHumidity, maxCO2);
    }
    public void close() throws IOException {
        repository.close();
    }
}
