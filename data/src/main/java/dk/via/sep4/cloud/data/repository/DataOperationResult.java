package dk.via.sep4.cloud.data.repository;

public interface DataOperationResult {
    boolean isSuccessful();
    long getAffectedCount();
    String getAdditionalInfo();
}