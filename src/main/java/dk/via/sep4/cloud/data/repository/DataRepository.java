package dk.via.sep4.cloud.data.repository;

import dk.via.sep4.cloud.data.SensorLimits;
import dk.via.sep4.cloud.data.SensorReading;
import dk.via.sep4.cloud.data.UserCredentials;

import java.io.Closeable;

public interface DataRepository extends Closeable {
    SensorReading[] getReadings();
    void insertReading(SensorReading reading);
    SensorLimits getLimits();
    void insertLimits(SensorLimits limits);
    void updateLimits(SensorLimits limits);
    UserCredentials getCredentials();
    void insertCredentials(UserCredentials credentials);
}
