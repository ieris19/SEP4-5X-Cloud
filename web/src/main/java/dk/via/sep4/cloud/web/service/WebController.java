package dk.via.sep4.cloud.web.service;

import dk.via.sep4.cloud.web.data.WebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class WebController {
    private final WebRepository repository;

    @Autowired
    public WebController(WebRepository webRepository) {
        repository = webRepository;
    }

    @GetMapping("/readings")
    public ResponseEntity<String> getReadings() {
        return ResponseEntity.ok(repository.getReadings());
    }

    @GetMapping("/limits")
    public ResponseEntity<String> getLimits() {
        return ResponseEntity.ok(repository.getLimits());
    }

    @GetMapping("/credentials")
    public ResponseEntity<String> getCredentials() {
        return ResponseEntity.ok(repository.getCredentials());
    }
}
