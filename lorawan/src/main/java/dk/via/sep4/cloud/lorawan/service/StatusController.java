package dk.via.sep4.cloud.lorawan.service;

import dk.via.sep4.cloud.lorawan.websocket.LorawanClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StatusController {
    private final LorawanClient lorawanClient;

    @Autowired
    public StatusController(LorawanClient lorawanClient) {
        this.lorawanClient = lorawanClient;
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        boolean isHealthy = lorawanClient.refreshConnection();
        if (isHealthy) {
            log.debug("Connection Successfully Refreshed through the API");
        } else {
            log.error("Connection Failed to Refresh through the API");
        }
        return ResponseEntity
                .status(isHealthy ? HttpStatus.ACCEPTED : HttpStatus.SERVICE_UNAVAILABLE)
                .body(isHealthy ? "Connection Successfully Refreshed" : "Connection Failed to Refresh");
    }
}
