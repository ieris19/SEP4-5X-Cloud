package dk.via.sep4.cloud.data.repository;

public abstract class DataOperationResult {
    protected boolean isSuccessful;
    protected long affectedCount;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public long getAffectedCount() {
        return affectedCount;
    }
}