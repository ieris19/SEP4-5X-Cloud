package dk.via.sep4.cloud.data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DBapplication {
    public static void main(String[] args) {
        DBrepository database=new DBrepository();

//        //Setting up a list of readings
//        database.insertReading(new SensorReading(true, 27, 30, 15, 1, 5, 404, Timestamp.valueOf(LocalDateTime.of(2023, 5, 12, 13, 4, 15))));
//        database.insertReading(new SensorReading(true, 21, 45, 500, 30, 414, 500, Timestamp.valueOf(LocalDateTime.of(2023, 5, 14, 11, 43, 14))));
//        database.insertReading(new SensorReading(false, 24, 60, 700, 27, 501, 282, Timestamp.valueOf(LocalDateTime.of(2023, 5, 21, 10, 30, 25))));
//        //Setting up limits
        SensorLimits limits=new SensorLimits();

        limits.setTemperature(50);
        limits.setHumidity(50);
        limits.setCo2(50);
        limits.setSound(50);
        limits.setLight(50);

//        database.setUp(limits);
        database.updateLimits(limits);

        System.out.println(database.getReadings());
    }
}
