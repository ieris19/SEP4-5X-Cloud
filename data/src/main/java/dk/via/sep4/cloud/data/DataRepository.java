package dk.via.sep4.cloud.data;

import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.data.dto.SensorState;

import java.io.Closeable;

/**
 * This interface is used as a database access.
 */
public interface DataRepository extends Closeable {
    /**
     * This method is used to retrieve all the sensor readings from the database.
     * @return an array of all the sensor readings in the database.
     */
    SensorReading[] getReadings(String date);
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
     * This method is used to add a comment to a specified reading.
     * @param id the id of the reading.
     * @param comment the comment to be added.
     */
    void addComment(String id, String comment);
    /**
     * This method is used to insert the sensor limits object into the database.
     * @SensorLimits limits the sensor limits to be inserted.
     */
    void insertLimits(SensorLimits limits);

    /**
     * This method is used to update the sensor limits in the database.
     * @param limits the sensor limits to be updated.
     */
    void updateLimits(SensorLimits limits);
    /**
     * This method is used to retrieve the sensor state from the database.
     * @param state the sensor state to be inserted.
     */
    void insertState(SensorState state);
    /**
     * This method is used to retrieve the sensor state from the database.
     * @return the sensor state object.
     */
    SensorState getState();
    /**
     * This method is used to update the sensor state in the database.
     * @param state the sensor state to be updated.
     */
    void updateState(SensorState state);
}
