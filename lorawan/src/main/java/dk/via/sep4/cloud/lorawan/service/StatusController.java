package dk.via.sep4.cloud.lorawan.service;

import dk.via.sep4.cloud.lorawan.websocket.LorawanClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@RestController
public class StatusController {
    private LorawanClient lorawanClient;

    @Autowired
    public StatusController(LorawanClient lorawanClient) {
        this.lorawanClient = lorawanClient;
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        boolean isHealthy = lorawanClient.refreshConnection();
        return ResponseEntity
                .status(isHealthy ? HttpStatus.ACCEPTED : HttpStatus.SERVICE_UNAVAILABLE)
                .body(isHealthy ? "Connection Successfully Refreshed" : "Connection Failed to Refresh");
    }
}
