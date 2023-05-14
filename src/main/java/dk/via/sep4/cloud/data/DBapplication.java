package dk.via.sep4.cloud.data;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DBapplication {
    public static void main(String[] args) throws JsonProcessingException {
        DBrepository database=new DBrepository();

        SensorReading[] list= database.getReadings();
        for (SensorReading reading:list) {
            System.out.println(reading.toString());
        }
    }
}
