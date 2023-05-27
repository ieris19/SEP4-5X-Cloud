package dk.via.sep4.cloud.data.repository;

import com.ieris19.lib.files.config.FileProperties;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.data.dto.SensorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;

class ActiveMongoRepositoryTest {
    private static MongoRepository mongoRepository;
    private static URI mongoURI;
    private static Logger logger = LoggerFactory.getLogger("DB-TEST");


    private static String getURI() {
        try (FileProperties secret = FileProperties.getInstance("secrets")) {
            return secret.getProperty("mongodb.url");
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("At least one of the accessed properties in secrets.properties doesn't exist", e);
        } catch (IOException e) {
            throw new IllegalStateException("Error reading secrets.properties", e);
        }
    }

    public static void main(String[] args) {
        setUp();
        //Inserting reading example
//        mongoRepository.insertReading(new SensorReading(true, 41, 80, 369, 42, 150, 527, Timestamp.valueOf(LocalDateTime.of(2023, 5, 15, 13, 22, 23))));
//        //Retrieving readings example
        SensorReading[] list = mongoRepository.getReadings("2023-05-15");
//
        for (SensorReading reading : list) {
            logger.info(reading.toString());
        }
//
//        //Inserting sensor limits example
//        mongoRepository.insertLimits(new SensorLimits(10, 35, 20, 80, 1000));
//
//        //Retrieving limits example
        logger.info(mongoRepository.getLimits().toString());
//
//        //Updating limits example
        mongoRepository.updateLimits("10", "35", "20", "80", "3000");
        logger.info(mongoRepository.getLimits().toString());
        logger.info(mongoRepository.getReadings("2023-05-15").toString());

//        for (SensorReading reading: list) {
//            System.out.println(reading.toString());
//        }

        //Inserting sensor state example
        mongoRepository.insertState(new SensorState(true));

        //Retrieving sensor state example
        System.out.println(mongoRepository.getState().toString());

        //Updating sensor state example
        mongoRepository.updateState("false");
        System.out.println(mongoRepository.getState().toString());
    }

    private static void setUp() {
        mongoRepository = new MongoRepository();
        mongoRepository.init(getURI(), "SEP4TEST");
        mongoRepository.clearEntireDatabase();
    }
}