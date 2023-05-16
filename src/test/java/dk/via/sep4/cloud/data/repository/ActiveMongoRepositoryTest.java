package dk.via.sep4.cloud.data.repository;

import dk.via.sep4.cloud.data.SensorLimits;
import dk.via.sep4.cloud.data.SensorReading;
import dk.via.sep4.cloud.data.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;

class ActiveMongoRepositoryTest {
    private static MongoRepository mongoRepository;

    public static void main(String[] args) {
        setUp();
        Logger logger = LoggerFactory.getLogger("DB-TEST");
        //Inserting reading example
        mongoRepository.insertReading(new SensorReading(true, 41, 80, 369, 42, 150, 527, Timestamp.valueOf(LocalDateTime.of(2023, 5, 15, 13, 22, 23))));
        //Retrieving readings example
        SensorReading[] list = mongoRepository.getReadings();
        for (SensorReading reading : list) {
            logger.info(reading.toString());
        }

        //Inserting sensor limits example
        mongoRepository.insertLimits(new SensorLimits(10, 35, 20, 80, 1000));

        //Retrieving limits example
        logger.info(mongoRepository.getLimits().toString());

        //Updating limits example
        mongoRepository.updateLimits(new SensorLimits(10, 35, 20, 80, 3000));
        logger.info(mongoRepository.getLimits().toString());

        //Inserting user credentials example
        mongoRepository.insertCredentials(new UserCredentials("Ryzhas_Momentas", "fotografuje_slimaka"));

        //Retrieving credentials example
        logger.info(mongoRepository.getCredentials().toString());
    }


    private static void setUp() {
        mongoRepository = new MongoRepository();
        mongoRepository.init("mongodb://localhost:27017", "SEP4TEST");
        mongoRepository.clearEntireDatabase();
    }
}