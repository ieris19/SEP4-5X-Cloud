package dk.via.sep4.cloud.web.service;

import dk.via.sep4.cloud.web.data.WebRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is used to create a REST API for the web application.
 * It gets the data from the database and returns it as JSON objects upon request.
 * ResponseEntity is used to return the data as an HTTP response.
 */
@RestController
@RequestMapping()
public class WebController {
    private final WebRepository repository;
    private final Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    public WebController(WebRepository webRepository) {
        repository = webRepository;
    }

    @GetMapping("/readings")
    public ResponseEntity<String> getReadings(@RequestParam String date) {
        try {
            return ResponseEntity.ok(repository.getReadings(date));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/limits")
    public ResponseEntity<String> getLimits() {
        try {
            return ResponseEntity.ok(repository.getLimits());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping("/limits")
    public ResponseEntity<String> updateLimits(@RequestBody String minTemp, @RequestBody String maxTemp, @RequestBody String minHumidity, @RequestBody String maxHumidity, @RequestBody String maxCO2) {
        try {
            repository.updateLimits(minTemp, maxTemp, minHumidity, maxHumidity, maxCO2);
            return ResponseEntity.ok("Limits updated successfully!");
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<String> handleException(Exception e) {
        logger.error("An internal error has occurred serving a request!", e);
        Class<? extends Exception> errorClass = e.getClass();
        StringBuilder errorMessage = new StringBuilder("An internal error has occurred: ");
        errorMessage.append(errorClass.getSimpleName());
        if (e.getMessage() != null) {
            errorMessage.append("\nThe following details were provided: ").append(e.getMessage());
        }
        return ResponseEntity.internalServerError().body(errorMessage.toString());
    }
}
