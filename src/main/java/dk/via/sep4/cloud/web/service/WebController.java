package dk.via.sep4.cloud.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.via.sep4.cloud.data.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class WebController {
    private MongoRepository repository=new MongoRepository();
    @GetMapping("/readings")
    public ResponseEntity<String> getReadings() throws JsonProcessingException {
        return ResponseEntity.ok(WebJSONData.getReadingsAsJSON(repository.getReadings()));
    }

    @GetMapping("/limits")
    public ResponseEntity<String> getLimits() {
        return ResponseEntity.ok(WebJSONData.getLimitsAsJSON(repository.getLimits()));
    }

    @GetMapping("/credentials")
    public ResponseEntity<String> getCredentials() {
        return ResponseEntity.ok(WebJSONData.getCredentialsAsJSON(repository.getCredentials()));
    }
}
