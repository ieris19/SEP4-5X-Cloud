package dk.via.sep4.cloud.data.repository;

public record DataOperationResult(boolean success, long affectedCount, String additionalInfo) {}
