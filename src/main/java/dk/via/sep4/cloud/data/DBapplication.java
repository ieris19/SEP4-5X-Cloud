package dk.via.sep4.cloud.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dk.via.sep4.cloud.data.repository.SensorRepository;
import org.bson.Document;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DBapplication {
    public static void main(String[] args) {
        DBrepository database=new DBrepository();

        //Test insert
        //database.insertReading(new SensorReading(true, 27, 30, 15, 1, 5, 404, Timestamp.valueOf(LocalDateTime.of(2023, 5, 12, 13, 4, 15))));

        //Setting up limits
        SensorLimits limits=new SensorLimits();

        limits.setTemperature(27);
        limits.setHumidity(27);
        limits.setCo2(27);
        limits.setSound(27);
        limits.setLight(27);

        database.updateTemperatureLimit(limits);
        database.updateHumidityLimit(limits);
        database.updateCo2Limit(limits);
        database.updateSoundLimit(limits);
        database.updateLightLimit(limits);
//      database.setUp(limits);
    }
}
