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
        database.insertReading(new SensorReading(true, true, 27, 30, 15, 1, 5, 404, Timestamp.valueOf(LocalDateTime.of(2023, 5, 12, 13, 4, 15))));
    }
}
