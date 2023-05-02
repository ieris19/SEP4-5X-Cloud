package dk.via.sep4.cloud.web.service;

import dk.via.sep4.cloud.Persistance.SensorReading;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WebController {
    @GetMapping("/lastReading")
    public ResponseEntity<Double> getLastReading() {
        return ResponseEntity.ok(SensorReading.getLastReading().getTemperature());
    }
}
