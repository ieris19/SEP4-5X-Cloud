package dk.via.sep4.cloud.data.repository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DataOperationResult {
    protected final boolean isSuccessful;
    protected final long affectedCount;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public long getAffectedCount() {
        return affectedCount;
    }
}