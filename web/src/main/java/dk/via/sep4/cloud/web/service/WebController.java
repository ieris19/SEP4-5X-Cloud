package dk.via.sep4.cloud.web.service;

import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.web.data.WebRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is used to create a REST API for the web application.
 * It gets the data from the database and returns it as JSON objects upon request.
 * ResponseEntity is used to return the data as an HTTP response.
 */
@Slf4j
@RestController
@RequestMapping()
public class WebController {
    private final WebRepository repository;
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

    @PutMapping("/limits")
    public ResponseEntity<String> updateLimits(@RequestBody String jsonLimits) {
        try {
            repository.updateLimits(new SensorLimits(jsonLimits));
            return ResponseEntity.ok("Limits updated successfully!");
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/state")
    public ResponseEntity<String> getState() {
        try {
            return ResponseEntity.ok(repository.getState());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PutMapping("/state")
    public ResponseEntity<String> updateState(@RequestBody String state) {
        try {
            repository.updateState(state);
            return ResponseEntity.ok("State updated successfully!");
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PutMapping("/comment")
    public ResponseEntity<String> addComment(@RequestBody String body) {
        try {
            repository.addComment(body);
            return ResponseEntity.ok("Comment added successfully!");
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<String> handleException(Exception e) {
        Class<?> exceptionClass = e.getClass();
        if (exceptionClass.isInstance(JSONException.class)) {
            return ResponseEntity.badRequest().body("Cannot parse JSON" + e.getMessage());
        }
        log.error("An internal error has occurred serving a request!", e);
        Class<? extends Exception> errorClass = e.getClass();
        StringBuilder errorMessage = new StringBuilder("An internal error has occurred: ");
        errorMessage.append(errorClass.getSimpleName());
        if (e.getMessage() != null) {
            errorMessage.append("\nThe following details were provided: ").append(e.getMessage());
        }
        return ResponseEntity.internalServerError().body(errorMessage.toString());
    }
}
