package dk.via.sep4.cloud.data.repository;

import dk.via.sep4.cloud.data.dto.ControlState;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;

import java.sql.Timestamp;

public class MockRepository implements DataRepository {
    DataOperationResult successfulResult = new DataOperationResult(true, 1);

    @Override
    public SensorReading[] getReadings(String date) {
        return new SensorReading[]{
                new SensorReading
                        (true, 20d, 60, 500, 100, 100, "100110", new Timestamp(0))
        };
    }

    @Override
    public SensorLimits getLimits() {
        return new SensorLimits(50, 50, 50, 50, 50);
    }

    @Override
    public ControlState getState() {return new ControlState(true);}

    @Override
    public DataOperationResult insertReading(SensorReading reading) {
        return successfulResult;
    }

    @Override
    public DataOperationResult addComment(String id, String comment) {
        return successfulResult;
    }

    @Override
    public DataOperationResult updateLimits(SensorLimits limits) {
        return successfulResult;
    }

    @Override
    public DataOperationResult updateState(ControlState state) {
        return successfulResult;
    }

    @Override
    public void close() {}
}
