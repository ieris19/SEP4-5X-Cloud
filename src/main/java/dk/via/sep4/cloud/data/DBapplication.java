package dk.via.sep4.cloud.data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DBapplication {
    public static void main(String[] args) {
        //DB repo initialization
        DBrepository database=new DBrepository();

        //Inserting reading example
//        database.insertReading(new SensorReading(true, 41, 80, 369, 42, 150, 527, Timestamp.valueOf(LocalDateTime.of(2023, 5, 15, 13, 22, 23))));
//
//        //Retrieving readings example
//        SensorReading[] list= database.getReadings();
//        for (SensorReading reading:list) {
//            System.out.println(reading.toString());
//        }
//
//        //Inserting limits example
//        database.insertLimits(new SensorLimits(10, 35, 20, 80, 1000));
//
//        //Retrieving limits example
//        System.out.println(database.getLimits().toString());

//        //Updating limits example
//        database.updateLimits(new SensorLimits(10, 35, 20, 80, 3000));
//        System.out.println(database.getLimits());

        //Inserting credentials example
        database.insertCredentials(new UserCredentials("Ryzhas_Momentas", "fotografuje_slimaka"));

        //Retrieving credentials example
        System.out.println(database.getCredentials().toString());

    }
}
