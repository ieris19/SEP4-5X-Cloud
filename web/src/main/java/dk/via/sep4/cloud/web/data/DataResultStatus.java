package dk.via.sep4.cloud.web.data;

import dk.via.sep4.cloud.data.repository.DataOperationResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public enum DataResultStatus {
    OK(HttpStatus.OK, "Operation completed successfully!"),
    UNAFFECTED(HttpStatus.ACCEPTED, "No data affected!"),
    UNSUCCESSFUL(HttpStatus.INTERNAL_SERVER_ERROR, "Operation unsuccessful!");

    private final ResponseEntity<String> dataResponse;

    DataResultStatus(HttpStatus status, String body) {
        this.dataResponse = ResponseEntity.status(status).body(body);
    }

    public static DataResultStatus of(DataOperationResult result) {
        if (!result.isSuccessful()) {
            return DataResultStatus.UNSUCCESSFUL;
        }
        if (result.getAffectedCount() < 1) {
            return DataResultStatus.UNAFFECTED;
        }
        return DataResultStatus.OK;
    }

    public ResponseEntity<String> httpResponse() {
        return dataResponse;
    }
}
