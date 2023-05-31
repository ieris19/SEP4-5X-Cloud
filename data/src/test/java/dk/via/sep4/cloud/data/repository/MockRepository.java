package dk.via.sep4.cloud.data.repository;

import dk.via.sep4.cloud.data.dto.ControlState;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;

import java.sql.Timestamp;

public class MockRepository implements DataRepository {
    @Override
    public SensorReading[] getReadings(String date) {
        return new SensorReading[]{MockData.READING_SAMPLE};
    }

    @Override
    public SensorLimits getLimits() {
        return MockData.LIMIT_SAMPLE;
    }

    @Override
    public ControlState getState() {
        return MockData.CONTROL_SAMPLE;
    }

    @Override
    public DataOperationResult insertReading(SensorReading reading) {
        return MockData.SUCCESS_OPERATION_RESULT;
    }

    @Override
    public DataOperationResult addComment(String id, String comment) {
        return MockData.SUCCESS_OPERATION_RESULT;
    }

    @Override
    public DataOperationResult updateLimits(SensorLimits limits) {
        return MockData.SUCCESS_OPERATION_RESULT;
    }

    @Override
    public DataOperationResult updateState(ControlState state) {
        return MockData.SUCCESS_OPERATION_RESULT;
    }

    @Override
    public void close() {
    }
}
