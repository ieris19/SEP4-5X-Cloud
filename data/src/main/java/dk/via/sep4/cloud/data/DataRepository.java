package dk.via.sep4.cloud.data;

import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.data.dto.UserCredentials;

import java.io.Closeable;
/**
 * This interface is used as a database access for the web-api.
 */
public interface DataRepository extends Closeable {
    /**
     * This method is used to retrieve all the sensor readings from the database.
     * @return an array of all the sensor readings in the database.
     */
    SensorReading[] getReadings();
    /**
     * This method is used to insert a sensor reading object into the database.
     * @param reading the sensor reading to be inserted.
     */
    void insertReading(SensorReading reading);
    /**
     * This method is used to retrieve the sensor limits from the database.
     * @return the sensor limits object.
     */
    SensorLimits getLimits();
    /**
     * This method is used to insert the sensor limits object into the database.
     * @param limits the sensor limits to be inserted.
     */
    void insertLimits(SensorLimits limits);
    /**
     * This method is used to update the sensor limits object in the database.
     * @param limits the sensor limits to be updated.
     */
    void updateLimits(SensorLimits limits);
    /**
     * This method is used to retrieve the user credentials from the database.
     * @return the user credentials object.
     */
    UserCredentials getCredentials();
    /**
     * This method is used to insert the user credentials object into the database.
     * @param credentials the user credentials to be inserted.
     */
    void insertCredentials(UserCredentials credentials);
}
