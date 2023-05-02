package dk.via.sep4.cloud.web.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class WebController {
    @GetMapping("/lastReading")
    public ResponseEntity<String> getLastReading() {
        return ResponseEntity.ok(WebJSONData.getDataAsJSON());
    }
}
