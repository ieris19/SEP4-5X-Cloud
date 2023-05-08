package dk.via.sep4.cloud.data.repository;

import dk.via.sep4.cloud.data.SensorReading;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SensorRepository extends MongoRepository<SensorReading, String> {
}
