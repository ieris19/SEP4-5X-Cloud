package dk.via.sep4.cloud.data;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DBapplication {
    public static void main(String[] args) throws JsonProcessingException {
        DBrepository database=new DBrepository();

        database.insertReading(new SensorReading(true, 41, 80, 369, 42, 150, 527, Timestamp.valueOf(LocalDateTime.of(2023, 5, 15, 13, 22, 23))));

        SensorReading[] list= database.getReadings();
        for (SensorReading reading:list) {
            System.out.println(reading.toString());
        }
    }
}
